package com.codeartify.fitness.membership.application.port.in;

import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;

public interface PauseMembership {
    Membership pause(MembershipId membershipId, PauseMembershipCommand command);
}
