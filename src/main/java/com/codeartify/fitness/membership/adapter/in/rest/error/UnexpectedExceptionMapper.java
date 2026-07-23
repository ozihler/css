package com.codeartify.fitness.membership.adapter.in.rest.error;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnexpectedExceptionMapper implements ExceptionMapper<Throwable> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        return ProblemResponses.response(
                Response.Status.INTERNAL_SERVER_ERROR,
                "unexpected-failure",
                "Unexpected failure",
                "The request could not be completed.",
                uriInfo);
    }
}
