package com.codeartify.fitness.membership.adapter.in.rest.error;

import com.codeartify.fitness.membership.domain.MembershipAlreadyPausedException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MembershipAlreadyPausedExceptionMapper implements ExceptionMapper<MembershipAlreadyPausedException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(MembershipAlreadyPausedException exception) {
        return ProblemResponses.response(
                Response.Status.CONFLICT,
                "membership-already-paused",
                "Membership already paused",
                exception.getMessage(),
                uriInfo);
    }
}
