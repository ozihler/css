# Output Schema

## Human-Readable Report

Produce sections in this order.

### 1. Qualitative Executive Summary

Include:

- overall coupling state in plain language;
- most restrictive autonomy areas;
- top three risk mechanisms;
- top three strengths or deliberate coupling decisions;
- most important next action;
- evidence confidence.

Do **not** lead with a percentage or formula result.

### 2. Scope, Perspective, and Evidence

State:

- system and business purpose;
- level of analysis;
- representative change scenarios;
- time window;
- sources inspected;
- exclusions;
- observed, inferred, and unknown evidence;
- overall confidence.

### 3. Component, Workflow, and Knowledge-Flow Map

List components, owners, upstream/downstream direction, dependency direction, knowledge crossing, and lifecycle/runtime relationships.

Represent each workflow as a sequence of directed relationship IDs. A workflow may have one aggregate blast-radius description and autonomy colour, but must not have one combined S/D/V or balance score.

### 4. Hotspot Distribution

Summarize counts and business importance by final autonomy colour and qualitative diagnosis. Count each material relationship or shared-knowledge hotspot once. The counts must agree with the inventory and detailed findings.

Example:

| Autonomy | Count | Critical paths | Main mechanisms |
|---|---:|---|---|
| Red | 1 | Payments | Intrusive cross-vendor access, no rollback |
| Orange | 3 | Membership, Billing | Global complexity, unknown consumers |
| Yellow | 5 | Reporting | Model coupling with contract tests |
| Green | 8 | Local administration | Bounded changes and fast verification |

### 5. Verification Matrix

| Command or suite | Scope / relationship | Discovered | Compiled | Executed | Passed | Failed | Skipped | Blocked / unknown | Interpretation |
|---|---|---:|---:|---:|---:|---:|---:|---|---|

Do not call a boundary verified when relevant integration or E2E tests were only listed, skipped, blocked, or not run.

### 6. Coupling Inventory

Use qualitative information before numeric values:

| Relationship ID | Workflow ID | Qualitative diagnosis | Evidence status | Upstream → downstream | Purpose | S | D | V | Balance | Cynefin | Autonomy | Confidence |
|---|---|---|---|---|---:|---:|---:|---:|---|---|---|

For uncertain inputs, use ranges such as `8–9`. Calculate the result set by enumeration and show an exact result or exact min–max range. Do not invent a midpoint or estimate a formula range mentally.

### 7. Detailed Hotspots

For each hotspot include, in order:

1. qualitative diagnosis;
2. evidence and change-propagation mechanism;
3. observed, inferred, and unknown items;
4. essential versus accidental coupling;
5. integration strength with anchor and rejected nearby scores;
6. distance with anchor and rejected nearby scores;
7. volatility with anchor and rejected nearby scores;
8. modularity and balance heuristic, when justified;
9. sensitivity and confidence;
10. likely AI failure mode;
11. autonomy derivation: decision-context default, consequence floor, verification floor, final colour, and determining gate;
12. smallest safe next step;
13. tactical and strategic options;
14. deliberate coupling to preserve.

### 8. Coupling AI-Readiness Overlay

Show all six 0–5 dimensions with anchor evidence and rejected adjacent scores before any rounded percentage. In rapid-scan mode, use a provisional range or readiness band when material evidence is unknown or blocked.

Include:

- hotspot distribution;
- areas excluded from autonomous changes;
- the most restrictive autonomy policy;
- a note that the overlay is skill-specific and not part of Khononov's formula.

Do not interpret differences of a few percentage points as meaningful.

### 9. Recommendations

Separate:

- immediate guardrails;
- evidence to gather;
- tactical changes;
- strategic changes;
- coupling to preserve;
- items to monitor.

### 10. Agent Guidance to Add

Draft concise repository guidance:

```text
Before changing <area>:
- identify the upstream owner and all known downstream consumers;
- preserve <contract, invariant, ordering, or consistency rule>;
- do not copy <legacy or duplicated pattern>;
- run <scoped tests and commands>;
- stop and ask when <unknown or high-risk condition>;
- keep the change within <boundary>;
- list affected components before editing.
```

### 11. Unknowns and Safe Experiments

List each unknown with the smallest test, trace, source-control analysis, interview, or reversible probe that can resolve it.

## Machine-Readable Summary

Use when useful:

