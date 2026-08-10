# Worked Calibration Example

## Scenario

A fitness platform has an independently deployed **Membership Service** and **Billing Service**.

This example scores one directed relationship with concrete endpoints:

```text
Upstream: membership-service/src/.../MembershipChanged.java::membership.pauseReason
Downstream: billing-service/src/.../BillingPauseFeeCalculator.java::calculatePauseFee()
Direction basis: contract consumption plus representative policy-change propagation
Authority status: duplicated with unclear policy authority
```

It does not assign one score to the wider workflow that may also include a UI, database, payment provider, or email delivery. Those would be separate relationship records linked by a workflow ID.

The requested change is:

> Add a medical membership pause. Medical pauses do not charge the normal pause fee and extend the membership renewal date.

The declared architecture says the services are independently deployable and owned by separate teams.

## Relevant Code

Membership publishes its internal model directly:

```java
public record MembershipChanged(
    MembershipEntity membership,
    String changeReason
) {}
```

Billing consumes the full object and reproduces the pause-fee policy:

```java
public Money calculatePauseFee(MembershipDto membership) {
    if (membership.pauseReason() == PauseReason.MEDICAL) {
        return Money.zero(membership.currency());
    }

    return membership.monthlyFee().multiply(new BigDecimal("0.10"));
}
```

Membership contains a similar rule:

```java
public Money pauseFeeFor(PauseReason reason) {
    return reason == PauseReason.MEDICAL
        ? Money.zero(plan.currency())
        : plan.monthlyFee().multiply(new BigDecimal("0.10"));
}
```

## Evidence Collected

### Observed

- The event serializes `MembershipEntity`, including fields Billing does not use.
- Both services implement the pause-fee algorithm.
- The services have separate build and deployment pipelines.
- Eleven delivery tickets in the last six months changed both repositories. Manual inspection classified six as business-policy changes, three as compatibility/migration work, and two as mechanical dependency updates.
- Four business-policy or compatibility tickets required coordinated production releases.
- There is no consumer/provider contract test for `MembershipChanged`.
- A previous pricing-field rename required an emergency Billing hotfix.

### Inferred

- The two teams treat the Membership model as a shared integration model despite separate ownership.
- Independent deployment is limited because rule and schema changes require coordination.

### Unknown

- Whether other consumers depend on the full event.
- Whether medical pauses require a single cross-service transaction.
- Whether the duplicated rule is intentionally maintained for resilience.

## 1. Qualitative Diagnosis

**Global complexity:** high knowledge coupling spans independently deployed services.

**Unstable coupling:** a frequently changing membership policy is duplicated and exposed through the internal Membership model.

**High change cost:** policy and schema changes repeatedly cross repository, team, release, and deployment boundaries.

**Implicit compatibility knowledge:** consumers rely on the producer's internal entity shape without an explicit versioned contract or contract tests.

Change-propagation mechanism:

1. Membership changes the medical-pause or pricing rule.
2. Billing must discover and reproduce the same semantic change.
3. The broad event may also change because it mirrors the internal entity.
4. Both teams coordinate testing and release.
5. An AI agent working in only one repository can produce a locally correct but systemically inconsistent change.

## 2. Integration Strength

**Score: 9 — symmetric functional coupling.**

Reason:

- both services independently implement the same pause-fee business behaviour;
- the implementations must remain equivalent as the rule evolves;
- the broad shared model adds model coupling, but the strongest applicable level governs the score.

Why not 8:

- functional coupling would apply if Billing merely depended on the Membership workflow or ordering;
- here, both sides reproduce the same algorithm and must stay behaviourally equivalent.

Why not 10:

- Billing does not directly read or mutate Membership's private database or memory;
- the producer exposes too much model knowledge, but the consumer is not accessing private implementation storage directly.

Confidence: **high** for the duplicated-rule relationship.

## 3. Connascence

Highest relevant degree: **algorithm**, with additional **meaning** coupling.

- Both sides must apply the same fee algorithm.
- Both sides must interpret `MEDICAL`, renewal dates, currency, and pause state identically.

No evidence currently requires identity connascence or a single shared in-memory value.

## 4. Distance

**Score: 9 — independently deployed services.**

Reason:

- separate repositories, teams, pipelines, artifacts, and deployments;
- production releases have repeatedly required coordination.

Why not 8:

- 8 would fit independently versioned libraries or artifacts without service runtime and deployment separation;
- these are operational services with independent release machinery.

Why not 10:

- both services are controlled by the same organisation rather than different vendors.

Confidence: **high**.

## 5. Volatility

**Selected score: 8; plausible range: 7–8.**

Knowledge being scored: the membership-pause pricing and renewal policy, not the event file or repository itself.

Problem/business volatility evidence:

- the requested medical-pause feature changes eligibility, charging, and renewal behaviour in the same policy area;
- six inspected tickets changed business policy rather than only implementation;
- product planning identifies further pause and plan-rule changes.

Solution/upstream evidence:

- broad entity-shaped integration causes Billing to adapt when Membership's internal model changes;
- three inspected tickets were compatibility or migration work.

Churn evidence:

- eleven cross-repository tickets were found in six months;
- two mechanical dependency updates were excluded from the business-volatility argument;
- four material changes required coordinated releases.

Why not 6:

- multiple observed business-rule changes and active roadmap pressure exceed moderate supporting-domain evolution.

Why not 9:

- only six months of history and limited roadmap evidence were inspected; sustained experimentation, regulation, or strategic differentiation was not confirmed.

Confidence: **medium** because longer-term roadmap, domain classification, and product-owner testimony are incomplete.

