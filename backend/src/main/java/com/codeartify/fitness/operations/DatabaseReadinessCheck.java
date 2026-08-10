package com.codeartify.fitness.operations;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.SQLException;

@Readiness
@ApplicationScoped
public class DatabaseReadinessCheck implements HealthCheck {
    @Resource(lookup = "jdbc/fitness")
    private DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("select 1")) {
            statement.execute();
            return HealthCheckResponse.up("postgresql");
        } catch (SQLException exception) {
            return HealthCheckResponse.down("postgresql");
        }
    }
}
