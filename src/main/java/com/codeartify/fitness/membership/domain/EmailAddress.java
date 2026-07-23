package com.codeartify.fitness.membership.domain;

import java.util.Locale;
import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final Pattern BASIC_EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public EmailAddress {
        if (value == null || value.isBlank()) {
            throw new MembershipDomainException("Email address is required.");
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.length() > 320 || !BASIC_EMAIL_PATTERN.matcher(value).matches()) {
            throw new MembershipDomainException("Email address must be syntactically valid.");
        }
    }
}