Sensitivity: V=7 or V=8 changes the balance heuristic from 4 to 3 but does not change the qualitative diagnosis, P1 priority, or Orange autonomy.

## 6. Numeric Heuristics

Only after the qualitative diagnosis, calculate deterministically:

```bash
python scripts/calculate_coupling_scores.py --strength 9 --distance 9 --volatility 7-8
```

Result:

```text
S = 9
D = 9
V = 7–8
MODULARITY values = [1]
BALANCE values = [3, 4]
BALANCE range = 3–4
```

Interpretation:

- the low modularity heuristic is consistent with global complexity;
- the 3–4 balance range reflects that neither plausible volatility anchor compensates for high strength at high distance;
- the numbers do not add information beyond the evidenced mechanism; they summarize it.

## 7. Verification Status

- Contract tests: **absent**, so none were executed.
- Cross-service characterization test: **absent**, so none were executed.
- Repository test suites: status outside this example is **unknown**.

The relationship is therefore not described as behaviourally verified merely because either service may compile or have passing local unit tests.

## 8. Cynefin and Agent Autonomy

Current context: **Complex / Orange**.

Reason:

- the main coupling mechanism is visible;
- unknown consumers and transaction requirements make a final redesign unsafe without probes;
- the system can be characterized through contract tests, event-consumer discovery, and a parallel compatibility change.

Agent policy:

- an agent may collect consumers, add characterization tests, and prepare a compatible contract;
- it must not autonomously replace the event or delete the duplicated implementation;
- a human owner must decide whether the rule belongs entirely to Membership, Billing, or a closer shared boundary.

## 9. Likely AI Failure Modes

- update only one implementation of the medical-pause rule;
- add fields to `MembershipEntity` and unintentionally change the event schema;
- copy the existing duplicated rule into another consumer;
- interpret asynchronous delivery as sufficient decoupling;
- create a new shared library that preserves symmetric functional coupling while hiding it behind reuse;
- replace the event without finding unknown consumers.

## 10. Smallest Safe Next Step

1. Discover every consumer of `MembershipChanged`.
2. Add a provider/consumer contract test for the current event.
3. Add a cross-service characterization test proving the current pause-fee result for standard and medical pauses.
4. Record the policy owner.

These steps reduce uncertainty without committing to a target architecture.

## 11. Tactical Recommendation

After consumer discovery:

- introduce an integration-specific, versioned event that exposes the business outcome Billing needs rather than `MembershipEntity`;
- make one component authoritative for the pause-fee decision;
- publish the calculated fee and relevant effective dates, or expose a narrow decision endpoint, depending on consistency needs;
- run the old and new contract in parallel during migration;
- add compatibility and contract tests;
- remove the duplicated algorithm only after all consumers have migrated.

Example contract:

```java
public record MembershipPausePricedV2(
    UUID membershipId,
    PauseReason reason,
    Money chargeAmount,
    LocalDate pauseStartsOn,
    LocalDate renewalDate
) {}
```

This is an example, not a universal prescription. If evidence shows that pause pricing and billing must be one atomic invariant with no meaningful independent lifecycle, reducing distance by colocating the behaviour may be the better design.

## 12. Coupling to Preserve

Preserve the semantic relationship between a pause and its billing consequence. The goal is not to make Billing ignorant of the business outcome; it is to stop Billing from knowing Membership's internal model and reproducing its volatile algorithm.


## 13. Overlay Range Calibration Example

For this workflow area—not the whole platform—suppose the evidence supports:

| Dimension | Score/range | Why lower rejected | Why higher rejected |
|---|---:|---|---|
| Change locality | 1–2 | Some affected endpoints are known | Unknown consumers and coordinated releases prevent 3 |
| Boundary visibility | 3 | Separate services and teams are visible | Policy authority and all consumers are unknown, preventing 4 |
| Knowledge explicitness | 1–2 | The event and enum expose some terms | Broad entity exposure and duplicated algorithm prevent 3 |
| Verification | 1 | Compilation or local tests would not verify the boundary | No executed contract or cross-service test supports 2 |
| Context curation | 2 | The inspected code provides examples and names | No explicit preferred pattern, trap warning, or escalation rule supports 3 |
| Agent feedback loop | 2 | Each repository has local commands | No reproducible cross-service verification supports 3 |

Calculate the provisional range:

```bash
python scripts/calculate_readiness_range.py \
  --change-locality 1-2 \
  --boundary-visibility 3 \
  --knowledge-explicitness 1-2 \
  --verification 1 \
  --context-curation 2 \
  --agent-feedback-loop 2
```

The result is derived from the explicit dimension ranges. Do not add a confidence margin manually. The Orange autonomy restriction still governs regardless of the resulting percentage.

## 14. Final Finding Format

> **Global complexity, unstable coupling, and high change cost:** `MembershipChanged.membership.pauseReason` is consumed by `BillingPauseFeeCalculator.calculatePauseFee()`, while both services independently implement the volatile pause-fee policy across separate deployments. Eleven recent delivery tickets changed both repositories; after excluding two mechanical updates, four material changes required coordinated releases, and no contract test protects the broad entity-shaped event. The relationship is symmetric functional coupling (S=9), service distance (D=9), and business-policy volatility V=7–8 (medium confidence), producing a deterministically calculated balance range of 3–4. Keep the area Orange: agents may discover consumers and add characterization/contract tests, but must not replace the contract or remove either rule autonomously. The smallest safe next step is consumer discovery plus tests for current pause-fee behaviour.
