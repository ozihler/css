package com.codeartify.fitness.membership.adapter.out.persistence;

import com.codeartify.fitness.membership.application.DuplicateEmailAddressException;
import com.codeartify.fitness.membership.domain.EmailAddress;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import com.codeartify.fitness.membership.domain.PlanCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class JpaMembershipRepositoryTest {
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18-alpine");

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;
    private JpaMembershipRepository repository;

    @BeforeAll
    static void createEntityManagerFactory() {
        Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();

        entityManagerFactory = Persistence.createEntityManagerFactory("fitness-test", Map.of(
                "jakarta.persistence.jdbc.driver", POSTGRES.getDriverClassName(),
                "jakarta.persistence.jdbc.url", POSTGRES.getJdbcUrl(),
                "jakarta.persistence.jdbc.user", POSTGRES.getUsername(),
                "jakarta.persistence.jdbc.password", POSTGRES.getPassword()
        ));
    }

    @AfterAll
    static void closeEntityManagerFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    void createRepository() {
        entityManager = entityManagerFactory.createEntityManager();
        repository = new JpaMembershipRepository(entityManager);
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("delete from memberships").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @AfterEach
    void closeEntityManager() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    void round_trips_a_membership_mapping() {
        Membership membership = membership("Jane Doe", "jane@example.com", "STANDARD", LocalDate.of(2026, 7, 23));
        membership.pause(30, LocalDate.of(2026, 8, 1));

        inTransaction(() -> repository.save(membership));

        assertThat(repository.findById(membership.id()))
                .get()
                .satisfies(saved -> {
                    assertThat(saved.emailAddress().value()).isEqualTo("jane@example.com");
                    assertThat(saved.pausePeriod().pausedFrom()).isEqualTo(LocalDate.of(2026, 8, 1));
                    assertThat(saved.pausePeriod().resumeOn()).isEqualTo(LocalDate.of(2026, 8, 31));
                });
    }

    @Test
    void translates_the_unique_email_constraint() {
        Membership first = membership("Jane Doe", "jane@example.com", "STANDARD", LocalDate.of(2026, 7, 23));
        Membership second = membership("Jane Other", "jane@example.com", "PREMIUM", LocalDate.of(2026, 7, 24));

        inTransaction(() -> repository.save(first));

        assertThatThrownBy(() -> inTransaction(() -> repository.save(second)))
                .isInstanceOf(DuplicateEmailAddressException.class)
                .hasMessage("Email address jane@example.com is already registered.");
    }

    @Test
    void supports_pagination_status_filtering_and_deterministic_sorting() {
        Membership active = membership("Active", "active@example.com", "STANDARD", LocalDate.of(2026, 7, 25));
        Membership paused = membership("Paused", "paused@example.com", "PREMIUM", LocalDate.of(2026, 7, 25));
        Membership older = membership("Older", "older@example.com", "STANDARD", LocalDate.of(2026, 7, 20));
        paused.pause(30, LocalDate.of(2026, 8, 1));

        inTransaction(() -> {
            repository.save(older);
            repository.save(paused);
            repository.save(active);
        });

        var all = repository.findMemberships(0, 2, null, LocalDate.of(2026, 8, 2));
        var pausedPage = repository.findMemberships(0, 20, MembershipStatus.PAUSED, LocalDate.of(2026, 8, 2));
        var activePage = repository.findMemberships(0, 20, MembershipStatus.ACTIVE, LocalDate.of(2026, 8, 2));

        assertThat(all.totalElements()).isEqualTo(3);
        assertThat(all.memberships()).hasSize(2);
        assertThat(all.memberships()).extracting(membership -> membership.activatedOn())
                .containsExactly(LocalDate.of(2026, 7, 25), LocalDate.of(2026, 7, 25));
        assertThat(pausedPage.memberships()).extracting(membership -> membership.emailAddress().value())
                .containsExactly("paused@example.com");
        assertThat(activePage.memberships()).extracting(membership -> membership.emailAddress().value())
                .containsExactly("active@example.com", "older@example.com");
    }

    @Test
    void detects_optimistic_lock_conflicts() {
        Membership membership = membership("Jane Doe", "jane@example.com", "STANDARD", LocalDate.of(2026, 7, 23));
        inTransaction(() -> repository.save(membership));
        entityManager.clear();

        EntityManager firstEntityManager = entityManagerFactory.createEntityManager();
        EntityManager secondEntityManager = entityManagerFactory.createEntityManager();
        try {
            MembershipJpaEntity first = firstEntityManager.find(MembershipJpaEntity.class, membership.id().value());
            MembershipJpaEntity second = secondEntityManager.find(MembershipJpaEntity.class, membership.id().value());

            firstEntityManager.getTransaction().begin();
            first.updateFrom(new MembershipJpaEntity(
                    first.id(), first.normalizedEmail(), "Jane First", first.planCode(),
                    first.activatedOn(), first.pausedFrom(), first.resumeOn()));
            firstEntityManager.getTransaction().commit();

            secondEntityManager.getTransaction().begin();
            second.updateFrom(new MembershipJpaEntity(
                    second.id(), second.normalizedEmail(), "Jane Second", second.planCode(),
                    second.activatedOn(), second.pausedFrom(), second.resumeOn()));

            assertThatThrownBy(() -> {
                secondEntityManager.flush();
                secondEntityManager.getTransaction().commit();
            }).isInstanceOf(OptimisticLockException.class);
        } finally {
            rollbackIfActive(firstEntityManager);
            rollbackIfActive(secondEntityManager);
            firstEntityManager.close();
            secondEntityManager.close();
        }
    }

    private void inTransaction(Runnable action) {
        entityManager.getTransaction().begin();
        try {
            action.run();
            entityManager.getTransaction().commit();
        } catch (RuntimeException exception) {
            rollbackIfActive(entityManager);
            throw exception;
        }
    }

    private static void rollbackIfActive(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static Membership membership(String memberName, String email, String planCode, LocalDate activatedOn) {
        return Membership.signUp(
                MembershipId.newId(),
                memberName,
                new EmailAddress(email),
                new PlanCode(planCode),
                activatedOn);
    }
}
