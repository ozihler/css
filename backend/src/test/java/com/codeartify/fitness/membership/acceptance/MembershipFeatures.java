package com.codeartify.fitness.membership.acceptance;

import io.cucumber.core.cli.Main;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipFeatures {
    @Test
    void runs_business_facing_membership_scenarios() {
        byte exitStatus = Main.run(new String[]{
                "classpath:features",
                "--glue", "com.codeartify.fitness.membership.acceptance",
                "--plugin", "summary"
        }, Thread.currentThread().getContextClassLoader());

        assertThat(exitStatus).isZero();
    }
}
