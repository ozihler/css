package com.codeartify.fitness.membership.adapter.in.rest.error;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return ProblemResponses.response(
                Response.Status.BAD_REQUEST,
                "invalid-request",
                "Invalid request",
                exception.getMessage(),
                uriInfo);
    }
}
