---
name: non-brittle-tests
description: Create, refactor, and review automated tests that remain stable through behavior-preserving changes. Use when adding tests, fixing brittle or flaky tests, choosing test doubles, reviewing mocks or stubs, improving test readability, or implementing a change with test-driven development.
---

# Non-Brittle Tests

## Purpose

Create tests that provide repeatable proof of externally meaningful behavior without coupling the test suite to incidental implementation details.

A good test should normally remain unchanged during:

- pure refactoring;
- addition of unrelated or compatible features;
- bug fixes outside the tested behavior;
- internal redesign that preserves the owned public contract.

A test should need modification when the behavior or requirement it specifies intentionally changes.

Treat test code as production code: readable, deterministic, maintained, and continuously executed.

## Autonomy

Work through the complete testing task unless a required behavior cannot be determined from the repository, task, examples, or existing contracts.

Do not invent a business rule merely to make a test concrete. When the expected behavior is materially ambiguous:

1. inspect callers, documentation, tickets, API contracts, existing acceptance tests, and nearby examples;
2. state the unresolved behavioral decision;
3. avoid encoding a speculative contract;
4. continue with any unambiguous parts of the task.

Never weaken, delete, skip, or rewrite a valid test merely to make a build pass.

Never claim that tests pass unless they were executed successfully.

## Core Standard

Optimize tests for all of the following:

1. **Behavioral relevance** — a failure indicates a meaningful regression.
2. **Refactoring resistance** — internal restructuring does not require test changes.
3. **Fast diagnosis** — the intended behavior and failure reason are obvious.
4. **Determinism** — the same code and controlled inputs produce the same result.
5. **Appropriate realism** — use as much real production behavior as is practical.
6. **Fast feedback** — use the smallest sufficient test scope, not the smallest possible class isolation.

## Workflow

### 1. Read the Local Context

Before writing or changing tests:

- read the applicable `AGENTS.md`, repository instructions, build files, and test configuration;
- identify the language, test framework, assertion library, mocking library, and normal verification commands;
- inspect a few nearby tests, but do not assume they are good examples;
- distinguish current preferred patterns from legacy or brittle patterns;
- identify the production entry point used by real callers;
- identify how the result can be observed through the unit's owned public API.

Preserve harmless local conventions such as naming syntax and framework idioms. Do not copy local practices that conflict with the behavioral and brittleness rules in this skill.

### 2. Define the Behavior Before the Test Shape

Express each intended test as one behavioral sentence:

> Given [relevant state], when [public action], then [observable outcome].

The behavior is the unit of testing, not the production method or class.

For each behavior, identify:

- the relevant precondition;
- the action a real caller performs;
- the observable result;
- the important boundary or failure case;
- the public contract being protected.

Use Zero, One, Many, Boundaries, Interfaces, Exceptions, and Simple scenarios as coverage prompts. Do not generate every combination mechanically. Select cases that protect meaningful distinctions in behavior.

If one proposed test name requires “and” to join unrelated outcomes, split it unless the outcomes together form one indivisible business behavior.

### 3. Select the Public Testing Surface

Test the system through the API owned and exposed by the unit under test.

“Public API” means the contract used outside the owning unit, not merely a language-level `public` modifier.

Prefer:

- an application use case over its internal helper methods;
- a domain aggregate operation over private validation functions;
- a repository port over the internal map used by an in-memory implementation;
- an HTTP endpoint over controller internals when the endpoint behavior is the contract;
- user-visible UI semantics over component internals or DOM structure.

Do not expose private methods only for tests.

Do not create one test class for every implementation class by default.

A test may execute many real collaborating classes while still verifying a narrow behavior. Scope is determined by the behavior being validated, not by the number of objects instantiated.

### 4. Choose the Smallest Sufficient Test Scope

Use the smallest scope that can verify the behavior reliably through a meaningful contract.

Prefer a narrow test when:

- the behavior is local and observable;
- dependencies are fast and deterministic;
- framework or infrastructure semantics are not part of the risk.

