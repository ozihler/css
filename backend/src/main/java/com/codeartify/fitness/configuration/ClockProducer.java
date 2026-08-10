package com.codeartify.fitness.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.Clock;

@ApplicationScoped
public class ClockProducer {
    @Produces
    @ApplicationScoped
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
