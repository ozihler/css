#!/usr/bin/env python3
"""Calculate the skill-specific coupling AI-readiness score range.

Each dimension accepts an exact integer from 0 to 5 or an inclusive range such
as ``2-3``. Every combination is enumerated. The script reports possible sums
and rounded percentages without inventing a midpoint or unexplained range.
"""

from __future__ import annotations

import argparse
import itertools
import json
import re
import sys
from typing import Iterable

DIMENSIONS = (
    "change_locality",
    "boundary_visibility",
    "knowledge_explicitness",
    "verification",
    "context_curation",
    "agent_feedback_loop",
)


def parse_score(value: str) -> list[int]:
    value = value.strip()
    match = re.fullmatch(r"([0-5])(?:\s*[-–]\s*([0-5]))?", value)
    if not match:
        raise argparse.ArgumentTypeError(
            f"Invalid score '{value}'. Use an integer 0-5 or an inclusive range such as 2-3."
        )
    start = int(match.group(1))
    end = int(match.group(2) or start)
    if start > end:
        raise argparse.ArgumentTypeError(f"Range start {start} exceeds end {end}.")
    return list(range(start, end + 1))


def label(values: Iterable[int]) -> str:
    unique = sorted(set(values))
    return str(unique[0]) if len(unique) == 1 else f"{unique[0]}-{unique[-1]}"


def rounded_percentage(total: int) -> int:
    return round((total / 30) * 100)


def main() -> int:
    parser = argparse.ArgumentParser()
    for dimension in DIMENSIONS:
        parser.add_argument(f"--{dimension.replace('_', '-')}", required=True, type=parse_score)
    parser.add_argument("--json", action="store_true", help="Emit JSON instead of text.")
    args = parser.parse_args()

    ranges = {dimension: getattr(args, dimension) for dimension in DIMENSIONS}
    combinations = []
    for values in itertools.product(*(ranges[d] for d in DIMENSIONS)):
        scores = dict(zip(DIMENSIONS, values, strict=True))
        total = sum(values)
        combinations.append(
            {
                "dimensions": scores,
                "sum": total,
                "percentage": rounded_percentage(total),
            }
        )

    sums = sorted({row["sum"] for row in combinations})
    percentages = sorted({row["percentage"] for row in combinations})
    uncertain_dimensions = [d for d in DIMENSIONS if len(ranges[d]) > 1]

    minimum_sum = min(sums)
    maximum_sum = max(sums)
    minimum_combinations = [row for row in combinations if row["sum"] == minimum_sum]
    maximum_combinations = [row for row in combinations if row["sum"] == maximum_sum]

    result = {
        "inputs": {d: label(ranges[d]) for d in DIMENSIONS},
        "uncertain_dimensions": uncertain_dimensions,
        "sum_values": sums,
        "sum_range": label(sums),
        "percentage_values": percentages,
        "percentage_range": label(percentages),
        "minimum_combinations": minimum_combinations,
        "maximum_combinations": maximum_combinations,
        "combination_count": len(combinations),
        "combinations": combinations,
    }

    if args.json:
        print(json.dumps(result, indent=2))
    else:
        print("Dimension inputs:")
        for dimension in DIMENSIONS:
            print(f"  {dimension}={result['inputs'][dimension]}")
        print(f"Uncertain dimensions: {', '.join(uncertain_dimensions) or 'none'}")
        print(f"SUM values={sums} range={result['sum_range']}")
        print(
            f"READINESS percentage values={percentages} "
            f"range={result['percentage_range']}%"
        )
        print("Minimum assumptions:")
        for row in minimum_combinations:
            print(f"  {row['dimensions']} => {row['sum']}/30 ({row['percentage']}%)")
        print("Maximum assumptions:")
        for row in maximum_combinations:
            print(f"  {row['dimensions']} => {row['sum']}/30 ({row['percentage']}%)")
        print(f"Combinations enumerated: {len(combinations)}")

    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except BrokenPipeError:
        sys.exit(0)
