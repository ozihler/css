package com.codeartify.fitness.membership.application.port.in;

import com.codeartify.fitness.membership.domain.MembershipStatus;

public record MembershipSearch(int page, Integer size, MembershipStatus status) {
}