Use a broader integration or acceptance test when correctness depends on:

- database constraints, transactions, queries, mappings, or serialization;
- framework configuration, dependency injection, security, or routing;
- communication between components;
- the fidelity of an external adapter;
- an emergent user workflow not expressed by one unit.

Do not mock a framework or infrastructure contract whose real behavior is the reason for the test.

Unit tests that rely on simplified doubles must be complemented by appropriate contract or integration tests against real implementations.

### 5. Select Collaborators in This Order

Use the following default preference:

1. real implementation;
2. fake;
3. stub;
4. mock, spy, or interaction verification.

#### Real Implementation

Use the real implementation when it is:

- fast enough for the intended feedback loop;
- deterministic;
- hermetic or under test control;
- simple to construct;
- relevant to the behavior.

Prefer realism over class-by-class isolation.

Use the same production composition or factory path when practical, while replacing only genuinely slow, nondeterministic, destructive, or external boundaries.

#### Fake

Use a fake when the real implementation is too slow, unavailable, destructive, nondeterministic, or operationally complex, and a stable behavioral implementation can be provided.

A fake must:

- implement the same owned contract as the real implementation;
- be indistinguishable from the test's behavioral perspective;
- model all semantics on which the test relies;
- reject unsupported paths rather than silently pretending they work;
- remain simpler than the real implementation.

When introducing or materially changing a fake, add shared contract tests that exercise the fake and the real adapter where practical. A fake is an optimization, not proof that the real integration works.

#### Stub

Use a stub only to supply a controlled input or failure that is difficult, unsafe, or impractical to trigger with a real implementation or fake.

Keep stubbing minimal and behavior-oriented.

A warning sign is that the reader must mentally execute the production implementation to understand why each stub exists.

Do not duplicate the collaborator's entire algorithm or contract through canned responses.

#### Mock, Spy, or Interaction Verification

Prefer state or output assertions.

Use interaction verification only when the interaction itself is part of the required behavior, such as:

- the number of calls affects cost, latency, idempotency, or correctness;
- call order is a real protocol requirement;
- the collaborator represents a state-changing external effect that cannot be observed safely through state;
- a rendering boundary intentionally abstracts the produced artifact;
- no practical state-based test exists at a suitable scope.

When interaction testing is necessary:

- verify only the behaviorally significant state-changing call;
- do not verify every delegation;
- do not verify getters or other non-state-changing calls merely because they were stubbed;
- avoid exact call order unless order is part of the contract;
- avoid `verifyNoMoreInteractions` unless unexpected calls are themselves harmful;
- supplement the interaction test with broader state-based coverage when practical.

### 6. Write the Test as Executable Documentation

Each test must be complete and concise.

**Complete** means the test body exposes all information required to understand the behavior and expected result.

**Concise** means irrelevant construction and incidental details are hidden.

Use:

- a behavior-oriented test name;
- one conceptual behavior per test;
- visible Given–When–Then separation, usually through whitespace;
- meaningful test values that explain the expected result;
- focused assertions against observable outcomes;
- clear failure messages where the assertion library does not already provide them.

Prefer test names such as:

- `rejectsWithdrawalWhenBalanceIsInsufficient`
- `membershipRemainsSuspendedUntilAllInvoicesArePaid`
- `returnsNotFoundForUnknownCustomer`

Avoid names such as:

- `testProcess`
- `serviceTest`
- `callsRepository`
- `worksCorrectly`

### 7. Keep Logic Out of Tests

Tests should not require tests of their own.

Avoid:

- branching in a test;
- loops that conceal which case failed;
- reimplementing production algorithms to calculate expected values;
- deriving expected values from the same source as the production result;
- random data without a fixed seed and a clear property-based-testing purpose;
- catching exceptions merely to make assertions;
- temporal sleeps;
- test-order dependencies.

