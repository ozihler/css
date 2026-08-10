#!/usr/bin/env python3
"""Calculate Khononov-inspired modularity and balance heuristics deterministically.

Accepts exact integer inputs (for example ``8``) or inclusive integer ranges
(for example ``8-9``). Every combination is enumerated so reported ranges are
exact rather than estimated mentally.
"""

from __future__ import annotations

import argparse
import itertools
import json
import re
import sys
from dataclasses import dataclass
from typing import Iterable


@dataclass(frozen=True)
class Inputs:
    strength: int
    distance: int
    volatility: int


def parse_score(value: str) -> list[int]:
    value = value.strip()
    match = re.fullmatch(r"(10|[1-9])(?:\s*[-–]\s*(10|[1-9]))?", value)
    if not match:
        raise argparse.ArgumentTypeError(
            f"Invalid score '{value}'. Use an integer 1-10 or an inclusive range such as 8-9."
        )
    start = int(match.group(1))
    end = int(match.group(2) or start)
    if start > end:
        raise argparse.ArgumentTypeError(f"Range start {start} exceeds end {end}.")
    return list(range(start, end + 1))


def calculate(inputs: Inputs) -> tuple[int, int]:
    modularity = abs(inputs.strength - inputs.distance) + 1
    balance = max(abs(inputs.strength - inputs.distance), 10 - inputs.volatility) + 1
    return modularity, balance


def score_label(values: Iterable[int]) -> str:
    unique = sorted(set(values))
    return str(unique[0]) if len(unique) == 1 else f"{unique[0]}-{unique[-1]}"


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--strength", required=True, type=parse_score)
    parser.add_argument("--distance", required=True, type=parse_score)
    parser.add_argument("--volatility", required=True, type=parse_score)
    parser.add_argument("--json", action="store_true", help="Emit JSON instead of text.")
    args = parser.parse_args()

    rows = []
    for strength, distance, volatility in itertools.product(
        args.strength, args.distance, args.volatility
    ):
        inputs = Inputs(strength, distance, volatility)
        modularity, balance = calculate(inputs)
        rows.append(
            {
                "strength": strength,
                "distance": distance,
                "volatility": volatility,
                "modularity": modularity,
                "balance": balance,
            }
        )

    modularity_values = sorted({row["modularity"] for row in rows})
    balance_values = sorted({row["balance"] for row in rows})
    result = {
        "inputs": {
            "strength": score_label(args.strength),
            "distance": score_label(args.distance),
            "volatility": score_label(args.volatility),
        },
        "modularity_values": modularity_values,
        "modularity_range": score_label(modularity_values),
        "balance_values": balance_values,
        "balance_range": score_label(balance_values),
        "combinations": rows,
    }

    if args.json:
        print(json.dumps(result, indent=2))
    else:
        print(
            f"S={result['inputs']['strength']} D={result['inputs']['distance']} "
            f"V={result['inputs']['volatility']}"
        )
        print(
            f"MODULARITY values={modularity_values} "
            f"range={result['modularity_range']}"
        )
        print(f"BALANCE values={balance_values} range={result['balance_range']}")
        print("Combinations:")
        for row in rows:
            print(
                "  S={strength} D={distance} V={volatility} "
                "=> M={modularity} B={balance}".format(**row)
            )
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except BrokenPipeError:
        sys.exit(0)
