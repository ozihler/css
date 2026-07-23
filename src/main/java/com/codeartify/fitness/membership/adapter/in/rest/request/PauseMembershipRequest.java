package com.codeartify.fitness.membership.adapter.in.rest.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PauseMembershipRequest")
public record PauseMembershipRequest(
        int durationInDays) {
}
