package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.PauseMembershipCommand;
import com.codeartify.fitness.membership.application.port.in.SignUpMembershipCommand;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PauseMembershipServiceTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-08-01T08:00:00Z"), ZoneOffset.UTC);

    private final InMemoryMembershipRepository repository = new InMemoryMembershipRepository();
    private final SignUpMembershipService signUpService = new SignUpMembershipService(repository, CLOCK);
    private final PauseMembershipService pauseService = new PauseMembershipService(repository, CLOCK);

    @Test
    void pauses_an_existing_membership_using_the_current_business_date() {
        Membership membership = signUpService.signUp(new SignUpMembershipCommand("Jane Doe", "jane@example.com", "STANDARD"));

        Membership pausedMembership = pauseService.pause(membership.id(), new PauseMembershipCommand(30));

        assertThat(pausedMembership.pausePeriod().pausedFrom()).isEqualTo(LocalDate.of(2026, 8, 1));
        assertThat(pausedMembership.pausePeriod().resumeOn()).isEqualTo(LocalDate.of(2026, 8, 31));
        assertThat(pausedMembership.statusOn(LocalDate.of(2026, 8, 30))).isEqualTo(MembershipStatus.PAUSED);
    }

    @Test
    void rejects_pausing_an_unknown_membership() {
        MembershipId unknownId = MembershipId.newId();

        assertThatThrownBy(() -> pauseService.pause(unknownId, new PauseMembershipCommand(30)))
                .isInstanceOf(MembershipNotFoundException.class)
                .hasMessage("Membership " + unknownId.value() + " was not found.");
    }
}
