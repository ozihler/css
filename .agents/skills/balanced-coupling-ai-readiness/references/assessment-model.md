# Detailed Assessment Model

This reference contains the detailed scoring anchors and recommendation catalogue used by the main skill.

The integration-strength, distance, volatility, modularity, and balance model is based on Vlad Khononov's *Balancing Coupling in Software Design*. The AI-readiness dimensions and autonomy mapping are skill-specific extensions.

## Numeric Discipline

The qualitative diagnosis always precedes numeric scoring.

For each score:

- name the anchor;
- cite observed evidence;
- mark inferred and unknown context;
- reject the nearest plausible lower and higher scores;
- use whole numbers;
- use a range rather than a fabricated midpoint when evidence crosses an anchor;
- avoid a single balance score when the inputs are low confidence;
- perform a sensitivity check if a one- or two-point difference could change priority or autonomy;
- calculate exact and ranged formulas with `scripts/calculate_coupling_scores.py` or an equivalent calculator;
- enumerate every integer combination for ranged inputs instead of estimating results mentally.

Numbers support the diagnosis; they never replace it.

## Evidence Status

- **Observed:** directly supported by code, history, tests, manifests, telemetry, or decisions.
- **Inferred:** supported indirectly by observed evidence.
- **Unknown:** requires additional evidence or an experiment.


## Relationship Decomposition

Score exactly one directed upstream–downstream relationship at one perspective per record.

A workflow such as:

```text
Angular page → backend API → application service → database → email provider
```

is not one relationship and must not receive one S/D/V tuple. Create separate records for every material connection, for example:

1. Angular page → backend API contract;
2. backend controller → application policy;
3. application service → owned database schema;
4. application service → email template or provider;
5. browser and backend → shared cookie or redirect convention.

A workflow may have:

- a shared workflow identifier;
- an aggregate blast-radius description;
- one conservative autonomy colour governed by its most restrictive material relationship.

It may not have a combined integration-strength, distance, volatility, modularity, or balance score. This prevents a weak API contract, intrusive database access, and vendor dependency from being averaged into one ambiguous number.

## Step 4: Classify Integration Strength

Integration strength describes how much upstream knowledge crosses the boundary and how likely an upstream change is to propagate.

Use the book's 1–10 anchors. Intermediate values are allowed only when evidence clearly places the interaction between anchors.

### 1 — Contract Coupling

The upstream exposes an integration-specific contract that shares only the knowledge needed by the consumer.

Typical evidence:

- consumer-specific API DTO;
- explicit event contract;
- narrow method signature;
- stable port;
- translation at the boundary;
- hidden implementation model.

Contract coupling is usually preferable across high distance, but it is not always justified at low distance.

### 3 — Model Coupling

The upstream exposes a domain or implementation model that contains more knowledge than the consumer needs.

Typical evidence:

- persistence entity crosses a boundary;
- internal aggregate serialized as an event;
- shared domain model package;
- API payload mirrors the producer's internal data model;
- consumers depend on fields unrelated to their use case.

### 8 — Functional Coupling

Components share knowledge of a behaviour, workflow, business rule, transaction, ordering requirement, or operational procedure.

Typical evidence:

- caller controls the callee's internal flow;
- components must execute in a specific sequence;
- two operations must succeed or fail together;
- consumers know internal process stages;
- duplicated implementation of a changing business rule;
- timing or value relationships across boundaries.

### 9 — Symmetric Functional Coupling

Multiple components independently implement the same business functionality and must remain behaviourally equivalent.

Typical evidence:

- duplicated pricing, eligibility, validation, or authorization logic;
- the same rule implemented in multiple services;
- coordinated releases required to keep behaviour aligned.

### 10 — Intrusive Coupling

The downstream depends on private implementation details of the upstream.

Typical evidence:

- direct access to another component's database tables;
- reflection into private state;
- dependence on internal file layout;
- cross-module mutation of internals;
- shared global state;
- one service modifying another service's data;
- private schema or implementation details treated as public API.

### Strength Classification Rules

