package com.codeartify.fitness.membership.application.port.out;

import com.codeartify.fitness.membership.application.port.in.MembershipPage;
import com.codeartify.fitness.membership.domain.EmailAddress;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.MembershipStatus;

import java.time.LocalDate;
import java.util.Optional;

public interface MembershipRepository {
    Optional<Membership> findById(MembershipId membershipId);

    Optional<Membership> findByEmailAddress(EmailAddress emailAddress);

    Membership save(Membership membership);

    MembershipPage findMemberships(int page, int size, MembershipStatus status, LocalDate businessDate);
}
