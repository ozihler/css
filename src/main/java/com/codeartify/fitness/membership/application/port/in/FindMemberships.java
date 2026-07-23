package com.codeartify.fitness.membership.application.port.in;

public interface FindMemberships {
    MembershipPage find(MembershipSearch search);
}
