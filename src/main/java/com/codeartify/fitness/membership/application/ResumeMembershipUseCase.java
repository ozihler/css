package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.ResumeMembership;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
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
    public void resumeFor(String membershipId) {
        var id = new MembershipId(membershipId);
        var membership = this.membershipRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException(id));

        membership.resume(LocalDate.now(clock));

        this.membershipRepository.save(membership);
    }
}
