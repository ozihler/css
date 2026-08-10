package com.codeartify.fitness.membership.adapter.in.rest.error;

import com.codeartify.fitness.membership.application.DuplicateEmailAddressException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateEmailAddressExceptionMapper implements ExceptionMapper<DuplicateEmailAddressException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(DuplicateEmailAddressException exception) {
        return ProblemResponses.response(
                Response.Status.CONFLICT,
                "duplicate-email-address",
                "Email address already registered",
                exception.getMessage(),
                uriInfo);
    }
}
