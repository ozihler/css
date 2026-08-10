package com.codeartify.fitness.membership.adapter.in.rest.error;

import com.codeartify.fitness.membership.domain.InvalidPauseDurationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidPauseDurationExceptionMapper implements ExceptionMapper<InvalidPauseDurationException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(InvalidPauseDurationException exception) {
        return ProblemResponses.response(
                422,
                "invalid-pause-duration",
                "Invalid pause duration",
                exception.getMessage(),
                uriInfo);
    }
}
