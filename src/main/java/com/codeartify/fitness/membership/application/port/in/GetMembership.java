package com.codeartify.fitness.membership.application.port.in;

import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;

public interface GetMembership {
    Membership get(MembershipId membershipId);
}
