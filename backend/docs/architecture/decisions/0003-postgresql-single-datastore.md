# 0003 PostgreSQL as Single Datastore

## Status

Accepted

## Decision

Use PostgreSQL for development, tests, and Docker operation. Manage schema changes with Flyway.

## Consequences

Integration tests exercise the same relational behaviour used locally, including uniqueness constraints and locking.
