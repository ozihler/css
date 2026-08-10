package com.codeartify.fitness.membership.acceptance;

import com.codeartify.fitness.membership.application.InMemoryMembershipRepository;
import com.codeartify.fitness.membership.application.MembershipQueryService;
import com.codeartify.fitness.membership.application.PageSizePolicy;
import com.codeartify.fitness.membership.application.PauseMembershipService;
import com.codeartify.fitness.membership.application.SignUpMembershipService;
import com.codeartify.fitness.membership.application.port.in.PauseMembershipCommand;
import com.codeartify.fitness.membership.application.port.in.SignUpMembershipCommand;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipSteps {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-08-01T08:00:00Z"), ZoneOffset.UTC);

    private InMemoryMembershipRepository repository;
    private SignUpMembershipService signUpMembershipService;
    private PauseMembershipService pauseMembershipService;
    private MembershipQueryService membershipQueryService;
    private Membership membership;

    @Before
    public void resetScenario() {
        repository = new InMemoryMembershipRepository();
        signUpMembershipService = new SignUpMembershipService(repository, CLOCK);
        pauseMembershipService = new PauseMembershipService(repository, CLOCK);
        membershipQueryService = new MembershipQueryService(repository, new PageSizePolicy(20, 100), CLOCK);
    }

    @Given("no membership exists for {string}")
    public void noMembershipExistsFor(String emailAddress) {
        assertThat(repository.findByEmailAddress(new com.codeartify.fitness.membership.domain.EmailAddress(emailAddress)))
                .isEmpty();
    }

    @When("Jane signs up for the STANDARD plan")
    public void janeSignsUpForTheStandardPlan() {
        membership = signUpMembershipService.signUp(
                new SignUpMembershipCommand("Jane Doe", "jane@example.com", "STANDARD"));
    }

    @Then("the membership is active")
    public void theMembershipIsActive() {
        assertThat(membership.statusOn(LocalDate.now(CLOCK))).isEqualTo(MembershipStatus.ACTIVE);
    }

    @Then("it can be viewed by its membership ID")
    public void itCanBeViewedByItsMembershipId() {
        assertThat(membershipQueryService.get(membership.id()).id()).isEqualTo(membership.id());
    }

    @Given("Jane has an active membership")
    public void janeHasAnActiveMembership() {
        janeSignsUpForTheStandardPlan();
        theMembershipIsActive();
    }

    @When("the membership is paused for {int} days")
    public void theMembershipIsPausedForDays(int durationInDays) {
        membership = pauseMembershipService.pause(membership.id(), new PauseMembershipCommand(durationInDays));
    }

    @Then("it is paused today")
    public void itIsPausedToday() {
        assertThat(membership.statusOn(LocalDate.now(CLOCK))).isEqualTo(MembershipStatus.PAUSED);
    }

    @Then("it becomes active on the resume date")
    public void itBecomesActiveOnTheResumeDate() {
        assertThat(membership.statusOn(membership.pausePeriod().resumeOn())).isEqualTo(MembershipStatus.ACTIVE);
    }
}
