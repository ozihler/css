package com.codeartify.fitness.membership.application;

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

class SignUpMembershipServiceTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-07-23T08:00:00Z"), ZoneOffset.UTC);

    private final InMemoryMembershipRepository repository = new InMemoryMembershipRepository();
    private final SignUpMembershipService service = new SignUpMembershipService(repository, CLOCK);

    @Test
    void signs_up_a_membership_with_the_current_business_date() {
        Membership membership = service.signUp(new SignUpMembershipCommand("Jane Doe", "JANE@example.com", "STANDARD"));

        assertThat(membership.id().value()).isNotBlank();
        assertThat(membership.activatedOn()).isEqualTo(LocalDate.of(2026, 7, 23));
        assertThat(membership.emailAddress().value()).isEqualTo("jane@example.com");
        assertThat(membership.statusOn(LocalDate.of(2026, 7, 23))).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(repository.findById(membership.id())).containsSame(membership);
    }

    @Test
    void rejects_a_duplicate_email_address_before_persistence() {
        service.signUp(new SignUpMembershipCommand("Jane Doe", "jane@example.com", "STANDARD"));

        assertThatThrownBy(() -> service.signUp(new SignUpMembershipCommand("Jane Other", "JANE@example.com", "PREMIUM")))
                .isInstanceOf(DuplicateEmailAddressException.class)
                .hasMessage("Email address jane@example.com is already registered.");
    }
}
