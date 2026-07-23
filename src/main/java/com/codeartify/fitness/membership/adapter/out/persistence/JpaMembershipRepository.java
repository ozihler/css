package com.codeartify.fitness.membership.adapter.out.persistence;

import com.codeartify.fitness.membership.application.DuplicateEmailAddressException;
import com.codeartify.fitness.membership.application.port.in.MembershipPage;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
import com.codeartify.fitness.membership.domain.EmailAddress;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaMembershipRepository implements MembershipRepository {
    private static final String POSTGRES_UNIQUE_VIOLATION = "23505";

    @PersistenceContext(unitName = "fitness")
    private EntityManager entityManager;

    private final MembershipPersistenceMapper mapper = new MembershipPersistenceMapper();

    public JpaMembershipRepository() {
    }

    JpaMembershipRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Membership> findById(MembershipId membershipId) {
        return Optional.ofNullable(entityManager.find(MembershipJpaEntity.class, membershipId.value()))
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Membership> findByEmailAddress(EmailAddress emailAddress) {
        return entityManager
                .createQuery("""
                        select membership
                        from MembershipJpaEntity membership
                        where membership.normalizedEmail = :email
                        """, MembershipJpaEntity.class)
                .setParameter("email", emailAddress.value())
                .getResultStream()
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public Membership save(Membership membership) {
        MembershipJpaEntity source = mapper.toEntity(membership);
        MembershipJpaEntity existing = entityManager.find(MembershipJpaEntity.class, membership.id().value());
        try {
            if (existing == null) {
                entityManager.persist(source);
            } else {
                existing.updateFrom(source);
            }
            entityManager.flush();
            return membership;
        } catch (PersistenceException exception) {
            if (isUniqueEmailViolation(exception)) {
                throw new DuplicateEmailAddressException(membership.emailAddress().value());
            }
            throw exception;
        }
    }

    @Override
    public MembershipPage findMemberships(int page, int size, MembershipStatus status, LocalDate businessDate) {
        String filter = statusFilter(status);
        List<Membership> memberships = entityManager
                .createQuery("""
                        select membership
                        from MembershipJpaEntity membership
                        %s
                        order by membership.activatedOn desc, membership.id asc
                        """.formatted(filter), MembershipJpaEntity.class)
                .setParameter("businessDate", businessDate)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList()
                .stream()
                .map(mapper::toDomain)
                .toList();

        long totalElements = entityManager
                .createQuery("""
                        select count(membership)
                        from MembershipJpaEntity membership
                        %s
                        """.formatted(filter), Long.class)
                .setParameter("businessDate", businessDate)
                .getSingleResult();

        return new MembershipPage(page, size, totalElements, memberships);
    }

    private static String statusFilter(MembershipStatus status) {
        if (status == MembershipStatus.ACTIVE) {
            return """
                    where membership.pausedFrom is null
                       or membership.resumeOn is null
                       or :businessDate < membership.pausedFrom
                       or :businessDate >= membership.resumeOn
                    """;
        }
        if (status == MembershipStatus.PAUSED) {
            return """
                    where membership.pausedFrom is not null
                      and membership.resumeOn is not null
                      and :businessDate >= membership.pausedFrom
                      and :businessDate < membership.resumeOn
                    """;
        }
        return "where :businessDate = :businessDate";
    }

    private static boolean isUniqueEmailViolation(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof SQLException sqlException
                    && POSTGRES_UNIQUE_VIOLATION.equals(sqlException.getSQLState())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