Parameterized tests are appropriate when every row expresses the same behavior with different representative inputs. Use descriptive case labels. Split the test when rows represent different business rules or failure reasons.

For complex expected data, prefer explicit literals, domain factories, or approved fixtures over a second implementation of the production logic.

### 8. Prefer DAMP Test Code Over Premature DRY

Test code should favor Descriptive And Meaningful Phrases.

A small amount of duplication is acceptable when it keeps each test self-contained and readable.

Extract a helper when it:

- hides irrelevant construction details;
- creates meaningful domain values;
- performs one focused conceptual assertion;
- improves both completeness and conciseness.

Do not extract a helper when it:

- hides values used by the assertion;
- performs a generic bundle of unrelated assertions;
- creates a long chain of indirection;
- makes several tests look identical while concealing different behaviors.

Use shared setup only for collaborators and values that are genuinely invariant and irrelevant to the individual behavior. Keep assertion-relevant values in the test body.

### 9. Assert Behavior, Not Representation

Assert only what the behavior promises.

Prefer:

- returned value;
- observable domain state;
- persisted state queried through an owned API;
- emitted domain or integration event when that event is a contract;
- HTTP status and semantic response content;
- accessible UI content and user-observable state;
- externally meaningful error type or result.

Avoid:

- private fields;
- internal collections;
- serialization text when serialization is not the contract;
- exact object equality when only selected properties matter;
- full response or object snapshots for a small semantic requirement;
- incidental ordering when order is unspecified;
- exact timestamps, UUIDs, formatting, or exception messages unless specified.

Use snapshots only when the whole artifact is intentionally reviewed and treated as a stable contract. Never update a snapshot blindly to clear a failure.

Do not over-assert unrelated outcomes. Each additional assertion creates another reason for the test to fail.

### 10. Make the Environment Hermetic and Deterministic

Control nondeterministic inputs explicitly:

- inject or wrap the system clock;
- inject ID generation;
- seed randomness;
- isolate filesystem state;
- replace uncontrolled network access;
- use deterministic containers or local dependencies;
- isolate database data per test;
- avoid shared mutable static state;
- reset framework context only when necessary;
- wait for observable conditions with bounded timeouts instead of sleeping;
- account for concurrency explicitly when concurrency is the behavior.

Do not depend on:

- the current date, timezone, locale, or machine;
- test execution order;
- an existing developer database;
- live third-party services;
- unbounded retries;
- timing luck.

A flaky test is not made acceptable by automatic retries. Diagnose and control the nondeterminism.

### 11. Work in Small Red–Green–Refactor Cycles

When implementing behavior, prefer this cycle:

1. write the smallest test that clearly expresses one missing behavior;
2. run it and confirm that it fails for the expected reason;
3. make the smallest production change that satisfies the behavior;
4. run the focused test;
5. refactor test and production code while green;
6. run the relevant suite;
7. repeat for the next behavior.

Do not add many speculative tests and a large implementation in one step.

For a bug fix, first add a regression test that fails because of the bug, then apply the fix.

For a pure refactoring, establish or identify adequate behavior coverage before changing structure. Keep the suite green after each small step.

Acceptance tests should express stakeholder-visible behavior. Unit and integration tests should provide the fast feedback and design pressure needed to implement that behavior cleanly.

### 12. Execute and Diagnose

Run the narrowest relevant test first, followed by the repository's normal broader verification command when feasible.

When a test fails, classify the failure:

- intended red test proving missing behavior;
- production defect;
- incorrect or brittle test;
- nondeterministic test;
- environment or tooling failure;
- unrelated pre-existing failure.

Do not change the test until the classification is understood.

If an existing test fails after a behavior-preserving refactoring, treat that as evidence of brittleness. Prefer correcting its abstraction level rather than preserving an accidental implementation contract.

If execution is blocked, report:

- the exact command;
- the failure category;
- the relevant error;
- which tests did and did not run;
- what remains unverified.

### 13. Perform the Brittleness Review

Before finishing, evaluate every new or modified test with these questions:

