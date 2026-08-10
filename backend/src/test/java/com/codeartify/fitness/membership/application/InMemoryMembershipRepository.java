package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.application.port.in.MembershipPage;
import com.codeartify.fitness.membership.application.port.out.MembershipRepository;
import com.codeartify.fitness.membership.domain.EmailAddress;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.MembershipStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryMembershipRepository implements MembershipRepository {
    private final Map<MembershipId, Membership> memberships = new LinkedHashMap<>();

    @Override
    public Optional<Membership> findById(MembershipId membershipId) {
        return Optional.ofNullable(memberships.get(membershipId));
    }

    @Override
    public Optional<Membership> findByEmailAddress(EmailAddress emailAddress) {
        return memberships.values().stream()
                .filter(membership -> membership.emailAddress().equals(emailAddress))
                .findFirst();
    }

    @Override
    public Membership save(Membership membership) {
        memberships.put(membership.id(), membership);
        return membership;
    }

    @Override
    public MembershipPage findMemberships(int page, int size, MembershipStatus status, LocalDate businessDate) {
        var matches = memberships.values().stream()
                .filter(membership -> status == null || membership.statusOn(businessDate) == status)
                .sorted(Comparator.comparing(Membership::activatedOn).reversed()
                        .thenComparing(membership -> membership.id().value()))
                .toList();
        int fromIndex = Math.min(page * size, matches.size());
        int toIndex = Math.min(fromIndex + size, matches.size());
        return new MembershipPage(page, size, matches.size(), matches.subList(fromIndex, toIndex));
    }
}