- Use the **strongest applicable level within the single directed relationship being scored**.
- Do not lower the rating because the interaction is asynchronous.
- Do not lower the rating because an interface exists; evaluate what knowledge the interface exposes.
- A shared database can be intrusive, model, functional, or a combination. Inspect actual knowledge and consistency assumptions.
- A broad event can be model-coupled even when published asynchronously.
- An interface that exposes SQL or persistence operations still shares storage knowledge.
- Duplicated IDs, titles, dates, labels, route fragments, locale text, configuration, or catalogue entries do **not** establish symmetric functional coupling by themselves. Classify the actual shared agreement: name, meaning, model, reference data, or contract.
- Use symmetric functional coupling only when two or more components independently execute behaviour and must remain behaviourally equivalent, such as pricing, eligibility, validation, authorization, state transition, or availability rules.
- A frontend interpreting an explicit backend status can remain contract coupling. It becomes functional coupling when the frontend must know workflow stages, sequencing, timing, or independently reproduce the backend decision rule.

### Frontend–Backend Calibration Examples

- The backend returns `registrationAllowed: true` and the frontend renders the result: usually contract coupling.
- The backend returns raw dates, capacity, and status while the frontend independently decides whether registration is open: functional coupling if that decision reproduces backend policy.
- Both backend and frontend independently calculate the same registration-eligibility rule and must stay equivalent: symmetric functional coupling.
- Both applications contain the same session ID, title, route slug, and translated label: reference-data, name, meaning, model, or contract coupling depending on use; not symmetric functional coupling without duplicated behaviour.
- The frontend depends on an undocumented cookie value or redirect query parameter: contract or meaning coupling unless it also reproduces the backend workflow.


## Step 5: Record Connascence or Degree

Use connascence to explain the degree of knowledge shared inside model, contract, or functional coupling.

From lower to higher shared agreement:

1. name;
2. type;
3. meaning;
4. algorithm;
5. position;
6. execution;
7. timing;
8. value;
9. identity.

Examples:

- magic numeric status shared across modules: meaning;
- both sides must use the same encryption algorithm: algorithm;
- tuple or array element order: position;
- operations must run in order: execution;
- consumer must wait a fixed time: timing;
- operations must succeed or fail together: value;
- components require the exact same shared state instance: identity.

Use the highest relevant level.

High connascence is not automatically wrong. It is evidence that the components may need to remain close.

## Step 6: Evaluate Distance

Distance represents the effort required to coordinate a cascading change.

Use these anchors:

| Score | Distance anchor |
|---:|---|
| 1 | Methods in the same object |
| 2 | Objects in the same namespace or package |
| 3–7 | Objects in different packages, modules, repositories, or ownership boundaries |
| 8 | Different libraries or independently versioned artifacts |
| 9 | Services in a distributed system |
| 10 | Systems implemented or controlled by different vendors |

Adjust within 3–7 using evidence from:

- separate build modules;
- separate repositories;
- separate teams;
- different release cadences;
- different languages or tooling;
- coordination overhead;
- access restrictions;
- independently deployed components.

Record these distance facets separately when useful:

- encapsulation distance;
- build distance;
- deployment distance;
- ownership distance;
- vendor distance;
- runtime distance.

The numeric distance should represent the highest material coordination cost for that one relationship, not merely directory depth. Same repository or same owner does not erase independently deployed artifact, runtime, or release distance; conversely, different files do not establish meaningful distance by themselves.

## Step 7: Evaluate Volatility

Volatility describes how often upstream knowledge changes.

Use these anchors:

| Score | Volatility anchor |
|---:|---|
| 1 | Frozen or rarely evolved legacy component |
| 3 | Supporting or generic subdomain with low-to-moderate change |
| 10 | Core subdomain or volatility inferred from strong coupling to core components |

Use intermediate values based on:

- source-control churn;
- roadmap pressure;
- defect frequency;
- regulatory change;
- experimentation;
- product differentiation;
- dependency on volatile upstreams;
- organisational restructuring;
- technology replacement.

Distinguish:

- **problem volatility**: business rules and domain knowledge change;
- **solution volatility**: frameworks, storage, infrastructure, implementation, or team structure change;
- **inferred volatility**: a component changes because strongly coupled upstream components change.

Do not assume that old code has low volatility if teams still frequently adapt it.

## Step 8: Calculate Coupling Indicators

Only calculate these indicators after stating the qualitative diagnosis and evidence. Use the numeric model as a heuristic.

Let:

- `S` = integration strength, 1–10;
- `D` = distance, 1–10;
- `V` = volatility, 1–10.

### Modularity

```text
MODULARITY = |S - D| + 1
```