#### Four-Change Check

Would this test remain unchanged after:

- a pure internal refactoring?
- an unrelated compatible feature?
- a bug fix elsewhere?
- replacement of an internal algorithm with equivalent behavior?

Only an intentional change to the protected behavior should normally require changing the test.

#### Public-API Check

- Does the test invoke the unit as a real caller would?
- Does it avoid private methods and internal representation?
- Is the chosen unit boundary owned and meaningful?

#### State-over-Interaction Check

- Can the result be asserted through output or observable state?
- Is every interaction assertion behaviorally necessary?
- Would an equivalent implementation cause a false failure?

#### Double Check

- Can a real implementation be used?
- Would a fake provide better fidelity and clarity than stubs?
- Is every stub necessary to create the scenario?
- Does a fake need a contract test?
- Is the mock reproducing the production implementation?

#### Clarity Check

- Does the name describe action and outcome?
- Is there exactly one conceptual behavior?
- Are the important values visible?
- Can a reader understand the test without opening helpers?
- Is the failure likely to identify the broken behavior immediately?

#### Determinism Check

- Are time, randomness, concurrency, I/O, and external services controlled?
- Is the test independent of order and shared state?
- Are waits condition-based and bounded?

#### Assertion Check

- Are assertions limited to the specified behavior?
- Are unspecified ordering and representation ignored?
- Are snapshots or broad object comparisons justified?

Correct material issues before completion.

## Refactoring Existing Brittle Tests

When asked to improve an existing test suite:

1. identify the behavior each test appears intended to protect;
2. confirm that behavior through requirements, public callers, or acceptance tests;
3. list the implementation details currently coupled to the test;
4. replace private calls with public entry points;
5. replace interaction assertions with state or output assertions;
6. replace unnecessary mocks with real implementations or fakes;
7. reduce stubbing to scenario-driving inputs;
8. split multi-behavior tests;
9. move assertion-relevant values out of hidden setup;
10. remove logic and generic assertion bundles;
11. control nondeterministic dependencies;
12. run tests after each small change.

Preserve behavior coverage. Do not merely make the test shorter.

## Reviewing Tests Written by Others or Generated by AI

Classify findings by consequence:

### Must Fix

- test passes without proving the claimed behavior;
- private or internal implementation detail is the primary test surface;
- excessive interaction verification makes equivalent implementations fail;
- expected result duplicates the production algorithm;
- uncontrolled time, randomness, network, concurrency, or shared state;
- a valid failure is silenced, skipped, retried, or weakened;
- a fake violates a relied-upon contract;
- the test encodes an invented business rule.

### Should Fix

- multiple unrelated behaviors in one test;
- hidden assertion-relevant setup;
- generic or misleading name;
- unnecessary stubs or mocks;
- broad snapshots or over-assertion;
- helper indirection that obscures intent;
- missing real-adapter coverage for a fidelity-sensitive fake.

### Consider

- small DAMP duplication that could remain as-is;
- local naming or formatting consistency;
- parameterization when it improves diagnosis rather than merely reducing lines.

Do not recommend abstractions solely to reduce test line count.

## Completion Report

At the end of the task, provide a concise report containing:

- behaviors added, changed, or reviewed;
- public API or observation surface used;
- real implementations and test doubles chosen, with brief rationale;
- focused and broader commands executed;
- test results;
- any blocked verification or remaining fidelity risk;
- any production design issue revealed by difficulty testing the behavior.

## Reference Principles

This skill synthesizes and operationalizes:

- Codeartify, **Brittle Tests**;
- Codeartify, **Test Doubles**;
- *Software Engineering at Google*, especially Testing Overview, Unit Testing, Test Doubles, and Larger Testing;
- Robert C. Martin, *Clean Craftsmanship*, especially test-driven development, simple design, acceptance testing, continuous build, automated testing, small cycles, and repeatable proof.

Use these references as engineering guidance, not as a substitute for the repository's explicit business requirements.
