# 0002 Hexagonal Architecture

## Status

Accepted

## Decision

Use inbound and outbound ports around the membership application layer. Keep domain code free of Jakarta, REST, JSON, and persistence concerns.

## Consequences

Business behaviour is testable without infrastructure. Adapters contain translation and technology-specific code.
