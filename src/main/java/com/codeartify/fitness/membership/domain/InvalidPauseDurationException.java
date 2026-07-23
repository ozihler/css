package com.codeartify.fitness.membership.domain;

public class InvalidPauseDurationException extends MembershipDomainException {
    public InvalidPauseDurationException(String message) {
        super(message);
    }
}
