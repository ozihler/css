package com.codeartify.fitness.membership.application.port.in;

import com.codeartify.fitness.membership.domain.Membership;

public interface SignUpMembership {
    Membership signUp(SignUpMembershipCommand command);
}