A high value indicates that strength and distance compensate for each other:

- strong coupling is kept near;
- weak coupling supports greater distance.

A low value indicates local or global complexity:

- weak coupling at very low distance can create needless indirection and shallow modules;
- strong coupling at high distance creates expensive distributed entanglement.

### Coupling Balance

```text
BALANCE = max(|S - D|, 10 - V) + 1
```

Interpretation:

- high modularity increases balance;
- low volatility can compensate for an otherwise imperfect integration;
- high volatility exposes poor coupling choices.

Do not present this value as an objective measurement. Always show the qualitative diagnosis first, followed by the underlying evidence, dimensions, confidence, and sensitivity.


### Deterministic Calculation and Ranges

Run:

```bash
python scripts/calculate_coupling_scores.py --strength 8 --distance 8-9 --volatility 8
```

The calculator enumerates every integer combination and returns:

- every valid modularity value;
- every valid balance value;
- the exact minimum and maximum;
- the combinations that produced the extremes.

Rules:

- exact inputs produce exact outputs;
- ranged inputs produce an exact result set and min–max range;
- never calculate from the midpoint of a range;
- never report an estimated range such as `2–3` when enumeration produces only `3`;
- if an input range crosses a qualitative anchor, retain the input range and explain the sensitivity instead of forcing a single classification.

### Operational Thresholds

For this skill:

- low: 1–3;
- medium: 4–7;
- high: 8–10.

These thresholds are practical assessment bands, not claims from the source model.

### Qualitative Risk Flags

Flag the following:

```text
UNSTABLE COUPLING = high volatility AND high strength
HIGH CHANGE COST = high volatility AND high distance
GLOBAL COMPLEXITY = high strength AND high distance
LOCAL COMPLEXITY = low strength AND low distance
```

Also flag:

- implicit knowledge;
- unknown ownership;
- unversioned contracts;
- coordinated deployments;
- duplicated volatile rules;
- shared mutable data;
- runtime failure cascades;
- missing tests for the interaction;
- undocumented compatibility assumptions.

## Step 9: Classify the Decision Context with Cynefin

Assign each important hotspot a decision mode.

### Clear

Cause and effect are explicit and predictable.

Agent policy:

- the agent may implement a bounded change;
- require normal tests and review;
- use established rules and examples.

### Complicated

The missing knowledge is identifiable and an expert exists.

Agent policy:

- the agent may analyze and propose;
- require expert validation before significant structural change;
- record the expert decision in code, tests, or an ADR.

### Complex

Cause and effect can only be understood through safe experiments.

Agent policy:

- do not ask the agent for a broad final redesign;
- use characterization tests, probes, observability, small parallel changes, and reversible experiments;
- let the agent collect evidence and implement safe probes;
- keep a human responsible for interpretation.

### Chaotic

There is no reliable relationship between change and outcome, or the system is actively unstable.

Agent policy:

- stop autonomous feature work;
- stabilize, isolate, roll back, add observability, and restore a testable state;
- only then reassess.

### Disorder

There is not enough information to classify the situation.

Agent policy:

- gather evidence first;
- do not invent certainty.


## Verification Evidence Taxonomy

Report each verification activity using one of these states:

- **Discovered:** the tool found the test or suite.
- **Compiled:** test sources and dependencies compiled or bundled.
- **Executed:** the test body ran.
- **Passed:** an executed test completed successfully.
- **Failed:** an executed test failed.
- **Skipped:** the runner intentionally did not execute it.
- **Blocked:** execution could not start because an environment, credential, service, browser, container runtime, or dependency was unavailable.
- **Not run:** the assessor did not attempt it.

Do not collapse these states into “verification passed.” Examples:

- `playwright test --list` means **discovered**, not executed or passed;
- a Maven build with Testcontainers tests skipped because Docker is unavailable means the non-containerised subset may have passed, while those integration boundaries are **blocked** or **skipped** and remain unverified;
- compilation or bundle generation confirms buildability, not runtime behaviour;
- a passing unit suite does not verify a frontend–backend contract unless tests exercise that boundary.

Verification confidence must be scoped to the relationship being assessed. A globally green build does not prove an Orange workflow safe.

## Step 10: Produce the Coupling AI-Readiness Subscore

This is a skill-specific overlay intended to feed a broader AI-readiness assessment. It is not part of the book's equation.

