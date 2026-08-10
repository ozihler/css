package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.PauseMembershipCommand;
import com.codeartify.fitness.membership.application.port.in.SignUpMembershipCommand;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class ResumeMembershipUseCaseTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-08-10T08:00:00Z"), ZoneOffset.UTC);

    private final InMemoryMembershipRepository repository = new InMemoryMembershipRepository();
    private final SignUpMembershipService signUpService = new SignUpMembershipService(repository, CLOCK);
    private final PauseMembershipService pauseService = new PauseMembershipService(repository, CLOCK);
    private final ResumeMembershipUseCase resumeService = new ResumeMembershipUseCase(repository, CLOCK);

    @Test
    void resumes_a_paused_membership() {
        Membership membership = signUpService.signUp(
                new SignUpMembershipCommand("Maya Chen", "maya@example.com", "PREMIUM"));
        pauseService.pause(membership.id(), new PauseMembershipCommand(45));

        Membership resumedMembership = resumeService.resume(membership.id());

        assertThat(resumedMembership.statusOn(LocalDate.of(2026, 8, 10))).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(resumedMembership.pausePeriod()).isNull();
        assertThat(repository.findById(membership.id())).containsSame(resumedMembership);
    }
}
