package com.codeartify.fitness.membership.domain;

import java.util.Locale;
import java.util.Set;

public record PlanCode(String value) {
    private static final Set<String> SUPPORTED_CODES = Set.of("STANDARD", "PREMIUM");

    public PlanCode {
        if (value == null || value.isBlank()) {
            throw new MembershipDomainException("Plan code is required.");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!SUPPORTED_CODES.contains(value)) {
            throw new MembershipDomainException("Plan code is not supported.");
        }
    }
}