Score each dimension from 0 to 5. Every dimension needs an anchor reason, observed/inferred/unknown evidence, and a reason the nearest lower and higher scores were rejected. Do not assign a precise value merely to complete the table.

| Dimension | 0 | 3 | 5 |
|---|---|---|---|
| Change locality and predictability | Change effects are unknown or chaotic | Typical changes have a partly known surface | Changes are usually bounded and effects are predictable |
| Boundary and ownership visibility | Boundaries and owners are unclear | Main boundaries are visible but exceptions exist | Ownership, dependency direction, and integration points are explicit |
| Knowledge explicitness | Critical assumptions are tacit or encoded in accidents | Some contracts/types/docs exist | Required knowledge is explicit in contracts, types, tests, and decisions |
| Verification and safe experimentation | No reliable safety net | Partial tests or staging evidence | Fast local tests, contract tests, observability, and safe probes exist |
| Context curation for agents | Nearby code is an uncurated mix | Some guidance and examples exist | Good examples, legacy traps, and escalation rules are explicit |
| Agent-operable feedback loop | Build/test/review is slow or irreproducible | Main commands work with manual steps | Reproducible scoped commands and small feedback loops are available |

Rapid-scan rule:

- when material integration, E2E, runtime, ownership, or history evidence is blocked or unknown, report a provisional score range or readiness band;
- use a precise percentage only when all six dimensions are anchored well enough that a one-point plausible change would not materially mislead the reader;
- verification blocked on a critical Orange workflow normally prevents a high verification score, even if unit tests pass elsewhere.
- when a critical cross-application workflow has only build/unit evidence while its integration and E2E suites are skipped, blocked, or merely listed, the verification dimension is normally `1–2`, not `3`; use `3` only when another executed boundary-level safety mechanism is observed and explained.

Calculate:

```text
COUPLING_AI_READINESS = (sum of six dimension scores / 30) * 100
```

Interpretation:

| Score | Interpretation |
|---:|---|
| 85–100 | Suitable for bounded AI-assisted changes with normal human review |
| 70–84 | Suitable with explicit guardrails and hotspot exclusions |
| 50–69 | Use AI for analysis and supervised, small changes |
| 30–49 | High risk; prioritize characterization and boundary clarification |
| 0–29 | Unsuitable for autonomous modification; stabilize and discover first |

Do not average away a critical red flag. A chaotic hotspot, intrusive cross-vendor integration, or untested shared database can constrain the recommended autonomy even when the overall score is high.

## Autonomy Derivation: Decision Context, Consequence, and Verification

Autonomy colour is not a direct synonym for Cynefin and must not be selected from general tone. Derive it from three gates and choose the most restrictive result.

### Gate 1 — Decision-Context Default

| Context | Default autonomy |
|---|---|
| Clear | Green |
| Complicated | Yellow |
| Complex | Orange |
| Chaotic | Red |
| Disorder | No modification until evidence is gathered |

### Gate 2 — Consequence Floor

Evaluate what happens if an agent changes or misunderstands the relationship.

| Condition | Minimum autonomy |
|---|---|
| Static content or reference-data duplication with identifiable scope and no correctness consequence | Yellow |
| Unknown authority for a material rule, but no demonstrated bypass or invalid state | Yellow; Orange when a safe decision requires experimentation |
| UI/client/presentation layer alone enforces eligibility, authorization, capacity, pricing, financial, state-transition, or other business correctness while the accepting backend does not | Orange |
| A direct caller can bypass the visible rule and create invalid accepted, persisted, externally visible, financial, security, or entitlement state | Orange |
| Critical mutation is irreversible, cannot be rolled back, or impact cannot be observed safely | Red |
| Active production instability or uncontrolled failure propagation | Red |

A browser closing a registration form while the backend still accepts registration is a **bypassable correctness boundary**. It is at least Orange even when the code path is easy to understand, because an autonomous change can preserve the UI while leaving business correctness unprotected.

Business criticality alone does not determine colour. The floor is driven by the combination of consequence, bypassability, authority, and recoverability.

### Gate 3 — Verification Floor

| Verification state for the material boundary | Effect |
|---|---|
| Representative boundary behaviour executed and passed | No additional floor |
| Only local unit/build evidence exists | At least Yellow for cross-boundary modification |
| Integration/E2E is skipped, blocked, listed only, or not run | Yellow by itself; Orange when a material invariant can be violated or authority is unknown |
| No safe experiment, observability, rollback, or verification path exists | Red |

