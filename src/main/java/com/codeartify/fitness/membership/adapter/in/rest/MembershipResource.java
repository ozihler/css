package com.codeartify.fitness.membership.adapter.in.rest;

import com.codeartify.fitness.membership.adapter.in.rest.error.ProblemResponse;
import com.codeartify.fitness.membership.adapter.in.rest.request.PauseMembershipRequest;
import com.codeartify.fitness.membership.adapter.in.rest.request.SignUpMembershipRequest;
import com.codeartify.fitness.membership.adapter.in.rest.response.MembershipResponse;
import com.codeartify.fitness.membership.application.port.in.GetMembership;
import com.codeartify.fitness.membership.application.port.in.PauseMembership;
import com.codeartify.fitness.membership.application.port.in.PauseMembershipCommand;
import com.codeartify.fitness.membership.application.port.in.SignUpMembership;
import com.codeartify.fitness.membership.application.port.in.SignUpMembershipCommand;
import com.codeartify.fitness.membership.domain.Membership;
import com.codeartify.fitness.membership.domain.MembershipId;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Clock;
import java.time.LocalDate;

@Path("/memberships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Memberships")
public class MembershipResource {
    private final SignUpMembership signUpMembership;
    private final PauseMembership pauseMembership;
    private final GetMembership getMembership;
    private final Clock clock;

    @Inject
    public MembershipResource(
            SignUpMembership signUpMembership,
            PauseMembership pauseMembership,
            GetMembership getMembership,
            Clock clock) {
        this.signUpMembership = signUpMembership;
        this.pauseMembership = pauseMembership;
        this.getMembership = getMembership;
        this.clock = clock;
    }

    @POST
    @Operation(summary = "Sign up a membership")
    @APIResponse(responseCode = "201", description = "Membership created",
            content = @Content(schema = @Schema(implementation = MembershipResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid request",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    @APIResponse(responseCode = "409", description = "Duplicate email",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    public Response signUp(@Valid @NotNull SignUpMembershipRequest request, @Context UriInfo uriInfo) {
        Membership membership = signUpMembership.signUp(new SignUpMembershipCommand(
                request.memberName(),
                request.email(),
                request.planCode()));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(membership.id().value()).build())
                .entity(MembershipResponse.from(membership, today()))
                .build();
    }

    @POST
    @Path("/{membershipId}/pause")
    @Operation(summary = "Pause a membership")
    @APIResponse(responseCode = "200", description = "Membership paused",
            content = @Content(schema = @Schema(implementation = MembershipResponse.class)))
    @APIResponse(responseCode = "404", description = "Membership not found",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    @APIResponse(responseCode = "409", description = "Membership already paused",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    @APIResponse(responseCode = "422", description = "Invalid pause duration",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    public MembershipResponse pause(
            @PathParam("membershipId") String membershipId,
            @Valid @NotNull PauseMembershipRequest request) {
        Membership membership = pauseMembership.pause(
                new MembershipId(membershipId),
                new PauseMembershipCommand(request.durationInDays()));
        return MembershipResponse.from(membership, today());
    }

    @GET
    @Path("/{membershipId}")
    @Operation(summary = "View one membership")
    @APIResponse(responseCode = "200", description = "Membership found",
            content = @Content(schema = @Schema(implementation = MembershipResponse.class)))
    @APIResponse(responseCode = "404", description = "Membership not found",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    public MembershipResponse get(@PathParam("membershipId") String membershipId) {
        return MembershipResponse.from(getMembership.get(new MembershipId(membershipId)), today());
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}
