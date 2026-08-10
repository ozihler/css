package com.codeartify.fitness.membership.application.port.in;

public record SignUpMembershipCommand(String memberName, String email, String planCode) {
}
