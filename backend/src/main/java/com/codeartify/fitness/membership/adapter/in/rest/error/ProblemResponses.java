package com.codeartify.fitness.membership.adapter.in.rest.error;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

final class ProblemResponses {
    static final String PROBLEM_JSON = "application/problem+json";
    private static final String PROBLEM_BASE = "https://fitness.example/problems/";

    private ProblemResponses() {
    }

    static Response response(Response.Status status, String type, String title, String detail, UriInfo uriInfo) {
        return Response.status(status)
                .type(PROBLEM_JSON)
                .entity(new ProblemResponse(
                        PROBLEM_BASE + type,
                        title,
                        status.getStatusCode(),
                        detail,
                        uriInfo == null ? null : uriInfo.getRequestUri().getPath()))
                .build();
    }

    static Response response(int status, String type, String title, String detail, UriInfo uriInfo) {
        return Response.status(status)
                .type(MediaType.valueOf(PROBLEM_JSON))
                .entity(new ProblemResponse(
                        PROBLEM_BASE + type,
                        title,
                        status,
                        detail,
                        uriInfo == null ? null : uriInfo.getRequestUri().getPath()))
                .build();
    }
}
