package com.codeartify.fitness.membership.adapter.in.rest.error;

import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonbExceptionMapper implements ExceptionMapper<JsonbException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(JsonbException exception) {
        return ProblemResponses.response(
                Response.Status.BAD_REQUEST,
                "invalid-json",
                "Invalid JSON",
                "The request body could not be parsed.",
                uriInfo);
    }
}
