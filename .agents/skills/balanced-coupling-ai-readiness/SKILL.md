---
name: balanced-coupling-ai-readiness
namespace: codeartify
version: 1.3.0
description: Codeartify reference skill to assess a codebase's coupling state using integration strength, distance, volatility, connascence, lifecycle coupling, and change propagation; identify coupling hotspots and recommend evidence-based improvements that make bounded AI-assisted changes safer and more predictable. It's based on Vlad Khononov's book "Balancing Coupling in Software Design - Universal Design Principles for Architecting Modular Software Systems"
---

# Balanced Coupling and AI Readiness

## Mission

Assess how components share knowledge, how changes propagate across their connections, and whether the coupling is appropriate for the components' distance and volatility.

Use the result to determine:

1. which coupling is essential and should be preserved;
2. which coupling is accidental or exposes unnecessary knowledge;
3. where a change is likely to cascade;
4. which interactions are expensive, unpredictable, or unsafe for AI-assisted modification;
5. what should be strengthened, weakened, moved closer, moved farther apart, documented, tested, or deliberately left unchanged;
6. what level of autonomy an AI coding agent may safely receive in each area.

This skill evaluates **relationships**, not boxes in isolation.

The coupling model is based on Vlad Khononov's *Balancing Coupling in Software Design*. The AI-readiness dimensions and autonomy policy are an explicit extension of that model, not part of the book's formulas.

## Required References

Use these files when the corresponding detail is needed:

- [`references/assessment-model.md`](references/assessment-model.md): scoring anchors, formulas, connascence, Cynefin, AI-readiness dimensions, and recommendation patterns.
- [`references/evidence-recipes.md`](references/evidence-recipes.md): concrete repository, Git, delivery, runtime, and interview evidence-gathering mechanics.
- [`references/worked-example.md`](references/worked-example.md): end-to-end calibrated example, including rejected nearby scores.
- [`references/output-schema.md`](references/output-schema.md): human-readable report structure and machine-readable schema.
- [`scripts/calculate_coupling_scores.py`](scripts/calculate_coupling_scores.py): deterministic modularity and balance calculation for exact scores and score ranges.
- [`scripts/calculate_readiness_range.py`](scripts/calculate_readiness_range.py): deterministic enumeration of provisional AI-readiness ranges from explicit per-dimension score ranges.
- [`scripts/validate_assessment_consistency.py`](scripts/validate_assessment_consistency.py): validate autonomy floors, hotspot counts, and summary consistency in machine-readable reports.

For a feature-centred or full assessment, read the worked example before assigning final scores. For a rapid scan, use it whenever score calibration is uncertain.

## Non-Negotiable Principles