Do not make every blocked test Orange. Combine the verification state with the consequence of a wrong change.

### Final Colour

```text
FINAL_AUTONOMY = max(
  decision_context_default,
  consequence_floor,
  verification_floor
)
```

Using the order `Green < Yellow < Orange < Red`.

For every material finding, record:

- `decision_context_default` and reason;
- `consequence_floor` and reason;
- `verification_floor` and reason;
- `final_autonomy` and the determining gate.

Consistency rules:

1. The same final colour appears in the relationship record, workflow, inventory, detailed finding, hotspot distribution, and executive summary.
2. The overall most-restrictive autonomy equals the maximum final colour among material relationship and shared-knowledge hotspots.
3. A workflow's colour equals the maximum colour among its material relationship and shared-knowledge findings.
4. A Yellow result is invalid when `enforcement_status` is client-only or presentation-only, `bypassability` is bypassable, and invalid business state is possible.
5. A statement such as `no Orange hotspot observed` is invalid when any material finding has an Orange consequence or verification floor, even if its Cynefin context was classified as Complicated.

When a JSON report is produced, run `scripts/validate_assessment_consistency.py` before finalizing it.

## Step 11: Assign an AI Autonomy Recommendation by Area

Use:

### Green — Implement and Verify

Conditions usually include:

- clear decision context;
- high or acceptable balance;
- bounded change surface;
- strong tests;
- explicit owner and contract;
- no unresolved critical assumptions.

### Yellow — Analyze, Propose, Implement Small Steps

Yellow is not allowed for a bypassable correctness boundary that can create invalid business state.

Conditions usually include:

- complicated context;
- medium balance;
- model or functional coupling;
- volatile area with reasonable tests;
- human expertise required.

### Orange — Characterize and Probe

Orange is the minimum for a client-only material invariant that the authoritative accepting component does not enforce and that a direct caller can bypass.

Conditions usually include:

- complex context;
- low balance;
- missing knowledge;
- high change cost;
- weak or incomplete tests;
- unknown consumers.

### Red — Do Not Modify Autonomously

Conditions include:

- chaotic behaviour;
- intrusive high-distance coupling;
- unowned shared data;
- irreversible change without rollback;
- no way to verify impact;
- active production instability.

# Recommendation Engine

Recommendations must respond to the dimensions that caused the problem.

## High Strength + Low Distance

This can be healthy local cohesion.

Prefer:

- keep strongly connascent components together;
- encapsulate the relationship inside a deep module;
- preserve transaction or invariant boundaries;
- avoid separating them merely for stylistic purity.

Improve only when local cognitive load becomes excessive.

## Low Strength + High Distance

This is usually the desired shape for independent components.

Prefer:

- preserve narrow contracts;
- version contracts deliberately;
- use consumer-driven or integration contract tests;
- keep implementation models private;
- maintain compatibility during rollout.

## High Strength + High Distance

This is a global-complexity hotspot.

Choose one or both:

1. **Reduce strength**
   - introduce an integration-specific contract;
   - stop exposing internal models;
   - remove direct database access;
   - centralize duplicated volatile rules under one owner;
   - replace control instructions with business facts;
   - encapsulate algorithms, ordering, or transaction details;
   - add translation or an anti-corruption layer.

2. **Reduce distance**
   - colocate highly connascent code;
   - align team ownership;
   - combine deployment units where independent release is fictional;
   - move a shared rule into the component that owns it;
   - keep transactional work inside one consistency boundary.

Do not preserve a distributed design whose components must always change and deploy together without a business reason.

## Low Strength + Low Distance

This is a local-complexity warning.

Prefer:

- remove needless interfaces;
- collapse shallow wrappers;
- reduce navigation and indirection;
- combine tiny modules that do not provide independent value;
- create deeper modules with simpler public interfaces;
- keep related concepts close.

Do not create contract-style ceremony between methods or classes that already share one lifecycle unless there is concrete volatility to isolate.

## High Volatility + High Strength

This is unstable coupling.

Prefer:

- reduce shared knowledge;
- invert dependency toward stable policy;
- extract an explicit contract;
- isolate volatile details;
- clarify the owner of the changing rule;
- add focused tests around the boundary.

