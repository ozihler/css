package com.codeartify.fitness.membership.adapter.in.rest.response;

import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import jakarta.json.bind.annotation.JsonbNillable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "Membership")
@JsonbNillable
public record MembershipResponse(
        String id,
        String memberName,
        String email,
        String planCode,
        MembershipStatus status,
        LocalDate activatedOn,
        LocalDate pausedFrom,
        LocalDate resumeOn) {
    public static MembershipResponse from(Membership membership, LocalDate businessDate) {
        LocalDate pausedFrom = membership.pausePeriod() == null ? null : membership.pausePeriod().pausedFrom();
        LocalDate resumeOn = membership.pausePeriod() == null ? null : membership.pausePeriod().resumeOn();
        return new MembershipResponse(
                membership.id().value(),
                membership.memberName(),
                membership.emailAddress().value(),
                membership.planCode().value(),
                membership.statusOn(businessDate),
                membership.activatedOn(),
                pausedFrom,
                resumeOn);
    }
}