1. **Do not aim for zero coupling.** Components that work together must share some knowledge or lifecycle.
2. **Distinguish essential from accidental coupling.** Preserve coupling that expresses a valid invariant, transaction, policy, or cohesive responsibility.
3. **Do not equate distribution with modularity.** Splitting code into services increases distance; it does not automatically reduce integration strength.
4. **Do not equate asynchronous communication with loose knowledge coupling.** Asynchrony can reduce runtime coupling while model, functional, or intrusive coupling remains high.
5. **Do not recommend microservices merely to reduce coupling.**
6. **Do not introduce interfaces, events, layers, modules, or repositories without a concrete change-pressure reason.**
7. **Do not lower integration strength because an interface, queue, event, or API exists.** Evaluate the knowledge the boundary exposes.
8. **Do not treat import graphs as the complete system model.** Include shared rules, ordering, consistency, ownership, lifecycle, deployment, and runtime assumptions.
9. **Score one directed relationship at a time.** A workflow may receive one aggregate autonomy policy, but never assign one S/D/V tuple to a chain such as frontend → backend → database → email provider. Decompose it into material upstream–downstream relationships.
10. **Do not infer symmetric functional coupling from duplicated content alone.** Repeated IDs, labels, dates, routes, configuration, or catalogue data indicate name, meaning, model, or reference-data coupling unless multiple components independently implement behaviour that must remain equivalent.
11. **Do not use size or centrality as coupling evidence by itself.** A large file, many imports, or a central shell is a Clean Code or architecture signal until shared knowledge or change propagation is demonstrated.
12. **Report verification precisely.** Distinguish discovered, compiled, executed, passed, failed, skipped, blocked, and not run. Listing tests is not executing them; a suite with skipped integration tests does not verify those boundaries.
13. **Use evidence, not architectural fashion.**
14. **Make the qualitative diagnosis before calculating or displaying numbers.**
15. **Treat all numeric values as ordinal heuristics, not measurements.** A score of 7 is not meaningfully precise relative to 6 or 8 without evidence.
16. **Never average away a critical hotspot.** A chaotic, intrusive, unverified, or cross-vendor hotspot constrains autonomy even when the aggregate score is high.
17. **Never hide uncertainty.** Mark evidence as observed, inferred, or unknown and state confidence.
18. **Never turn non-observation into proof of absence.** Say `no Red areas were observed in the inspected scope`, not `there are no Red areas`, when runtime, incident, ownership, or integration evidence is incomplete.
19. **Do not use churn as a direct volatility score.** Commit counts corroborate change frequency; business uncertainty, differentiation, roadmap pressure, regulation, and upstream volatility determine the volatility anchor.
20. **Name concrete endpoints.** Every scored record identifies exact symbols, API fields, schema paths, commands, events, tables, or runtime endpoints—not generic labels such as `policy → service`.
21. **Derive autonomy from both uncertainty and consequence.** Cynefin supplies a default decision mode, but a bypassable correctness boundary, unverified critical invariant, or active instability can impose a stricter autonomy floor.
22. **A client-only business invariant is never merely Yellow when it can be bypassed.** If a backend or other authoritative state-changing component accepts an invalid operation that the UI alone prevents, classify the workflow at least Orange until authoritative enforcement and representative verification exist.
23. **Keep colour assignments internally consistent.** The executive summary, hotspot distribution, workflow, relationship record, and detailed finding must use the same final autonomy colour; the most restrictive material finding governs the area.
24. **Do not modify the codebase during an assessment unless explicitly asked to implement changes.**

## Relationship to Other Skills

Use the three skills as complementary perspectives:

- **Clean Code** owns local readability, naming, function and class design, cohesion, tests, smells, and safe local refactoring.
- **Modern Application Architecture** owns system purpose, domain boundaries, ownership, dependency direction, architectural style, deployment topology, and target structural options.
- **Balanced Coupling** owns knowledge flow, lifecycle coupling, integration strength, distance, volatility, connascence, cascading change, coupling balance, and autonomy implications.

### Handoff Protocol

| Finding | Primary skill | Balanced Coupling's role |
|---|---|---|
| Long method, unclear names, mixed abstraction levels inside one component | Clean Code | Ignore unless it causes cross-component knowledge propagation or obscures an interaction contract |
| Weak cohesion inside a class or module with no cross-boundary effect | Clean Code | Defer entirely |
| Unclear bounded context, target decomposition, ownership, or dependency direction | Modern Application Architecture | Evaluate the consequences of the existing or proposed connection |
| Components repeatedly change together because of shared models, rules, ordering, lifecycle, or runtime assumptions | Balanced Coupling | Own the finding; request architectural options only after diagnosing the coupling mechanism |
| Shared database across independently deployed components | Balanced Coupling | Classify knowledge, consistency, lifecycle, and distance; architecture skill evaluates target ownership and topology |
| Clean readable code participating in a high-distance duplicated business rule | Balanced Coupling | Do not lower the finding because local code quality is good |
| Weak tests make a local refactoring unsafe | Clean Code | Record as coupling evidence only when verification weakness increases cross-boundary uncertainty or autonomy risk |

Do not duplicate another skill's full assessment. Link or hand off the finding with the evidence already collected.

## Assessment Modes

Choose the smallest mode that can answer the question.

### Rapid Scan

Inspect repository structure, build boundaries, imports, public contracts, database ownership, representative tests, deployment manifests, and recent commits for one or two important features.

Produce:

