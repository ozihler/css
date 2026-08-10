package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.PauseMembership;
import com.codeartify.fitness.membership.application.port.in.PauseMembershipCommand;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@ApplicationScoped
public class PauseMembershipService implements PauseMembership {
    private final MembershipRepository membershipRepository;
    private final Clock clock;

    @Inject
    public PauseMembershipService(MembershipRepository membershipRepository, Clock clock) {
        this.membershipRepository = membershipRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Membership pause(MembershipId membershipId, PauseMembershipCommand command) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipNotFoundException(membershipId));
        membership.pause(command.durationInDays(), LocalDate.now(clock));
        return membershipRepository.save(membership);
    }
}
