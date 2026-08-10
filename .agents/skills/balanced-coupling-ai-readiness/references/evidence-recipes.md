# Evidence-Gathering Recipes

Use these mechanics selectively. The goal is not to collect every possible metric; it is to gather enough independent evidence to explain how a representative change propagates.

## 1. Establish the Unit of Analysis

Before running tools, write down:

- the business change being traced;
- the component level being evaluated;
- the expected upstream and downstream;
- the Git time window;
- repositories and systems included;
- known exclusions.

Do not mix method-level distance with service-level distance in one score.

### Name Concrete Endpoints

Before scoring, resolve generic component names to the narrowest stable endpoint supported by evidence:

- `file::class.method()` or `file::field`;
- API route plus request/response field;
- JSON/YAML path;
- event type plus field;
- table plus column;
- command or build target;
- runtime or vendor endpoint.

Record whether the relationship direction is a runtime call, data flow, contract consumption, ownership/authority, or representative change propagation. A co-change relationship must not be presented as a runtime dependency.

For duplicated representations with unclear authority, create a shared-concept record and list both representations. Use a directed record only for an observed edit sequence, data flow, or explicitly stated change scenario.

## 2. Static Structure

Inspect:

- source roots, packages, namespaces, and modules;
- build descriptors and dependency declarations;
- public APIs, event schemas, commands, DTOs, and shared libraries;
- persistence entities and migration ownership;
- generated code and shared configuration;
- deployment descriptors and infrastructure modules.

Useful searches:

```bash
# Build and module boundaries
find . -maxdepth 4 \( -name 'pom.xml' -o -name 'build.gradle*' -o -name 'settings.gradle*' -o -name 'package.json' -o -name 'pyproject.toml' \)

# API and event definitions; adapt terms to the stack
rg -n "record .*Event|class .*Event|interface .*Api|@RestController|@Path|openapi|asyncapi|protobuf|avro"

# Persistence leakage and direct cross-boundary access
rg -n "EntityManager|JdbcTemplate|SELECT |INSERT |UPDATE |DELETE |repository|tableName"
```

Interpretation rules:

- line count, import count, number of routes, or centrality does not prove coupling; first identify shared knowledge and a change-propagation mechanism;
- duplicated IDs, labels, titles, routes, dates, configuration, or locale text does not prove symmetric functional coupling; determine whether behaviour is independently implemented;
- an interface does not prove contract coupling;
- a DTO that mirrors an internal aggregate may still be model coupling;
- a queue does not prove low knowledge coupling;
- directory separation does not prove lifecycle independence.

## 3. Change History and Co-Change

Start with representative history:

```bash
# Changes for a component or feature
 git log --date=short --stat -- path/to/component

# Follow a file through renames
 git log --follow -- path/to/file

# Find commits that added or removed a rule, field, or concept
 git log -S'membershipStatus' --all -- path/to/repository
 git log -G'pause.*fee|grace.*period' --all -- path/to/repository

# Reverts and emergency fixes
 git log --all --regexp-ignore-case --grep='revert\|rollback\|hotfix'

# Files changed together in the selected window
 git log --since='6 months ago' --name-only --format='---'
```

For multi-repository changes, use ticket IDs, issue links, release notes, or commit-message conventions to correlate changes.

### Co-Change Procedure

1. Group changed files by commit or delivery ticket.
2. Map each file to a component.
3. Count component pairs that appear in the same change.
4. Inspect representative high-frequency pairs manually.
5. Exclude or label mechanical changes:
   - formatting;
   - mass renames;
   - dependency upgrades;
   - generated code;
   - repository-wide license or configuration updates.
6. Compare historical co-change with declared architecture boundaries.
7. Look for coordinated releases, parallel changes, compatibility code, and recurring integration defects.

Co-change is evidence of possible coupling, not proof. Explain the business or technical mechanism behind the pattern.

## 4. Churn and Volatility

Repository churn is corroborating evidence, not the volatility definition.

```bash
# Commit frequency by file in a time window
 git log --since='12 months ago' --name-only --format='' \
   | sed '/^$/d' | sort | uniq -c | sort -nr | head -50

# Recent changes to a bounded area
 git log --since='6 months ago' --oneline -- path/to/core-domain

# Inspect the actual reasons for representative changes
 git log --since='6 months ago' --format='%h %ad %s' --date=short -- path/to/area
```

Assess four evidence layers separately:

1. **Problem/business volatility — primary**
   - changing business rules, policies, eligibility, pricing, schedules, regulation, experiments, or differentiation;
   - roadmap items or owner testimony showing expected adaptation.
2. **Upstream/inferred volatility**
   - the endpoint changes because a strongly coupled upstream concept changes.
3. **Solution volatility**
   - framework, infrastructure, persistence, deployment, implementation, or team changes.
4. **Churn — corroborative**
   - commit frequency, defects, reverts, co-change, and coordinated releases.

For representative high-churn files, classify commits before using them as evidence:

- business-rule change;
- data/content update;
- defect correction;
- compatibility/migration;
- generated or mechanical change;
- dependency/tooling update;
- formatting/rename.

Exclude or explicitly label mechanical and content-only changes. Ninety copy or date edits do not automatically establish V=9. Conversely, low historical churn does not prove low volatility when roadmap or regulatory pressure is high.

Every volatility conclusion should state:

- knowledge being scored;
- business/problem evidence;
- solution or upstream evidence;
- churn time window and representative commits;
- excluded mechanical changes;
- lower and higher rejected anchors;
- missing roadmap/domain/ownership evidence.

Use a score range when business context is unavailable. Never map a commit count directly to V.

## 5. Lifecycle and Delivery Evidence

Inspect:

- build pipelines;
- test stages;
- artifact versioning;
- deployment manifests;
- release notes;
- rollback procedures;
- ownership files such as `CODEOWNERS`;
- coordinated deployment runbooks;
- cross-team approval requirements.

Questions:

- Can either component be built and tested independently?
- Can either be deployed without the other?
- Is backward compatibility required?
- Can one side roll back independently?
- Who is paged when the interaction fails?
- Does separate deployment exist in name only?

## 6. Runtime and Data Evidence

Inspect:

- distributed traces;
- logs and correlation IDs;
- service dependency maps;
- latency, timeout, retry, and circuit-breaker settings;
- message lag and dead-letter queues;
- database ownership and write paths;
- ordering, idempotency, and consistency rules;
- cache rebuild and reconciliation procedures.

For a synchronous chain, trace what happens when each downstream is slow or unavailable.

For asynchronous integration, inspect both:

- runtime decoupling: availability and timing;
- knowledge coupling: model, meaning, ordering, algorithms, and compatibility.

## 7. Tests and Safe Experimentation

Record test evidence using the exact state: `discovered`, `compiled`, `executed`, `passed`, `failed`, `skipped`, `blocked`, or `not run`.

Examples:

```bash
# Discovery only; does not execute Playwright tests
npx playwright test --list

# Execute the tests
npx playwright test

# Maven summary: inspect run, failure, error, and skipped counts
./mvnw test

# Gradle: inspect skipped/up-to-date tasks and test reports
./gradlew test
```

Interpretation rules:

- a listed test is discovered, not executed;
- a compiled application is buildable, not behaviourally verified;
- skipped Testcontainers tests leave the database/API integration boundary unverified;
- a passing unit suite does not verify a frontend–backend workflow unless it executes that contract;
- state which specific relationship each test verifies.

Look for:

- focused unit tests for shared rules;
- characterization tests around legacy behaviour;
- consumer/provider contract tests;
- integration tests at ownership boundaries;
- migration and compatibility tests;
- rollback verification;
- observability that can confirm or falsify assumptions.

Do not count test quantity. Determine whether a representative cross-boundary change can be verified locally and safely. Report counts as `executed / passed / failed / skipped / blocked`, and never describe the whole verification step as passed when critical boundary suites were skipped or blocked.

## 8. Domain and Ownership Interviews

When repository evidence is insufficient, ask concrete questions:

- Which business changes forced these components to change together recently?
- Which side owns the meaning of each field or event?
- Which rules are deliberately duplicated?
- Which release combinations are supported?
- What breaks when one side changes alone?
- Which behaviour is understood only by one expert?
- Which areas are intentionally frozen?
- Which roadmap changes are expected in the next 6–12 months?

Record interview claims as observed testimony, not as code evidence. Confirm critical claims with another source where possible.

## 9. AI Context Evidence

Inspect:

- repository and module agent instructions;
- build and test commands;
- architecture decisions;
- examples explicitly marked as preferred;
- legacy or compatibility code marked as unsafe to copy;
- stop-and-ask rules;
- scoped commands for fast verification;
- ownership and expert contacts.

A codebase can be structurally reasonable but still difficult for agents when these signals are absent or contradictory.

## 10. Scoped Absence Statements

Before writing `no Red areas`, `no cross-feature dependencies`, `no unknown consumers`, or similar claims:

1. list the inspected evidence categories;
2. list unavailable runtime, incident, ownership, delivery, and executed-boundary evidence;
3. state the result as `not observed in the inspected scope`;
4. identify the smallest evidence source that could falsify the conclusion.

Static inspection can support `no Red hotspot observed statically`; it cannot prove that production runtime or incident conditions are non-chaotic.

## 11. Minimal Evidence Sets by Assessment Mode

### Rapid Scan

Minimum:

- static component map;
- one representative feature trace;
- recent history for that feature;
- build/deployment boundaries;
- representative tests;
- explicit unknowns.

### Feature-Centred

Minimum:

- end-to-end change trace;
- all touched components and owners;
- relevant co-change history;
- contracts and consistency assumptions;
- verification path;
- likely agent failure mode.

### Full Assessment

Add:

- multi-feature sampling;
- incident and revert evidence;
- roadmap volatility;
- runtime traces;
- team and vendor boundaries;
- aggregate hotspot distribution.