- a provisional component and knowledge-flow map;
- three to seven likely hotspots;
- qualitative diagnoses;
- confidence and missing evidence;
- recommended deep dives;
- a provisional readiness band or range only when all six dimensions have explicit evidence anchors;
- no precise headline percentage when the evidence is insufficient or important verification is blocked;
- absence statements qualified by scope, for example `no Red areas were observed in the inspected static scope`;
- every provisional range derived from explicit per-dimension ranges, never from an unexplained confidence margin.

### Feature-Centred Assessment

Trace one representative feature, defect, or change request from entry point through domain logic, persistence, integrations, release, and verification.

Record every component that must know something, change, test, deploy, or coordinate. This is usually the highest-value AI-readiness mode because it exposes the actual context and blast radius an agent needs.

### Full Coupling Assessment

Combine static structure, version-control history, runtime dependencies, ownership, deployment, tests, incidents, roadmap volatility, and domain knowledge.

## Evidence Discipline

For every material claim, classify the evidence:

- **Observed:** directly visible in code, schemas, history, tests, manifests, telemetry, or documented decisions.
- **Inferred:** strongly suggested by observed evidence but not directly confirmed.
- **Unknown:** requires unavailable history, runtime data, an owner interview, or an experiment.

Use confidence as a summary of the evidence quality:

- **High:** multiple independent observed sources agree.
- **Medium:** the main mechanism is observed but some context is inferred or missing.
- **Low:** the diagnosis depends mainly on inference, sparse examples, or unverified assumptions.

Do not convert `unknown` into a midpoint score. Use a score range or withhold the numeric heuristic until enough evidence exists.

# Assessment Procedure

## Step 1: Establish Purpose, Scope, and Perspective

State:

- system or subsystem;
- business purpose;
- level of analysis: methods, classes, packages, modules, libraries, services, systems, teams, or vendors;
- representative changes;
- source-control time window;
- exclusions and unavailable evidence.

The same interaction can be local at one level and global at another. Never score without naming the perspective.

## Step 2: Gather Evidence

Use the smallest useful set of mechanics from [`references/evidence-recipes.md`](references/evidence-recipes.md).

At minimum inspect:

- static dependencies and public contracts;
- change history for representative features;
- build, test, deployment, and ownership boundaries;
- runtime and consistency expectations;
- tests and observability, including which tests were discovered, compiled, executed, passed, failed, skipped, blocked, or not run;
- domain volatility and roadmap pressure;
- agent instructions, good examples, legacy traps, and reproducible commands.

Record what was not available.

## Step 3: Map Components and Knowledge Flow

For every important workflow, first list the material connections separately. Then, for every important connection identify:

- exactly one concrete upstream endpoint that provides knowledge or functionality;
- exactly one concrete downstream endpoint that consumes or depends on it;
- endpoint kind and location, such as `file::symbol`, API field, JSON path, event field, table/column, command, deployment artifact, or external system endpoint;
- direction basis: runtime call, data flow, contract consumption, ownership, or representative change propagation;
- dependency direction;
- knowledge-flow direction;
- business purpose;
- essential or accidental nature;
- lifecycle and runtime relationship;
- authority status and owners of both sides.

A connection record must have one upstream and one downstream at one perspective. A workflow may link several records through a shared workflow ID and may receive one conservative autonomy colour, but it must not receive a combined strength, distance, volatility, modularity, or balance score.

For duplicated representations with unclear authority, do not invent an architectural source of truth merely to create an arrow. Create a shared-knowledge hotspot, name every representation, mark authority as unknown, and create scenario-specific directed records based on observed data flow or representative change propagation.

Knowledge generally flows from upstream to downstream, opposite to the dependency arrow. State when the arrow represents change propagation rather than a runtime dependency.

## Step 4: Create a Coupling Record

Use:

