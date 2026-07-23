package com.codeartify.fitness.membership.adapter.in.rest.response;

import com.codeartify.fitness.membership.application.port.in.MembershipPage;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "MembershipPage")
public record MembershipPageResponse(
        int page,
        int size,
        long totalElements,
        List<MembershipResponse> memberships) {
    public static MembershipPageResponse from(MembershipPage page, LocalDate businessDate) {
        return new MembershipPageResponse(
                page.page(),
                page.size(),
                page.totalElements(),
                page.memberships().stream()
                        .map(membership -> MembershipResponse.from(membership, businessDate))
                        .toList());
    }
}
