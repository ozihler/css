package com.codeartify.fitness.membership.domain;

public class MembershipAlreadyPausedException extends MembershipDomainException {
    public MembershipAlreadyPausedException(String message) {
        super(message);
    }
}
