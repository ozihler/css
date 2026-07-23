package com.codeartify.fitness.membership.adapter.out.persistence;

import com.codeartify.fitness.membership.domain.EmailAddress;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import com.codeartify.fitness.membership.domain.PausePeriod;
import com.codeartify.fitness.membership.domain.PlanCode;

import java.time.LocalDate;

public class MembershipPersistenceMapper {
    MembershipJpaEntity toEntity(Membership membership) {
        LocalDate pausedFrom = membership.pausePeriod() == null ? null : membership.pausePeriod().pausedFrom();
        LocalDate resumeOn = membership.pausePeriod() == null ? null : membership.pausePeriod().resumeOn();
        return new MembershipJpaEntity(
                membership.id().value(),
                membership.emailAddress().value(),
                membership.memberName(),
                membership.planCode().value(),
                membership.activatedOn(),
                pausedFrom,
                resumeOn);
    }

    Membership toDomain(MembershipJpaEntity entity) {
        PausePeriod pausePeriod = null;
        if (entity.pausedFrom() != null && entity.resumeOn() != null) {
            pausePeriod = new PausePeriod(entity.pausedFrom(), entity.resumeOn());
        }
        return Membership.restore(
                new MembershipId(entity.id()),
                entity.memberName(),
                new EmailAddress(entity.normalizedEmail()),
                new PlanCode(entity.planCode()),
                entity.activatedOn(),
                pausePeriod);
    }
}
