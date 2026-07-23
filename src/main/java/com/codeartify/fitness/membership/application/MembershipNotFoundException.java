package com.codeartify.fitness.membership.application;

import com.codeartify.fitness.membership.domain.MembershipId;

public class MembershipNotFoundException extends RuntimeException {
    private final MembershipId membershipId;

    public MembershipNotFoundException(MembershipId membershipId) {
        super("Membership " + membershipId.value() + " was not found.");
        this.membershipId = membershipId;
    }

    public MembershipId membershipId() {
        return membershipId;
    }
}
