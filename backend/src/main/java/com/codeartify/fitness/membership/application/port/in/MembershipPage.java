package com.codeartify.fitness.membership.application.port.in;

import com.codeartify.fitness.membership.domain.Membership;

import java.util.List;

public record MembershipPage(int page, int size, long totalElements, List<Membership> memberships) {
    public MembershipPage {
        memberships = List.copyOf(memberships);
    }
}
