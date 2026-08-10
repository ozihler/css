# 0005 Separate Domain and Persistence Models

## Status

Accepted

## Decision

Keep `Membership` as a behaviour-rich domain object and map it to `MembershipJpaEntity` in the persistence adapter.

## Consequences

JPA annotations do not leak into the domain. Mapping adds a little code but keeps dependency direction inward.