## High Volatility + High Distance

This has high cascading-change cost.

Prefer:

- reduce distance when high connascence is essential;
- otherwise reduce strength;
- introduce compatibility windows;
- use parallel change;
- version APIs and events;
- add contract tests;
- remove coordinated-release requirements;
- align ownership and communication.

## Low Volatility

An imperfect coupling design may be good enough.

Prefer:

- document why the coupling is accepted;
- add minimal verification;
- avoid costly redesign without observed change pressure;
- monitor for rising volatility.

Do not lower the score merely because the structure is unfashionable.

## Shared Database

Inspect:

- who owns the schema;
- who writes;
- who reads;
- whether consumers assume immediate consistency;
- whether private table details are exposed;
- whether releases must be coordinated.

Possible improvements:

- assign table or schema ownership;
- prevent cross-service writes;
- expose a narrow API, event, or read model;
- publish a stable contract;
- add migration compatibility;
- colocate components if transactional identity coupling is essential.

Do not automatically split the database. First classify the knowledge and consistency requirements.

## Duplicated Business Rule

Treat as symmetric functional coupling only when the copies execute behaviour and must remain behaviourally equivalent. Duplicated static content, identifiers, labels, routes, dates, or locale values are not sufficient; classify them as reference-data, name, meaning, model, or contract coupling unless they drive independently implemented decisions.

Possible improvements:

- assign one authoritative owner;
- execute the rule in one boundary;
- colocate the copies if duplication is intentional and highly connascent;
- publish a decision or policy outcome instead of duplicating the algorithm;
- add cross-implementation conformance tests only when duplication is unavoidable.

## Replicated Data and Local Caches

Inspect:

- source of truth;
- freshness requirement;
- tolerated staleness;
- ordering;
- idempotency;
- deletion and correction handling;
- rebuild strategy.

Possible improvements:

- expose business-relevant change events;
- avoid publishing the entire internal model;
- define versioned consumer contracts;
- add lag and reconciliation observability;
- make stale-data behaviour explicit.

## Synchronous Runtime Chains

Inspect both runtime and knowledge coupling.

Possible improvements:

- add timeouts, isolation, fallbacks, and bulkheads;
- use asynchronous communication when business semantics allow;
- cache or replicate only with explicit consistency rules;
- reduce the chain's depth;
- avoid pretending that asynchronous delivery removes model or functional coupling.

## Implicit Knowledge

Convert assumptions into:

- types and value objects;
- narrow contracts;
- explicit names;
- invariants;
- executable tests;
- ADRs;
- module-level agent instructions;
- observability;
- ownership records.

For AI readiness, implicit knowledge is a priority because agents will otherwise infer intent from nearby accidents.

## Large or Central Components

A large file, many imports, broad routing table, or central application shell is not a coupling diagnosis by itself.

Before creating a coupling finding, show at least one of:

- unrelated business changes repeatedly propagate through it;
- it owns knowledge that belongs to independently changing components;
- downstream components depend on its internal rules, models, ordering, or lifecycle;
- history demonstrates coordinated changes caused by that shared knowledge.

Otherwise hand the finding to Clean Code for cohesion/readability or to Modern Application Architecture for responsibility and boundary analysis.

# AI-Readiness Improvements

Prioritize improvements that reduce the context reconstruction burden and make change effects observable.

## Immediate Guardrails

- mark good examples;
- mark legacy traps and compatibility code;
- list areas excluded from autonomous changes;
- document build and test commands;
- identify owners and domain experts;
- add stop-and-ask rules for high-risk boundaries;
- require the agent to list affected components before editing;
- require small diffs and explicit verification.

## Tactical Improvements

- add characterization tests around hotspots;
- add contract tests;
- create integration-specific DTOs or events;
- reduce broad payloads;
- extract explicit domain concepts from magic values;
- remove direct cross-boundary persistence access;
- add compatibility and migration tests;
- make transaction, ordering, and consistency assumptions explicit;
- create a representative vertical-slice example.

## Strategic Improvements

- realign component and team boundaries;
- move volatile rules behind stable interfaces;
- merge components that cannot evolve independently;
- split components whose unrelated responsibilities share a lifecycle;
- reduce cross-vendor private knowledge;
- redesign ownership of shared data;
- establish versioned contract governance;
- create a living component and knowledge-flow map.
