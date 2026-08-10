package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.ResumeMembership;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@ApplicationScoped
public class ResumeMembershipUseCase implements ResumeMembership {

    private final MembershipRepository membershipRepository;
    private final Clock clock;

    @Inject
    public ResumeMembershipUseCase(MembershipRepository membershipRepository, Clock clock) {
        this.membershipRepository = membershipRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Membership resume(MembershipId membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipNotFoundException(membershipId));

        membership.resume(LocalDate.now(clock));
        return membershipRepository.save(membership);
    }
}
