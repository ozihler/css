package com.codeartify.fitness.membership.adapter.in.rest;

import com.codeartify.fitness.membership.adapter.in.rest.error.ProblemResponse;
import com.codeartify.fitness.membership.adapter.in.rest.response.MembershipPageResponse;
import com.codeartify.fitness.membership.application.port.in.FindMemberships;
import com.codeartify.fitness.membership.application.port.in.MembershipSearch;
import com.codeartify.fitness.membership.domain.MembershipStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Clock;
import java.time.LocalDate;

@Path("/admin/memberships")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Administration")
public class AdminMembershipResource {
    private final FindMemberships findMemberships;
    private final Clock clock;

    @Inject
    public AdminMembershipResource(FindMemberships findMemberships, Clock clock) {
        this.findMemberships = findMemberships;
        this.clock = clock;
    }

    @GET
    @Operation(summary = "View all memberships for administration")
    @APIResponse(responseCode = "200", description = "Membership page",
            content = @Content(schema = @Schema(implementation = MembershipPageResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid page request",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemResponse.class)))
    public MembershipPageResponse find(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") Integer size,
            @QueryParam("status") String status) {
        return MembershipPageResponse.from(
                findMemberships.find(new MembershipSearch(page, size, parseStatus(status))),
                LocalDate.now(clock));
    }

    private static MembershipStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return MembershipStatus.valueOf(status);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Status must be ACTIVE or PAUSED.");
        }
    }
}
