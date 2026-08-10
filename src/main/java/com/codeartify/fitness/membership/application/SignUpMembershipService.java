package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.SignUpMembership;
import com.codeartify.fitness.membership.application.port.in.SignUpMembershipCommand;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
import com.codeartify.fitness.membership.domain.EmailAddress;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.PlanCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@ApplicationScoped
public class SignUpMembershipService implements SignUpMembership {
    private final MembershipRepository membershipRepository;
    private final Clock clock;

    @Inject
    public SignUpMembershipService(MembershipRepository membershipRepository, Clock clock) {
        this.membershipRepository = membershipRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Membership signUp(SignUpMembershipCommand command) {
        EmailAddress emailAddress = new EmailAddress(command.email());
        membershipRepository.findByEmailAddress(emailAddress)
                .ifPresent(_ -> {
                    throw new DuplicateEmailAddressException(emailAddress.value());
                });

        Membership membership = Membership.signUp(
                MembershipId.newId(),
                command.memberName(),
                emailAddress,
                new PlanCode(command.planCode()),
                LocalDate.now(clock));

        return membershipRepository.save(membership);
    }
}
