# 0007 Authentication Deferred

## Status

Accepted

## Decision

Do not implement authentication or fake header-based authorization in the first milestone. Use MicroProfile JWT when real admin authentication is added.

## Consequences

The admin endpoint is unauthenticated in this milestone by explicit scope decision.
