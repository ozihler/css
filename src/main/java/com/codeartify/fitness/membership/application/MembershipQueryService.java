package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.FindMemberships;
import com.codeartify.fitness.membership.application.port.in.GetMembership;
import com.codeartify.fitness.membership.application.port.in.MembershipPage;
import com.codeartify.fitness.membership.application.port.in.MembershipSearch;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Clock;
import java.time.LocalDate;

@ApplicationScoped
public class MembershipQueryService implements GetMembership, FindMemberships {
    private final MembershipRepository membershipRepository;
    private final PageSizePolicy pageSizePolicy;
    private final Clock clock;

    @Inject
    public MembershipQueryService(
            MembershipRepository membershipRepository,
            PageSizePolicy pageSizePolicy,
            Clock clock) {
        this.membershipRepository = membershipRepository;
        this.pageSizePolicy = pageSizePolicy;
        this.clock = clock;
    }

    @Override
    public Membership get(MembershipId membershipId) {
        return membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipNotFoundException(membershipId));
    }

    @Override
    public MembershipPage find(MembershipSearch search) {
        if (search.page() < 0) {
            throw new IllegalArgumentException("Page number must not be negative.");
        }
        int pageSize = pageSizePolicy.resolve(search.size());
        return membershipRepository.findMemberships(search.page(), pageSize, search.status(), LocalDate.now(clock));
    }
}
