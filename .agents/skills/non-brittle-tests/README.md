# Non-Brittle Tests Codex Skill

This package contains a Codex-compatible agent skill:

```text
non-brittle-tests/
└── SKILL.md
```

The skill is intended to activate when Codex is asked to:

- create tests for new or existing behavior;
- fix or refactor brittle or flaky tests;
- review mocks, stubs, fakes, or interaction-heavy tests;
- implement a change with TDD;
- review AI-generated tests for maintainability.

The `SKILL.md` file contains YAML frontmatter with the skill name and activation description, followed by the complete workflow and guardrails.

Suggested repository-level description, when your Codex setup uses an `AGENTS.md` skill list:

```markdown
- non-brittle-tests: Create, refactor, and review automated tests that verify observable behavior through stable public APIs, prefer state over interactions, and use real implementations or high-fidelity fakes before mocks. Use for test creation, TDD, brittle-test repair, test-double decisions, and test review.
```
