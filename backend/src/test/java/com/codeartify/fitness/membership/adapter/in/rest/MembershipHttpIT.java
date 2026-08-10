package com.codeartify.fitness.membership.adapter.in.rest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Paths;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class MembershipHttpIT {
    private static final Network NETWORK = Network.newNetwork();
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:18-alpine"))
            .withNetwork(NETWORK)
            .withNetworkAliases("postgres")
            .withDatabaseName("fitness")
            .withUsername("fitness")
            .withPassword("fitness");
    private static final GenericContainer<?> APP = new GenericContainer<>(
            new ImageFromDockerfile("fitness-memberships-it:latest", false)
                    .withFileFromPath(".", Paths.get(".").toAbsolutePath().normalize()))
            .withNetwork(NETWORK)
            .withEnv("database.host", "postgres")
            .withEnv("database.port", "5432")
            .withEnv("database.name", "fitness")
            .withEnv("database.user", "fitness")
            .withEnv("database.password", "fitness")
            .withExposedPorts(9080)
            .waitingFor(Wait.forHttp("/health/ready")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(3)));

    @BeforeAll
    static void startApplication() {
        POSTGRES.start();
        APP.start();
        RestAssured.baseURI = "http://" + APP.getHost();
        RestAssured.port = APP.getMappedPort(9080);
    }

    @AfterAll
    static void stopApplication() {
        APP.stop();
        POSTGRES.stop();
        NETWORK.close();
        RestAssured.reset();
    }

    @Test
    void signs_up_views_pauses_lists_and_resumes_memberships_over_http() {
        String emailAddress = "jane.http@example.com";

        String membershipId = given()
                .contentType("application/json")
                .body("""
                        {
                          "memberName": "Jane Doe",
                          "email": "%s",
                          "planCode": "STANDARD"
                        }
                        """.formatted(emailAddress))
                .when()
                .post("/api/memberships")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("memberName", equalTo("Jane Doe"))
                .body("email", equalTo(emailAddress))
                .body("planCode", equalTo("STANDARD"))
                .body("status", equalTo("ACTIVE"))
                .body("pausedFrom", equalTo(null))
                .body("resumeOn", equalTo(null))
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/memberships/{membershipId}", membershipId)
                .then()
                .statusCode(200)
                .body("id", equalTo(membershipId));

        given()
                .contentType("application/json")
                .body("""
                        {
                          "durationInDays": 30
                        }
                        """)
                .when()
                .post("/api/memberships/{membershipId}/pause", membershipId)
                .then()
                .statusCode(200)
                .body("id", equalTo(membershipId))
                .body("status", equalTo("PAUSED"))
                .body("pausedFrom", notNullValue())
                .body("resumeOn", notNullValue());

        given()
                .queryParam("page", 0)
                .queryParam("size", 20)
                .queryParam("status", "PAUSED")
                .when()
                .get("/api/admin/memberships")
                .then()
                .statusCode(200)
                .body("page", equalTo(0))
                .body("size", equalTo(20))
                .body("totalElements", equalTo(1))
                .body("memberships[0].id", equalTo(membershipId));

        given()
                .contentType("application/json")
                .when()
                .post("/api/memberships/{membershipId}/resume", membershipId)
                .then()
                .statusCode(200)
                .body("id", equalTo(membershipId))
                .body("status", equalTo("ACTIVE"))
                .body("pausedFrom", equalTo(null))
                .body("resumeOn", equalTo(null));
    }
}
