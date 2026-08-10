package com.codeartify.fitness.membership.adapter.in.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SignUpMembershipRequest")
public record SignUpMembershipRequest(
        @NotBlank @Size(max = 200) String memberName,
        @NotBlank @Email @Size(max = 320) String email,
        @NotBlank @Size(max = 32) String planCode) {
}
