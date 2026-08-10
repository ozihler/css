package com.codeartify.fitness.membership.adapter.in.rest.error;

import com.codeartify.fitness.membership.application.MembershipNotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MembershipNotFoundExceptionMapper implements ExceptionMapper<MembershipNotFoundException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(MembershipNotFoundException exception) {
        return ProblemResponses.response(
                Response.Status.NOT_FOUND,
                "membership-not-found",
                "Membership not found",
                exception.getMessage(),
                uriInfo);
    }
}
