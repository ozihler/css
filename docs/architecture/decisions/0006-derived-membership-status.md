# 0006 Derived Membership Status

## Status

Accepted

## Decision

Do not store a mutable status column. Derive effective status from pause dates and the supplied business date.

## Consequences

The database cannot contain a stale status. Queries must express the same date logic used by the aggregate.
