# Revision Notes

## Applied feedback

- Made qualitative diagnosis mandatory before any numeric score.
- Added observed, inferred, and unknown evidence states.
- Added score calibration rules, rejected-neighbour scores, ranges, and sensitivity checks.
- Added a complete worked example with code, evidence, scoring, AI failure modes, and smallest safe next step.
- Added concrete Git, static-analysis, delivery, runtime, testing, and interview evidence recipes.
- Added an explicit handoff protocol for Clean Code and Modern Application Architecture skills.
- Changed the report structure so hotspot distribution and autonomy constraints precede aggregate percentages.
- Expanded the machine-readable schema to retain evidence status, score rationale, uncertainty, and coupling to preserve.
- Split detailed material into references so the operational `SKILL.md` stays focused.
- Corrected source attribution to Vlad Khononov.

## Output-calibration refinement

- Enforced one directed upstream–downstream relationship per scored record; workflows now aggregate blast radius and autonomy only.
- Added deterministic score-range enumeration through `scripts/calculate_coupling_scores.py`.
- Added explicit rules preventing static duplicated content from being called symmetric functional coupling.
- Added rules preventing line count, import count, or centrality from being treated as coupling evidence by themselves.
- Added precise verification states: discovered, compiled, executed, passed, failed, skipped, blocked, and not run.
- Required rapid scans to use provisional readiness ranges or bands when material evidence is blocked or unknown.
- Expanded report and JSON schemas with workflow maps, verification matrices, calculation provenance, and AI-readiness score rationale.

## Evidence-traceability refinement

- Replaced categorical absence claims with scoped language such as `no Red hotspot observed in the inspected scope`, including evidence limitations that could change the conclusion.
- Separated problem/business volatility from solution volatility and repository churn; commit counts are now corroborative rather than direct V-score inputs.
- Required exact upstream and downstream endpoints using symbols, API fields, JSON paths, events, tables, commands, or runtime endpoints.
- Added direction-basis and authority-status fields so change propagation is not misrepresented as runtime dependency or ownership.
- Added shared-knowledge hotspots for duplicated concepts with unclear authority instead of inventing a source of truth.
- Added full 0–5 anchors for all six AI-readiness dimensions, including context curation and agent-operable feedback.
- Required observed/inferred/unknown evidence and rejected adjacent scores for every readiness dimension before publishing a percentage.
- Added `scripts/calculate_readiness_range.py` to derive provisional percentages from explicit per-dimension ranges and enumerate minimum/maximum assumptions.
- Updated the worked example to use concrete endpoints, classify churn evidence, use a volatility range, and demonstrate deterministic overlay-range calculation.
- Expanded the human and JSON output schemas with evidence coverage, scoped absence claims, shared-knowledge hotspots, endpoint metadata, volatility evidence layers, and readiness range provenance.

## Autonomy-consistency refinement

- Added a three-gate autonomy derivation: decision-context default, consequence floor, and verification floor.
- Made bypassable client-only or presentation-only business invariants at least Orange when invalid accepted or persisted state is possible.
- Clarified that blocked boundary tests are Yellow by themselves but raise the floor to Orange when combined with material correctness consequences.
- Required every hotspot to record business criticality, correctness boundary, bypassability, and explicit autonomy rationale.
- Required workflow, inventory, detailed finding, hotspot distribution, executive summary, and machine-readable output to use the same final colour.
- Added deterministic JSON validation through `scripts/validate_assessment_consistency.py`.

