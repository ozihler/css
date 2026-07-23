package com.codeartify.fitness.membership.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class PageSizePolicy {
    private final int defaultPageSize;
    private final int maximumPageSize;

    @Inject
    public PageSizePolicy(
            @ConfigProperty(name = "fitness.admin.default-page-size", defaultValue = "20") int defaultPageSize,
            @ConfigProperty(name = "fitness.admin.maximum-page-size", defaultValue = "100") int maximumPageSize) {
        this.defaultPageSize = defaultPageSize;
        this.maximumPageSize = maximumPageSize;
    }

    public PageSizePolicy() {
        this(20, 100);
    }

    public int resolve(Integer requestedSize) {
        int resolvedSize = requestedSize == null ? defaultPageSize : requestedSize;
        if (resolvedSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
        return Math.min(resolvedSize, maximumPageSize);
    }
}
