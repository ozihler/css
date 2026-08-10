package com.codeartify.fitness.membership.domain;

import java.time.LocalDate;
import java.util.Objects;

public final class Membership {
    private final MembershipId id;
    private final String memberName;
    private final EmailAddress emailAddress;
    private final PlanCode planCode;
    private final LocalDate activatedOn;
    private PausePeriod pausePeriod;

    private Membership(
            MembershipId id,
            String memberName,
            EmailAddress emailAddress,
            PlanCode planCode,
            LocalDate activatedOn,
            PausePeriod pausePeriod) {
        this.id = Objects.requireNonNull(id, "id");
        this.memberName = requireMemberName(memberName);
        this.emailAddress = Objects.requireNonNull(emailAddress, "emailAddress");
        this.planCode = Objects.requireNonNull(planCode, "planCode");
        this.activatedOn = Objects.requireNonNull(activatedOn, "activatedOn");
        this.pausePeriod = pausePeriod;
    }

    public static Membership signUp(
            MembershipId id,
            String memberName,
            EmailAddress emailAddress,
            PlanCode planCode,
            LocalDate activatedOn) {
        return new Membership(id, memberName, emailAddress, planCode, activatedOn, null);
    }

    public static Membership restore(
            MembershipId id,
            String memberName,
            EmailAddress emailAddress,
            PlanCode planCode,
            LocalDate activatedOn,
            PausePeriod pausePeriod) {
        return new Membership(id, memberName, emailAddress, planCode, activatedOn, pausePeriod);
    }

    public void pause(int durationInDays, LocalDate today) {
        if (statusOn(today) == MembershipStatus.PAUSED) {
            throw new MembershipAlreadyPausedException("Membership is already paused.");
        }
        pausePeriod = PausePeriod.startingOn(today, durationInDays);
    }

    public void resume(LocalDate today) {
        if (statusOn(today) == MembershipStatus.ACTIVE) {
            throw new ActiveMembershipCannotBeResumedException("Membership is already active. Cannot be resumed");
        }

        if (statusOn(today) == MembershipStatus.PAUSED) {
            pausePeriod = null;
        }


    }

    public MembershipStatus statusOn(LocalDate date) {
        if (pausePeriod != null && pausePeriod.includes(date)) {
            return MembershipStatus.PAUSED;
        }
        return MembershipStatus.ACTIVE;
    }

    public MembershipId id() {
        return id;
    }

    public String memberName() {
        return memberName;
    }

    public EmailAddress emailAddress() {
        return emailAddress;
    }

    public PlanCode planCode() {
        return planCode;
    }

    public LocalDate activatedOn() {
        return activatedOn;
    }

    public PausePeriod pausePeriod() {
        return pausePeriod;
    }

    private static String requireMemberName(String memberName) {
        if (memberName == null || memberName.isBlank()) {
            throw new MembershipDomainException("Member name is required.");
        }
        return memberName.trim();
    }
}
