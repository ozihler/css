package com.codeartify.fitness.membership.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDate;

@Entity
@Table(name = "memberships")
public class MembershipJpaEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "normalized_email", nullable = false, unique = true, length = 320)
    private String normalizedEmail;

    @Column(name = "member_name", nullable = false, length = 200)
    private String memberName;

    @Column(name = "plan_code", nullable = false, length = 32)
    private String planCode;

    @Column(name = "activated_on", nullable = false)
    private LocalDate activatedOn;

    @Column(name = "paused_from")
    private LocalDate pausedFrom;

    @Column(name = "resume_on")
    private LocalDate resumeOn;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    protected MembershipJpaEntity() {
    }

    MembershipJpaEntity(
            String id,
            String normalizedEmail,
            String memberName,
            String planCode,
            LocalDate activatedOn,
            LocalDate pausedFrom,
            LocalDate resumeOn) {
        this.id = id;
        this.normalizedEmail = normalizedEmail;
        this.memberName = memberName;
        this.planCode = planCode;
        this.activatedOn = activatedOn;
        this.pausedFrom = pausedFrom;
        this.resumeOn = resumeOn;
    }

    String id() {
        return id;
    }

    String normalizedEmail() {
        return normalizedEmail;
    }

    String memberName() {
        return memberName;
    }

    String planCode() {
        return planCode;
    }

    LocalDate activatedOn() {
        return activatedOn;
    }

    LocalDate pausedFrom() {
        return pausedFrom;
    }

    LocalDate resumeOn() {
        return resumeOn;
    }

    void updateFrom(MembershipJpaEntity source) {
        this.normalizedEmail = source.normalizedEmail;
        this.memberName = source.memberName;
        this.planCode = source.planCode;
        this.activatedOn = source.activatedOn;
        this.pausedFrom = source.pausedFrom;
        this.resumeOn = source.resumeOn;
    }
}
