package com.codeartify.fitness.membership.domain;

public class ActiveMembershipCannotBeResumedException extends MembershipDomainException {
    public ActiveMembershipCannotBeResumedException(String message) {
        super(message);
    }
}