```json
{
  "scope": {
    "system": "",
    "business_purpose": "",
    "perspective": "service|module|package|class|method|system|vendor",
    "change_scenarios": [],
    "history_window": "",
    "exclusions": []
  },
  "overall": {
    "qualitative_state": "",
    "overall_confidence": "high|medium|low",
    "most_restrictive_autonomy": "green|yellow|orange|red",
    "autonomy_consistency_validated": false,
    "critical_hotspots": [],
    "top_strengths": [],
    "most_important_next_action": ""
  },
  "evidence": {
    "observed": [],
    "inferred": [],
    "unknown": []
  },
  "workflows": [
    {
      "id": "",
      "name": "",
      "relationship_ids": [],
      "blast_radius": "",
      "most_restrictive_autonomy": "green|yellow|orange|red"
    }
  ],
  "verification": [
    {
      "command_or_suite": "",
      "relationship_ids": [],
      "discovered": 0,
      "compiled": null,
      "executed": 0,
      "passed": 0,
      "failed": 0,
      "skipped": 0,
      "blocked": [],
      "interpretation": ""
    }
  ],
  "hotspot_distribution": {
    "green": 0,
    "yellow": 0,
    "orange": 0,
    "red": 0
  },
  "coupling_ai_readiness": {
    "score": null,
    "provisional_range": null,
    "readiness_band": "",
    "is_secondary_summary": true,
    "dimensions": {
      "change_locality": 0,
      "boundary_visibility": 0,
      "knowledge_explicitness": 0,
      "verification": 0,
      "context_curation": 0,
      "agent_feedback_loop": 0
    },
    "dimension_rationale": {
      "change_locality": {"anchor_reason": "", "rejected_lower": "", "rejected_higher": ""},
      "boundary_visibility": {"anchor_reason": "", "rejected_lower": "", "rejected_higher": ""},
      "knowledge_explicitness": {"anchor_reason": "", "rejected_lower": "", "rejected_higher": ""},
      "verification": {"anchor_reason": "", "rejected_lower": "", "rejected_higher": ""},
      "context_curation": {"anchor_reason": "", "rejected_lower": "", "rejected_higher": ""},
      "agent_feedback_loop": {"anchor_reason": "", "rejected_lower": "", "rejected_higher": ""}
    },
    "autonomy_exclusions": []
  },
  "shared_knowledge_hotspots": [
    {
      "id": "",
      "workflow_id": "",
      "concept": "",
      "representations": [],
      "authority_status": "shared-deliberately|duplicated-unclear|unknown",
      "business_criticality": "low|medium|high|critical",
      "correctness_consequence": "",
      "is_hotspot": true,
      "autonomy_derivation": {
        "decision_context_default": "green|yellow|orange|red",
        "decision_context_reason": "",
        "consequence_floor": "green|yellow|orange|red",
        "consequence_reason": "",
        "verification_floor": "green|yellow|orange|red",
        "verification_reason": "",
        "final_autonomy": "green|yellow|orange|red",
        "determining_gate": "decision-context|consequence|verification"
      },
      "confidence": "high|medium|low"
    }
  ],
  "relationships": [
    {
      "id": "",
      "workflow_id": "",
      "perspective": "",
      "upstream": "",
      "downstream": "",
      "purpose": "",
      "business_criticality": "low|medium|high|critical",
      "correctness_boundary": {
        "invariant_or_policy": "",
        "authoritative_enforcer": "",
        "enforcement_status": "enforced-at-authority|duplicated|client-only|presentation-only|downstream-only|unknown|not-applicable",
        "bypassability": "not-bypassable|bypassable|unknown|not-applicable",
        "invalid_state_possible": null
      },
      "is_hotspot": true,
      "essential": true,
      "qualitative_diagnosis": [],
      "change_propagation_mechanism": "",
      "knowledge_crossing": [],
      "evidence": {
        "observed": [],
        "inferred": [],
        "unknown": []
      },
      "integration_strength": {
        "score": "8|8-9",
        "type": "contract|model|functional|symmetric-functional|intrusive",
        "anchor_reason": "",
        "rejected_lower": "",
        "rejected_higher": ""
      },
      "connascence": [],
      "distance": {
        "score": "8|8-9",
        "anchor_reason": "",
        "rejected_lower": "",
        "rejected_higher": ""
      },
      "volatility": {
        "score": "8|8-9",
        "anchor_reason": "",
        "rejected_lower": "",
        "rejected_higher": ""
      },
      "calculation": {
        "method": "scripts/calculate_coupling_scores.py",
        "modularity_values": [],
        "balance_values": [],
        "modularity_range": null,
        "balance_range": null
      },
      "sensitivity": "",
      "cynefin": "clear|complicated|complex|chaotic|disorder",
      "autonomy_derivation": {
        "decision_context_default": "green|yellow|orange|red",
        "decision_context_reason": "",
        "consequence_floor": "green|yellow|orange|red",
        "consequence_reason": "",
        "verification_floor": "green|yellow|orange|red",
        "verification_reason": "",
        "final_autonomy": "green|yellow|orange|red",
        "determining_gate": "decision-context|consequence|verification"
      },
      "autonomy": "green|yellow|orange|red",
      "confidence": "high|medium|low",
      "confidence_reason": "",
      "likely_ai_failure_modes": [],
      "smallest_safe_next_step": "",
      "recommendations": [],
      "coupling_to_preserve": []
    }
  ]
}
```

When numeric inputs are ranges, populate `calculation.modularity_values`, `calculation.balance_values`, and their exact min–max ranges from deterministic enumeration. When evidence is too weak to justify the inputs, leave the calculation fields empty and explain why; do not serialize a fabricated midpoint.


Validation rules:

- each relationship object has exactly one upstream and one downstream;
- no workflow object contains S, D, V, modularity, or balance;
- `symmetric-functional` requires duplicated behaviour, not duplicated static content;
- line count or centrality cannot be the sole `change_propagation_mechanism`;
- test discovery/listing cannot populate `executed` or `passed`;
- formula fields must come from deterministic enumeration, not mental arithmetic;
- `autonomy` equals `autonomy_derivation.final_autonomy`;
- final autonomy equals the most restrictive of decision-context default, consequence floor, and verification floor;
- a relationship with client-only or presentation-only enforcement, bypassable access, and possible invalid state is at least Orange;
- workflow autonomy equals the most restrictive material relationship or shared-knowledge hotspot in that workflow;
- `overall.most_restrictive_autonomy` equals the most restrictive material finding;
- hotspot-distribution counts equal the number of material findings by final autonomy;
- validate these rules with `scripts/validate_assessment_consistency.py` when JSON output is available.
