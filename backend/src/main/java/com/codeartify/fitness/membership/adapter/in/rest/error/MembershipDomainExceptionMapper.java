package com.codeartify.fitness.membership.adapter.in.rest.error;

import com.codeartify.fitness.membership.domain.MembershipDomainException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MembershipDomainExceptionMapper implements ExceptionMapper<MembershipDomainException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public jakarta.ws.rs.core.Response toResponse(MembershipDomainException exception) {
        return ProblemResponses.response(
                422,
                "membership-rule-violated",
                "Membership rule violated",
                exception.getMessage(),
                uriInfo);
    }
}
