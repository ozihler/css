package com.codeartify.fitness.membership.domain;

import java.time.LocalDate;

public record PausePeriod(LocalDate pausedFrom, LocalDate resumeOn) {
    public static final int MINIMUM_DAYS = 30;
    public static final int MAXIMUM_DAYS = 60;

    public PausePeriod {
        if (pausedFrom == null) {
            throw new MembershipDomainException("Pause start date is required.");
        }
        if (resumeOn == null) {
            throw new MembershipDomainException("Resume date is required.");
        }
        if (!resumeOn.isAfter(pausedFrom)) {
            throw new MembershipDomainException("Resume date must be after the pause start date.");
        }
    }

    public static PausePeriod startingOn(LocalDate pausedFrom, int durationInDays) {
        if (durationInDays < MINIMUM_DAYS || durationInDays > MAXIMUM_DAYS) {
            throw new InvalidPauseDurationException(
                    "Pause duration must be between 30 and 60 days inclusive.");
        }
        return new PausePeriod(pausedFrom, pausedFrom.plusDays(durationInDays));
    }

    public boolean includes(LocalDate date) {
        return !date.isBefore(pausedFrom) && date.isBefore(resumeOn);
    }
}
