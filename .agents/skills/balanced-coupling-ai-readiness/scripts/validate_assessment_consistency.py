#!/usr/bin/env python3
"""Validate autonomy and hotspot consistency in a Balanced Coupling assessment JSON."""

from __future__ import annotations

import argparse
import json
import sys
from collections import Counter
from pathlib import Path
from typing import Any

ORDER = {"green": 0, "yellow": 1, "orange": 2, "red": 3}


def colour(value: Any, path: str, errors: list[str]) -> str | None:
    if not isinstance(value, str) or value.lower() not in ORDER:
        errors.append(f"{path}: expected green|yellow|orange|red, got {value!r}")
        return None
    return value.lower()


def validate_derivation(item: dict[str, Any], path: str, errors: list[str]) -> str | None:
    deriv = item.get("autonomy_derivation")
    if not isinstance(deriv, dict):
        errors.append(f"{path}.autonomy_derivation: missing object")
        return None

    gates = []
    for key in ("decision_context_default", "consequence_floor", "verification_floor"):
        c = colour(deriv.get(key), f"{path}.autonomy_derivation.{key}", errors)
        if c is not None:
            gates.append(c)

    final = colour(deriv.get("final_autonomy"), f"{path}.autonomy_derivation.final_autonomy", errors)
    if len(gates) == 3 and final is not None:
        expected = max(gates, key=ORDER.get)
        if final != expected:
            errors.append(
                f"{path}: final autonomy {final} does not equal most restrictive gate {expected}"
            )

    legacy = item.get("autonomy")
    if legacy is not None:
        legacy_c = colour(legacy, f"{path}.autonomy", errors)
        if final is not None and legacy_c is not None and legacy_c != final:
            errors.append(f"{path}: autonomy {legacy_c} != final_autonomy {final}")

    cb = item.get("correctness_boundary")
    if isinstance(cb, dict) and final is not None:
        enforcement = cb.get("enforcement_status")
        bypass = cb.get("bypassability")
        invalid = cb.get("invalid_state_possible")
        if (
            enforcement in {"client-only", "presentation-only"}
            and bypass == "bypassable"
            and invalid is True
            and ORDER[final] < ORDER["orange"]
        ):
            errors.append(
                f"{path}: bypassable client/presentation-only correctness boundary must be at least orange"
            )

    return final


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("report", type=Path, help="Assessment JSON file")
    args = parser.parse_args()

    try:
        data = json.loads(args.report.read_text())
    except Exception as exc:
        print(f"ERROR: cannot read JSON: {exc}", file=sys.stderr)
        return 2

    errors: list[str] = []
    findings: dict[str, str] = {}
    workflow_colours: dict[str, list[str]] = {}

    for idx, rel in enumerate(data.get("relationships", [])):
        path = f"relationships[{idx}]"
        if not isinstance(rel, dict):
            errors.append(f"{path}: expected object")
            continue
        final = validate_derivation(rel, path, errors)
        rid = rel.get("id") or path
        if rel.get("is_hotspot", True) and final is not None:
            findings[str(rid)] = final
        wid = rel.get("workflow_id")
        if wid and rel.get("is_hotspot", True) and final is not None:
            workflow_colours.setdefault(str(wid), []).append(final)

    for idx, shared in enumerate(data.get("shared_knowledge_hotspots", [])):
        path = f"shared_knowledge_hotspots[{idx}]"
        if not isinstance(shared, dict):
            errors.append(f"{path}: expected object")
            continue
        final = validate_derivation(shared, path, errors)
        sid = shared.get("id") or path
        if shared.get("is_hotspot", True) and final is not None:
            findings[str(sid)] = final
        wid = shared.get("workflow_id")
        if wid and shared.get("is_hotspot", True) and final is not None:
            workflow_colours.setdefault(str(wid), []).append(final)

    workflows = data.get("workflows", [])
    for idx, workflow in enumerate(workflows):
        path = f"workflows[{idx}]"
        if not isinstance(workflow, dict):
            errors.append(f"{path}: expected object")
            continue
        wid = str(workflow.get("id") or path)
        declared = colour(workflow.get("most_restrictive_autonomy"), f"{path}.most_restrictive_autonomy", errors)
        child = workflow_colours.get(wid, [])
        if child and declared is not None:
            expected = max(child, key=ORDER.get)
            if declared != expected:
                errors.append(f"{path}: workflow colour {declared} != material finding maximum {expected}")

    if findings:
        expected_overall = max(findings.values(), key=ORDER.get)
        overall = data.get("overall", {})
        declared_overall = colour(
            overall.get("most_restrictive_autonomy") if isinstance(overall, dict) else None,
            "overall.most_restrictive_autonomy",
            errors,
        )
        if declared_overall is not None and declared_overall != expected_overall:
            errors.append(
                f"overall.most_restrictive_autonomy {declared_overall} != material finding maximum {expected_overall}"
            )

    expected_counts = Counter(findings.values())
    distribution = data.get("hotspot_distribution", {})
    if isinstance(distribution, dict):
        for c in ORDER:
            actual = distribution.get(c, 0)
            if actual != expected_counts.get(c, 0):
                errors.append(
                    f"hotspot_distribution.{c}: {actual} != counted material findings {expected_counts.get(c, 0)}"
                )
    else:
        errors.append("hotspot_distribution: expected object")

    if errors:
        print("Assessment consistency validation failed:", file=sys.stderr)
        for error in errors:
            print(f"- {error}", file=sys.stderr)
        return 1

    print(
        "Assessment consistency validation passed: "
        f"{len(findings)} material findings; most restrictive="
        f"{max(findings.values(), key=ORDER.get) if findings else 'none'}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