| Field | Required content |
|---|---|
| ID | Stable directed-relationship identifier |
| Workflow ID | Optional parent workflow used to group multiple relationship records without combining their scores |
| Perspective | Level and scope being scored |
| Upstream / downstream endpoints | Exact provider and consumer identifiers, including endpoint kind and location |
| Direction basis | Runtime call, data flow, contract consumption, ownership, or representative change propagation |
| Authority status | Upstream authoritative, downstream authoritative, shared deliberately, duplicated with unclear authority, or unknown |
| Purpose | Business reason for the connection |
| Business criticality | Low, medium, high, or critical, with the consequence of failure |
| Correctness boundary | The invariant or policy, authoritative enforcer, enforcement status, bypassability, and whether invalid persisted or externally visible state is possible |
| Essential or accidental | Why it must exist or why it is avoidable |
| Knowledge crossing | Data, model, rule, algorithm, ordering, timing, transaction, identity, or private detail |
| Lifecycle coupling | Build, test, deploy, release, rollback, support, and ownership dependencies |
| Runtime coupling | Availability, latency, timing, and failure dependencies |
| Evidence | Observed, inferred, and unknown items |
| Qualitative diagnosis | Risk flags and mechanism, stated before scores |
| Numeric heuristics | Strength, distance, volatility, modularity, and balance when justified |
| Confidence | High, medium, or low with reason |
| AI failure mode | Likely mistake an agent could make |
| Smallest safe next step | Evidence or change that reduces uncertainty or risk |

## Step 5: Diagnose Qualitatively First

Before assigning any number, state the mechanism in plain language.

Use one or more of these diagnoses where applicable:

- **Unstable coupling:** volatile knowledge is shared with high integration strength.
- **High change cost:** volatile knowledge crosses high coordination distance.
- **Global complexity:** strong coupling spans high distance.
- **Local complexity:** weak coupling at very low distance creates unnecessary indirection or shallow modules.
- **Implicit knowledge:** critical assumptions are not represented in contracts, types, tests, decisions, or ownership.
- **Lifecycle fiction:** supposedly independent components must build, test, release, deploy, or roll back together.
- **Runtime cascade:** availability or timing failures propagate across a chain.
- **Unknown coupling:** evidence is insufficient to determine the change mechanism safely.
- **Bypassable correctness boundary:** a presentation layer, client, or downstream consumer enforces a business invariant that the authoritative state-changing component does not enforce, allowing direct callers or alternate clients to create an invalid outcome.

A valid diagnosis must explain **what knowledge is shared**, **between which concrete endpoints**, **how a change propagates**, and **why the current distance or volatility makes that important**. File length, import count, or the fact that a component is central does not satisfy this requirement.

Absence claims are scoped evidence claims. Use `no <severity> hotspot was observed in <inspected scope>` and list the uninspected runtime, incident, ownership, or integration evidence that could change the conclusion.

## Step 6: Assign Numeric Heuristics

Use the anchors and formulas in [`references/assessment-model.md`](references/assessment-model.md) only after the qualitative diagnosis.

For each numeric dimension:

1. cite the anchor used;
2. cite the observed evidence;
3. for volatility, separate business/problem volatility from solution churn and use churn only as corroborating evidence;
4. state why the nearest lower and higher plausible scores were rejected;
5. state confidence;
6. use whole numbers only;
7. use a range when uncertainty crosses an anchor;
8. avoid calculating a single balance value from low-confidence midpoint guesses;
9. calculate modularity and balance with [`scripts/calculate_coupling_scores.py`](scripts/calculate_coupling_scores.py) or an equivalent calculator, never by mental arithmetic;
10. when an input is a range, enumerate every integer combination and report the exact minimum and maximum result; do not estimate the range from endpoints unless the calculator confirms it.

### Mandatory Score Order

Always present an interaction in this order:

1. qualitative diagnosis;
2. evidence and change-propagation mechanism;
3. integration strength, distance, and volatility;
4. modularity and balance heuristic;
5. confidence and sensitivity;
6. recommendation and smallest safe next step.

Never make the score the heading or first sentence of a finding.

### Sensitivity Check

When a score is uncertain, ask whether a plausible one- or two-point change would alter:

- the qualitative diagnosis;
- priority;
- autonomy colour;
- recommended action.

If it would, report the range and choose the more conservative autonomy policy until evidence resolves it. Do not claim that the architecture itself is worse than the evidence supports.

## Step 7: Classify Decision Context and Derive AI Autonomy

Autonomy is derived, not selected impressionistically. Use the detailed rules in the assessment model and apply three gates:

1. **Decision-context default:** Clear → Green, Complicated → Yellow, Complex → Orange, Chaotic → Red, Disorder → gather evidence before modification.
2. **Consequence floor:** determine whether correctness, security, financial state, entitlement, authorization, capacity, eligibility, or another material invariant can be violated.
3. **Verification floor:** determine whether a representative test or safe runtime probe actually executed across the material boundary.

The final autonomy colour is the most restrictive result from those gates.

Mandatory floors:

- a bypassable client-only or presentation-only business invariant that can create invalid accepted or persisted state is **at least Orange**;
- a high- or critical-impact invariant with unknown authority and no executed boundary-level verification is **at least Orange**;
- active instability, irreversible mutation without recovery, or no safe means to observe or verify impact is **Red**;
- duplicated static catalogue or content with unclear authority is usually **Yellow**, unless it controls correctness or can produce an invalid business outcome, in which case reassess for Orange;
- blocked integration or E2E verification does not automatically make every relationship Orange, but it raises the floor when combined with material correctness consequences.

Yellow is valid only when the missing knowledge is identifiable, the change is reversible or bounded, and no caller can bypass a material correctness rule. A low balance score alone does not force Orange, and a high balance score cannot override a consequence floor.

For each material finding, show:

- decision-context default;
- consequence floor and reason;
- verification floor and reason;
- final autonomy colour and the rule that determined it.

The executive summary, workflow map, coupling inventory, detailed hotspot, hotspot distribution, and machine-readable summary must agree. Run [`scripts/validate_assessment_consistency.py`](scripts/validate_assessment_consistency.py) when a JSON report is produced.

The most restrictive material hotspot governs autonomy for that area. Do not derive autonomy from the aggregate readiness percentage alone. When relevant evidence is incomplete, report `no Red hotspot observed` rather than asserting that Red conditions do not exist.

## Step 8: Produce the Coupling AI-Readiness Overlay

Evaluate these six dimensions from 0 to 5 using the detailed anchors in the assessment model. For every dimension, cite evidence, state why the nearest lower and higher scores were rejected, and mark unverified boundary tests as blocked rather than successful:

1. change locality and predictability;
2. boundary and ownership visibility;
3. knowledge explicitness;
4. verification and safe experimentation;
5. context curation for agents;
6. agent-operable feedback loop.

The percentage is a summary, not the diagnosis:

```text
COUPLING_AI_READINESS = (sum of six dimension scores / 30) * 100
```

Rules:

- treat the overlay as incomplete unless **all six** dimensions include selected score or range, observed/inferred/unknown evidence, anchor reason, rejected lower score, rejected higher score, and a range reason when uncertain;
- do not publish a percentage when any dimension lacks that calibration;
- in a rapid scan, assign an explicit plausible range to every uncertain dimension;
- derive the overall range by enumerating those dimension ranges with [`scripts/calculate_readiness_range.py`](scripts/calculate_readiness_range.py) or equivalent deterministic calculation;
- list the uncertain dimensions and the exact assumptions producing the minimum and maximum;
- never create a symmetric or one-sided percentage margin merely from `medium confidence`;
- use a provisional range or band when evidence could plausibly move any dimension by one or more points;
- round a justified percentage to a whole number;
- do not interpret differences of a few percentage points as meaningful;
- show hotspot distribution and autonomy exclusions beside the percentage;
- never let the percentage override a red or orange hotspot;
- derive each final autonomy colour from the decision-context default, consequence floor, and verification floor;
- treat a bypassable client-only business invariant as at least Orange until authoritative enforcement and representative verification exist;
- state that the overlay is skill-specific and not part of Khononov's equation.

## Step 9: Recommend the Smallest Evidence-Based Improvement

Respond to the dimension that caused the problem:

- high strength at high distance: reduce shared knowledge or reduce distance;
- high volatility with high strength: isolate changing knowledge or move it behind a stable owner;
- high volatility at high distance: add compatibility, reduce coordination distance, or weaken the contract;
- low strength at low distance: remove needless abstraction and create a deeper local module;
- intrusive access: restore ownership and stop exposing private implementation details;
- symmetric functional coupling: establish one policy owner or explicit conformance strategy;
- lifecycle fiction: align deployment/ownership or redesign for genuine independence;
- implicit knowledge: encode it in types, contracts, tests, ADRs, agent instructions, and observability;
- insufficient evidence: run the smallest safe experiment before proposing a redesign.

