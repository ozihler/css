package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.MembershipSearch;
import com.codeartify.fitness.membership.application.port.in.SignUpMembershipCommand;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MembershipQueryServiceTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-08-01T08:00:00Z"), ZoneOffset.UTC);

    private final InMemoryMembershipRepository repository = new InMemoryMembershipRepository();
    private final SignUpMembershipService signUpService = new SignUpMembershipService(repository, CLOCK);
    private final MembershipQueryService queryService = new MembershipQueryService(repository, new PageSizePolicy(20, 50), CLOCK);

    @Test
    void gets_an_existing_membership() {
        Membership membership = signUpService.signUp(new SignUpMembershipCommand("Jane Doe", "jane@example.com", "STANDARD"));

        assertThat(queryService.get(membership.id())).isSameAs(membership);
    }

    @Test
    void rejects_getting_an_unknown_membership() {
        var membershipId = com.codeartify.fitness.membership.domain.MembershipId.newId();

        assertThatThrownBy(() -> queryService.get(membershipId))
                .isInstanceOf(MembershipNotFoundException.class);
    }

    @Test
    void applies_default_and_maximum_page_size() {
        signUpService.signUp(new SignUpMembershipCommand("Jane Doe", "jane@example.com", "STANDARD"));

        var defaultPage = queryService.find(new MembershipSearch(0, null, null));
        var cappedPage = queryService.find(new MembershipSearch(0, 100, null));

        assertThat(defaultPage.size()).isEqualTo(20);
        assertThat(cappedPage.size()).isEqualTo(50);
    }

    @Test
    void filters_by_effective_status() {
        Membership membership = signUpService.signUp(new SignUpMembershipCommand("Jane Doe", "jane@example.com", "STANDARD"));
        membership.pause(30, LocalDate.of(2026, 8, 1));
        repository.save(membership);

        var page = queryService.find(new MembershipSearch(0, 20, MembershipStatus.PAUSED));

        assertThat(page.totalElements()).isEqualTo(1);
        assertThat(page.memberships()).containsExactly(membership);
    }
}
