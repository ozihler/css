package com.codeartify.fitness.membership.adapter.in.rest.error;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Problem")
public record ProblemResponse(
        String type,
        String title,
        int status,
        String detail,
        String instance) {
}
