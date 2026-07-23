package com.codeartify.fitness.membership.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MembershipTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 7, 23);

    @Test
    void creates_an_active_membership() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");

        assertThat(membership.id().value()).isNotBlank();
        assertThat(membership.memberName()).isEqualTo("Jane Doe");
        assertThat(membership.emailAddress().value()).isEqualTo("jane@example.com");
        assertThat(membership.planCode().value()).isEqualTo("STANDARD");
        assertThat(membership.activatedOn()).isEqualTo(TODAY);
        assertThat(membership.statusOn(TODAY)).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(membership.pausePeriod()).isNull();
    }

    @Test
    void normalizes_the_email_address() {
        Membership membership = newMembership("Jane Doe", "  JANE@Example.COM ", "PREMIUM");

        assertThat(membership.emailAddress().value()).isEqualTo("jane@example.com");
    }

    @Test
    void rejects_a_blank_member_name() {
        assertThatThrownBy(() -> newMembership(" ", "jane@example.com", "STANDARD"))
                .isInstanceOf(MembershipDomainException.class)
                .hasMessage("Member name is required.");
    }

    @Test
    void rejects_an_invalid_email_address() {
        assertThatThrownBy(() -> newMembership("Jane Doe", "not-an-email", "STANDARD"))
                .isInstanceOf(MembershipDomainException.class)
                .hasMessage("Email address must be syntactically valid.");
    }

    @Test
    void rejects_an_unsupported_plan() {
        assertThatThrownBy(() -> newMembership("Jane Doe", "jane@example.com", "BASIC"))
                .isInstanceOf(MembershipDomainException.class)
                .hasMessage("Plan code is not supported.");
    }

    @Test
    void pauses_an_active_membership_for_30_days() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");

        membership.pause(30, LocalDate.of(2026, 8, 1));

        assertThat(membership.pausePeriod().pausedFrom()).isEqualTo(LocalDate.of(2026, 8, 1));
        assertThat(membership.pausePeriod().resumeOn()).isEqualTo(LocalDate.of(2026, 8, 31));
        assertThat(membership.statusOn(LocalDate.of(2026, 8, 30))).isEqualTo(MembershipStatus.PAUSED);
        assertThat(membership.statusOn(LocalDate.of(2026, 8, 31))).isEqualTo(MembershipStatus.ACTIVE);
    }

    @Test
    void pauses_an_active_membership_for_60_days() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");

        membership.pause(60, TODAY);

        assertThat(membership.pausePeriod().resumeOn()).isEqualTo(TODAY.plusDays(60));
    }

    @Test
    void rejects_a_pause_shorter_than_30_days() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");

        assertThatThrownBy(() -> membership.pause(29, TODAY))
                .isInstanceOf(InvalidPauseDurationException.class)
                .hasMessage("Pause duration must be between 30 and 60 days inclusive.");
    }

    @Test
    void rejects_a_pause_longer_than_60_days() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");

        assertThatThrownBy(() -> membership.pause(61, TODAY))
                .isInstanceOf(InvalidPauseDurationException.class)
                .hasMessage("Pause duration must be between 30 and 60 days inclusive.");
    }

    @Test
    void rejects_pausing_a_membership_that_is_currently_paused() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");
        membership.pause(30, TODAY);

        assertThatThrownBy(() -> membership.pause(30, TODAY.plusDays(1)))
                .isInstanceOf(MembershipAlreadyPausedException.class)
                .hasMessage("Membership is already paused.");
    }

    @Test
    void allows_pausing_again_after_the_previous_pause_expired() {
        Membership membership = newMembership("Jane Doe", "jane@example.com", "STANDARD");
        membership.pause(30, TODAY);

        membership.pause(30, TODAY.plusDays(30));

        assertThat(membership.pausePeriod().pausedFrom()).isEqualTo(TODAY.plusDays(30));
        assertThat(membership.statusOn(TODAY.plusDays(31))).isEqualTo(MembershipStatus.PAUSED);
    }

    private static Membership newMembership(String memberName, String email, String planCode) {
        return Membership.signUp(
                MembershipId.newId(),
                memberName,
                new EmailAddress(email),
                new PlanCode(planCode),
                TODAY);
    }
}
