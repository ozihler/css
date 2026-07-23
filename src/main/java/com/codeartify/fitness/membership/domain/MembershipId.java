package com.codeartify.fitness.membership.domain;

import java.util.UUID;

public record MembershipId(String value) {
    public MembershipId {
        if (value == null || value.isBlank()) {
            throw new MembershipDomainException("Membership ID is required.");
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new MembershipDomainException("Membership ID must be a UUID.");
        }
    }

    public static MembershipId newId() {
        return new MembershipId(UUID.randomUUID().toString());
    }
}