Use the detailed recommendation engine in the assessment model. Preserve justified coupling and document why it is accepted.

## Step 10: Report

Use [`references/output-schema.md`](references/output-schema.md).

The report must lead with:

1. qualitative system state;
2. autonomy constraints;
3. critical hotspots and their mechanisms;
4. evidence confidence;
5. only then numeric summaries.

Include a verification matrix that distinguishes test discovery, compilation, execution, pass/fail, skips, blocked environments, and untested boundaries. Qualify non-observation statements by inspected scope and unknown evidence.

For each hotspot include:

- qualitative diagnosis;
- concrete upstream and downstream endpoints plus direction basis;
- authority status, including unclear or duplicated authority;
- evidence status with separate observed, inferred, and unknown items;
- essential versus accidental coupling;
- risk mechanism;
- numeric rationale and rejected nearby scores;
- likely AI failure mode;
- autonomy derivation: decision-context default, consequence floor, verification floor, and final colour;
- smallest safe next step;
- deliberate coupling to preserve.

# Priority Model

Rank findings by:

1. business criticality;
2. qualitative severity;
3. volatility;
4. integration strength;
5. distance;
6. verification weakness;
7. frequency of related change;
8. evidence confidence.

Use:

- **P0 — Stabilize:** chaotic behaviour, production risk, or no safe verification.
- **P1 — Contain:** volatile, strong, distant, intrusive, or lifecycle-coupled connection on a critical path.
- **P2 — Clarify:** hidden ownership, implicit knowledge, incomplete contracts, weak tests, or uncertain consumers.
- **P3 — Simplify:** local indirection, shallow modules, unnecessary abstractions.
- **P4 — Monitor:** currently balanced or justified by low volatility.

# Quality Check Before Finalizing

Verify that the assessment:

- evaluates relationships rather than only components;
- uses one directed upstream–downstream relationship per scored record and keeps workflow-level autonomy separate;
- states the perspective and representative change;
- distinguishes knowledge, lifecycle, and runtime coupling;
- identifies exact upstream and downstream endpoints, endpoint kinds, and knowledge-flow direction;
- states whether direction is runtime/data flow or representative change propagation;
- uses a shared-knowledge hotspot instead of inventing authority when duplicated representations have no confirmed source of truth;
- classifies evidence as observed, inferred, or unknown;
- leads with a qualitative diagnosis rather than a number;
- does not classify duplicated labels, IDs, routes, or catalogue content as symmetric functional coupling without duplicated behaviour;
- does not use line count, import count, or centrality as coupling evidence without a propagation mechanism;
- explains how the change propagates;
- justifies every score with an anchor and rejects nearby alternatives;
- uses ranges instead of invented midpoint precision;
- treats formulas and percentages as heuristics;
- calculates all formula values deterministically and verifies ranges by enumeration;
- performs a sensitivity check when scores are uncertain;
- identifies local and global complexity;
- separates business/problem volatility from solution churn and never derives V directly from commit count;
- considers volatility, ownership, and delivery boundaries;
- recognizes deliberate strong local coupling;
- does not recommend distribution by default;
- does not claim asynchronous communication eliminates knowledge coupling;
- does not average away critical hotspots;
- qualifies `no Red` and similar absence claims by inspected scope and missing evidence;
- derives autonomy from explicit decision-context, consequence, and verification gates rather than choosing a colour from prose tone;
- never labels a bypassable client-only correctness rule Yellow;
- keeps the final colour identical across summary, workflow, inventory, detailed finding, distribution, and machine-readable output;
- derives readiness ranges from explicit dimension ranges and deterministic enumeration;
- calibrates all six overlay dimensions, including context curation and agent feedback loop, with rejected adjacent scores;
- hands local code-quality and target-architecture findings to the correct sibling skill;
- reports tests as discovered, compiled, executed, passed, failed, skipped, blocked, or not run;
- does not claim a boundary is verified when its integration or E2E tests were skipped or only listed;
- gives a smallest safe next step;
- translates findings into explicit agent guardrails;
- identifies coupling that should be preserved, not only coupling to remove.
