---
name: clean-code-refactoring
namespace: codeartify
version: 1.3.0
description: Codeartify reference skill for reviewing, refactoring, and implementing business software with Clean Code principles. Use it to detect code smells, improve names, simplify control flow, remove harmful duplication, model primitive data, reduce inheritance coupling, improve readable algorithms, and refactor safely in baby steps.
---

## About This Skill

This skill was created by **Codeartify** as a practical reference for AI-assisted clean code, code smell diagnosis, and behavior-preserving refactoring.

It is based on Codeartify's Clean Code workshop material around Clean Code principles, code smells, naming, refactoring techniques, harmful duplication, domain-centric abstractions, safe baby-step refactoring, and Java-specific Streams/Lambdas/Optionals practices.

Learn more at **codeartify.com**.

Use it as a decision and implementation aid. Do not use it as a mechanical checklist that forces every smell to be removed regardless of context.

## Purpose

Use this skill when implementing, changing, reviewing, or refactoring code and the main goal is to make the code easier to read, test, maintain, and evolve.

The skill helps an AI agent:

- detect common code smells
- decide whether a smell is harmful in the current context
- choose the smallest useful refactoring
- avoid overengineering
- improve names and intention-revealing structure
- keep behavior stable while refactoring
- decide when duplication should remain or be removed
- use composition, polymorphism, parameter objects, value objects, and extraction responsibly
- keep Java Streams, Lambdas, and Optionals readable when working in Java codebases
- improve poorly named or overly clever algorithmic code when readability, structure, or testability is part of the problem

## When to Use This Skill

Use this skill when the user asks for things like:

- "Review this code for clean code issues."
- "Refactor this method/class."
- "Improve readability without changing behavior."
- "Find code smells."
- "Split this long method."
- "Improve the names."
- "Remove duplication safely."
- "Is this abstraction useful or speculative?"
- "Should this use inheritance, composition, a value object, or polymorphism?"
- "Make this code easier to test."
- "Clean up this Java stream/lambda/optional usage."
- "Make this algorithm easier to understand without changing its behavior."
- "Refactor this legacy code in small safe steps."

## Do Not Use This Skill For

Do not use this skill when the task is only about:

- architecture style selection without local code-quality implications
- deployment, CI/CD, hosting, Kubernetes, or infrastructure operations
- database administration
- UI styling
- pure algorithmic optimization where the task is only about asymptotic complexity, runtime, memory use, or mathematical correctness and not about readability, structure, naming, testability, or maintainability
- rewriting code into a different technology stack
- broad redesign without first understanding current behavior

## Core Principle

Clean Code is not about making code look clever. It is about making the code communicate intention with minimal surprise.

Always ask:

- What does this code try to say?
- Does the name match the behavior?
- Are responsibilities separated clearly?
- Are all statements at the same level of abstraction?
- Is the business/domain idea visible in the code?
- Is the code coupled to details it should not know?
- Would this refactoring reduce future change cost, or only add ceremony?
- Can the change be made in small safe steps while tests remain green?

Prefer simple, specific, intention-revealing code over generic abstractions and speculative extensibility.

## Strong Default: Small, Safe, Intention-Revealing Refactoring

When cleaning code, default to the smallest behavior-preserving change that improves understanding.

Do not start with a big rewrite. First:

1. Identify the most harmful smell.
2. Add or preserve a safety net with tests if possible.
3. Make one small refactoring.
4. Run tests or reason carefully about behavior preservation.
5. Repeat only while clarity improves.

A running, imperfect system is more valuable than a perfectly redesigned but broken one.

## Design Principles for Common Business Software

Prioritize these principles when working on ordinary business software:

1. **Separation of Concerns**  
   Separate code that changes for different reasons. Presentation, business behavior, persistence, integration, formatting, validation, and orchestration should not be mixed accidentally.

2. **Single Responsibility**  
   Variables, methods, classes, packages, and components should each express one clear responsibility.

3. **KISS and YAGNI**  
   Keep the code simple. Do not implement abstractions, configuration, interfaces, or flexibility for hypothetical future needs.

4. **Principle of Least Surprise**  
   Code should do what its name, type, and context suggest. Avoid hidden side effects, output parameters, temporary fields, and misleading names.

5. **Single Level of Abstraction**  
   A method should read at one conceptual level. Do not mix high-level business steps with low-level technical details in the same method.

6. **DRY, Applied Carefully**  
   Remove duplication when it represents the same concept and will change for the same reason. Keep duplication when extraction would couple unrelated callers.

7. **Cycle-Free Dependencies**  
   Avoid dependency cycles between classes, packages, and modules.

8. **Prefer Composition Over Inheritance**  
   Use inheritance only for genuine polymorphic behavior. Do not use inheritance merely for code reuse.

9. **Use SOLID, Interfaces, and Patterns Where They Pay Off**  
   SOLID principles, "program to an interface", and GoF patterns are useful, but should not be applied mechanically. Business software often benefits more from specific, simple code than from generic framework-style extensibility.

## Clean Code Decision Workflow

Before editing code, use this workflow.

### Step 1: Understand Current Behavior

Do not refactor before understanding what the code currently does.

Check:

- public behavior
- callers
- tests
- edge cases
- side effects
- persistence or external interactions
- expected exceptions or error handling

If tests are missing, prefer adding characterization tests around important behavior before changing structure.

### Step 2: Classify the Main Smell

Classify the primary issue as one or more of:

- Naming / readability smell
- Control-flow smell
- Method-size smell
- Side-effect / state-lifecycle smell
- Data-modeling smell
- Responsibility / cohesion smell
- Coupling / encapsulation smell
- Inheritance / polymorphism smell
- Duplication smell
- Overengineering / dead-code smell
- Error-handling smell
- Java Streams/Lambdas/Optionals readability smell, only when the codebase uses those Java/JVM constructs
- Algorithm decomposition / naming smell, when the algorithm works but naming, decomposition, abstraction level, or tests make it hard to understand safely

Do not try to fix every smell at once.

### Step 3: Choose the Smallest Useful Refactoring

Pick the least invasive refactoring that improves clarity:

- rename
- extract variable
- extract method
- introduce guard clause
- split method
- move method
- extract class
- introduce parameter object or value object
- replace flag with explicit methods/factory/polymorphism
- replace inheritance with composition
- inline lazy/speculative abstractions
- safely remove duplicate/dead code

### Step 4: Preserve Behavior

Keep the code compiling and tests runnable.

Use baby steps:

- extract first, change later
- delegate old code to new code before deleting old code
- switch callers gradually
- run tests after each step when possible
- remove scaffolding only after the new structure is used

### Step 5: Re-check Names and Responsibilities

After each refactoring, check:

- Does the new name describe intention?
- Does the method/class now have one responsibility?
- Did extraction create a useful abstraction or just a new place to hide complexity?
- Are callers easier to understand?
- Are tests more focused?

### Step 6: Stop When the Next Refactoring No Longer Pays Off

Do not polish endlessly. Stop when:

- the behavior is understandable
- the names are honest
- responsibilities are reasonably separated
- risky coupling has been reduced
- tests cover the intended behavior
- further changes would mostly be aesthetic or speculative

## Code Smell Diagnosis Guide

Use the following smell guide to diagnose and fix code. The goal is not to remove every occurrence, but to judge whether the smell creates real readability, testability, or change-cost problems.

## Naming and Readability Smells

### Magic Number / Magic String

**Symptom:** A number or string appears without a clear meaning.

Examples:

- `42`
- `"1"`
- `"abc"`
- timeout values without unit
- status values encoded as strings

**Why it matters:** Readers do not know why the value was chosen or what it represents.

**Refactorings:**

- Introduce an explaining local variable.
- Introduce a named constant.
- Introduce an enum for a meaningful set of values.
- Derive expected values explicitly in tests when that clarifies the calculation.
- Improve the test name when literal values are clearer inside tests than extracted constants.

**Do not over-fix:**

- `0` and `1` in simple loop indices are usually fine.
- End-user strings and log messages may already be self-explanatory.

### Redundant Comment

**Symptom:** A comment repeats what the code already says.

**Why it matters:** Repeated information violates DRY and can become inconsistent with the code. It often hides poor naming or too-large methods.

**Refactorings:**

- Delete the comment.
- Rename the variable/method/class so the comment becomes unnecessary.
- Extract a method with an intention-revealing name.
- Disable generated comment templates for trivial getters/setters.

**Comments are acceptable when they explain:**

- exceptional cases
- non-obvious constraints
- hacks or workarounds
- public API usage
- complicated regular expressions
- references to external requirements or documents

### Bad Name

**Symptom:** A variable, method, class, constant, package, or module name is wrong, incomplete, generic, misleading, ambiguous, or too technical.

Typical examples:

- `Manager`, `Helper`, `Util`, `Data`, `Container`, `Tools`
- unclear metrics like `time` without seconds/milliseconds
- abbreviations not understood by the team
- names that describe implementation instead of intention
- names that promise one thing while the code does another

**Why it matters:** Bad names force readers to inspect implementation details and can cause bugs when the name suggests incorrect behavior.

**Refactorings:**

- Rename the symbol.
- Split a class/method/package that cannot be named precisely.
- Introduce a new variable instead of reusing one for another meaning.
- Move classes into packages/namespaces that provide context.
- Remove redundant prefixes when package context already provides meaning.

**Rule:** Do not document bad code. Rename it.

### Good Names

Names are one of the strongest readability tools.

Good names:

- describe intention or purpose
- are specific
- document side effects when side effects are part of behavior
- are unique in their scope
- grow longer as their scope grows
- use common domain language or established pattern names
- include units for quantities when needed

Check names from the caller's perspective, not only from the declaration site.

Naming checklist:

- Does the name say exactly what the code is for?
- Does the code only do what the name suggests?
- Would another developer interpret the name the same way?
- Is the unit clear?
- Is the name generic because the code has too many responsibilities?
- Is a domain term missing?

## Boolean and Control-Flow Smells

### Complicated Boolean Expression

**Symptom:** A condition combines several logical operations, negations, or type checks in a way that is hard to read.

**Why it matters:** Operator precedence, negation, and edge cases are easy to misunderstand.

**Refactorings:**

- Add parentheses to make grouping explicit.
- Extract explaining variables for meaningful parts of the condition.
- Extract predicate methods with domain names.
- Remove double negations.
- Rewrite negative names to positive names where possible.
- Apply De Morgan transformations when they simplify the expression.

**Example direction:**

```java
if (order.isPaid() && !order.isCancelled() && customer.hasValidAddress()) {
    ship(order);
}
```

may become:

```java
if (order.canBeShippedTo(customer)) {
    ship(order);
}
```

### Deeply-Nested Control Flow

**Symptom:** Nested `if`, `else`, `switch`, loops, `try/catch/finally`, or combinations of them make a method hard to scan.

**Why it matters:** Deep nesting mixes abstraction levels, increases cyclomatic complexity, and makes tests harder.

**Refactorings:**

- Introduce guard clauses and early returns.
- Merge nested `if` statements when the condition is naturally one concept.
- Extract predicate methods.
- Extract composed methods.
- Move extracted behavior into appropriate classes when that improves cohesion and testability.

**Guidance:**

- Two levels of nesting can be tolerable.
- Three levels should usually be refactored.
- A method should read at one level of abstraction.

### Long Method

**Symptom:** A method is too large to understand at once or too broad to name precisely.

**Why it matters:** Long methods often violate SRP, mix abstraction levels, hide duplicated logic, and are hard to test.

**Refactorings:**

- Extract methods with intention-revealing names.
- Use comments inside the method as hints for extraction.
- Move extracted methods to classes where the data and behavior belong.
- Replace low-level primitive logic with domain/value objects when useful.
- Split the method if it does more than one thing.

**Do not over-fix:**

- Long but simple mapping or protocol-filling code without branching may be acceptable.

### Single Level of Abstraction

A method should either express high-level intent or low-level mechanics, but not both at the same time.

Bad direction:

```java
public void activateMembership(...) {
    // parse request fields
    // check domain rules
    // calculate dates
    // save database entity
    // send email
}
```

Better direction:

```java
public void activateMembership(...) {
    Membership membership = loadMembership(...);
    membership.activateFor(planDuration);
    save(membership);
    notifyMember(membership);
}
```

Then inspect whether `activateFor`, `save`, and `notifyMember` belong in separate classes or collaborators.

## Side-Effect and State-Lifecycle Smells

### Side Effect

**Symptom:** A method does more than its name implies, especially when a method that looks like a query also changes state.

**Why it matters:** Hidden state changes violate least surprise and make callers lose trust in the code.

**Refactorings:**

1. Rename the method to reveal the side effect if immediate cleanup is too risky.
2. Split query behavior from command behavior.
3. Rename both methods according to their responsibilities.
4. Let the caller invoke both actions explicitly.

**Acceptable side effects:**

- logging
- caching
- known atomic operations such as `incrementAndGet`
- HTTP methods that are expected to change state and return a response

### Temporary Field

**Symptom:** A field is not valid for the whole object lifetime. It is initialized by one method and read by another as a hidden side channel.

**Why it matters:** The object looks valid after construction but contains fields that only become valid after certain call sequences. This creates hidden preconditions and hard-to-find bugs.

**Refactorings:**

- Replace the field with a local variable.
- Extract the calculation into a method called by both places.
- Pass the value explicitly as a parameter.
- Return a value object that contains multiple values if those values form a meaningful concept.
- Use lazy initialization only when it is encapsulated and intentional.

**Avoid:** Generic tuples that only hide unrelated values.

### Error Code

**Symptom:** A method returns special values to signal errors.

Examples:

- `-1` means not found
- `0` means success
- `null` means invalid input
- a numeric return value mixes normal data and error states

**Why it matters:** Callers may forget to check the error code, and business logic becomes mixed with error handling.

**Refactorings:**

- Use exceptions for exceptional or invalid states.
- Use a meaningful result type when errors are expected business outcomes and the language/team uses result types consistently.
- Avoid mixing normal results and error codes in the same primitive return value.

### Output Parameter

**Symptom:** A parameter is passed into a method only so the method can write a result into it.

**Why it matters:** It surprises callers, hides behavior, and often indicates multiple responsibilities.

**Refactorings:**

- Replace the output parameter with a return value.
- Split the method into separate calculations.
- Return a meaningful object if multiple values belong together.
- Avoid mutable containers as disguised return values.

## Data Modeling and Object-Oriented Smells

### Primitive Obsession

**Symptom:** Domain concepts are represented by primitives such as `String`, `int`, `double`, `boolean`, arrays, maps, or raw collections.

Examples:

- `String email`
- `double amount`
- `String currency`
- `int durationDays`
- `Map<String, Object> settings`

**Why it matters:** Domain meaning, validation, units, and behavior are not visible in the code. Rules become duplicated across services, controllers, and helpers.

**Refactorings:**

- Introduce a value object.
- Move validation and domain behavior into the value object.
- Use enums for meaningful closed sets.
- Replace unrelated booleans with explicit concepts.

**Do not over-fix:**

- Pure pass-through DTOs at system boundaries can contain primitives.
- However, even simple validation is logic; if the primitive participates in logic, consider wrapping it.

### Data Clump

**Symptom:** Several primitives appear together repeatedly as fields or parameters.

Examples:

- `street`, `houseNumber`, `zip`, `city`, `country`
- `x`, `y`
- `amount`, `currency`
- `startDate`, `endDate`

**Why it matters:** A missing concept attracts duplicated logic and hides domain language.

**Refactorings:**

- Extract a class or parameter object.
- Move behavior operating on the group into the new class.
- Give the class a domain name.

### Data Class

**Symptom:** A class only contains fields, getters, and setters, with no behavior.

**Why it matters:** Logic that belongs with the data is usually spread across the application. This often follows from solving primitive obsession by introducing a data container but not moving behavior into it.

**Refactorings:**

- Find methods that use the data heavily.
- Extract and move those methods into the data class when cohesion improves.
- Replace setters with intention-revealing methods when invariants matter.
- Keep DTOs as data classes when they are only boundary objects.

### Feature Envy

**Symptom:** A method calls many getters or methods on another object to make a decision or calculation.

**Why it matters:** The behavior likely belongs to the object that owns the data. Feature envy creates high coupling and low cohesion.

**Refactorings:**

- Move the method to the data-owning class.
- Extract a value object and move behavior there.
- Keep responsibility separation in mind: not every method using another object belongs there, but repeated data access is a strong signal.

### Long Parameter List

**Symptom:** A method or constructor has too many parameters.

**Why it matters:** Callers cannot easily understand parameter purpose, same-type arguments can be mixed up, and the method often violates SRP.

**Rule of thumb:** More than 3-4 parameters is suspicious, but context matters.

**Refactorings:**

- Introduce a parameter object when parameters form a meaningful group.
- Introduce value objects for primitive groups.
- Remove flag parameters.
- Split the method if it has multiple responsibilities.
- Use a builder only when object construction complexity justifies it.

### Inappropriate Intimacy

**Symptom:** One object accesses another object's internal state or non-private members directly.

**Why it matters:** Encapsulation is broken, coupling increases, and implementation changes become risky.

**Refactorings:**

- Make fields private.
- Encapsulate access through methods.
- Move behavior into the class that owns the data.
- Merge and re-split classes if direct access reveals the responsibilities are poorly distributed.

**Acceptable cases:**

- inner classes
- builders/factories with controlled package-private access
- framework constraints, when unavoidable

## Inheritance and Polymorphism Smells

### Deep Inheritance Hierarchy

**Symptom:** Classes or interfaces are part of a deep inheritance tree.

**Why it matters:** Inheritance is strong coupling. To understand a child, the reader must understand its parents. Parent changes can break children. Abstract parents are harder to test directly.

**Refactorings:**

- Prefer composition where possible.
- Collapse unused hierarchy levels.
- Replace template-method inheritance with strategy composition when it improves testability and decoupling.
- Keep inheritance only when it expresses real business or framework hierarchy.

### Unnecessary Inheritance

**Symptom:** A subclass does not override behavior and only inherits code for reuse.

**Why it matters:** The child exposes inherited behavior that may not belong to it and is tightly coupled to the parent.

**Refactorings:**

- Replace inheritance with composition.
- Add a field referencing the reused class.
- Delegate only the methods that are actually needed.
- Remove unused inherited behavior.

### Refused Bequest

**Symptom:** A subclass overrides parent behavior but does not honor the parent contract, or it avoids calling required parent behavior.

**Why it matters:** The inheritance relationship is false or fragile. The child does not behave like a proper subtype.

**Refactorings:**

- Add the required parent call if the relationship is valid.
- Replace inheritance with composition if the relationship is not valid.
- Reconsider the hierarchy and contracts.

### Simulated Polymorphic Behavior

**Symptom:** A `switch`, `if/else`, enum, boolean, or type check chooses behavior that could be represented by polymorphism.

**Why it matters:** New behavior requires changing central conditionals, and each branch may grow independently.

**Refactorings:**

- Split behavior into separate methods first.
- Introduce polymorphic types only when behavior truly varies by type/concept.
- Use a strategy object or composition when that is simpler than inheritance.
- Use inheritance carefully when it expresses real substitutability.

### Flag Parameter

**Symptom:** A boolean, enum, null, or mode parameter changes what a method does.

Examples:

- `render(true)`
- `createUser(..., false)`
- `calculate(mode)`

**Why it matters:** One method hides multiple behaviors and callers are hard to read.

**Refactorings:**

- Split the method into explicit methods.
- Replace constructor flags with named factory methods.
- Replace mode branching with polymorphism when behavior is genuinely different.
- Introduce meaningful enums only when they model a domain concept, not to hide a flag smell.

## Duplication, Consistency, and Overengineering Smells

### Duplicated Code

**Symptom:** Similar or identical code appears in several places.

**Why it matters:** Duplicates can fall out of sync and require repeated testing and changes.

**Refactoring workflow:**

1. Transform similar code into identical code plus explicit differences.
2. Run tests.
3. Replace one duplicate with a delegating call to the other.
4. Run tests.
5. Inline or remove the redundant duplicate.
6. Optionally move the single implementation to the class where it belongs.

**Important rule:** Remove duplication only when it represents the same concept and will change for the same reason.

Keep duplication when:

- callers are heterogeneous
- similarity is coincidental
- extraction would create flags
- extraction would couple independent code paths
- the abstraction name would be vague

### Alternative Class with Different Interface

**Symptom:** Two classes provide the same functionality through different APIs.

**Why it matters:** It creates duplicated behavior, duplicated tests, and inconsistent use.

**Refactorings:**

- Align method signatures.
- Move to one implementation.
- Delete or inline the duplicate.
- Keep both only if they serve genuinely different contexts.

### Inconsistent Solution

**Symptom:** The same problem is solved differently in different parts of the codebase.

Examples:

- multiple logging approaches
- different date/time formatting strategies
- inconsistent string formatting
- different equality/hash-code implementations
- inconsistent loop/stream conventions
- inconsistent factories versus constructors

**Why it matters:** Developers must learn many variants and edge cases.

**Refactorings:**

- Discuss and agree on one team/project convention.
- Automate formatting and simple conventions where possible.
- Refactor gradually toward the agreed solution.

### Dead Code

**Symptom:** Code is never referenced or executed, or only exists because someone thought it might be needed again.

**Why it matters:** It must be read, maintained, and understood. Dead code often points to more dead code.

**Refactorings:**

- Use tools to find it.
- Consider reflection/framework usage before deleting.
- Delete it.

### Lazy Class

**Symptom:** A class does too little to justify its existence.

Examples:

- only constants
- only trivial static helpers
- leftover class after refactoring
- tiny abstraction created for speculative design

**Refactorings:**

- Inline the class.
- Move constants/methods to a more appropriate place.
- Collapse hierarchy if it is part of an unnecessary inheritance tree.

**Acceptable cases:**

- framework-required empty implementations
- marker interfaces
- some command-pattern implementations

### Speculative Generality

**Symptom:** Code supports future scenarios that are not required.

Examples:

- interface with one implementation and no real boundary
- abstract class with one subclass
- configuration that never varies
- unused parameters
- unused branches
- test-only production abstractions

**Why it matters:** It violates YAGNI and KISS, adds branches not properly tested, and increases maintenance cost.

**Refactorings:**

- Remove unused parameters.
- Inline unnecessary interfaces.
- Collapse hierarchies.
- Delete unused configuration.
- Rename abstract names to concrete names.

**Do not over-fix:**

Single-implementation interfaces can be useful when they define a real boundary between layers/components, represent a published API, or decouple from infrastructure.

### Large Class

**Symptom:** A class is too broad to express one clear concept.

Typical names:

- `Manager`
- `Helper`
- `Utils`
- `Service`
- `Processor`

**Why it matters:** Large classes are hard to understand and test, often have many dependencies, and frequently contain long methods, primitive obsession, data classes, and procedural code.

**Refactorings:**

- Identify homogeneous caller groups.
- Extract interfaces to understand how callers use the class.
- Split the class according to responsibilities or caller groups.
- Extract collaborators and use composition.
- Move behavior closer to the data it uses.

## Refactoring Techniques

Use these techniques to keep code safe and runnable during refactoring.

### Parallel Change

Use when replacing a structure or changing behavior gradually.

Workflow:

1. Optionally create and test the new structure.
2. Instantiate the new structure in parallel to the old one.
3. Add writes to the new structure while keeping writes to the old one.
4. Switch reads from old to new step by step.
5. Remove writes to the old structure.
6. Delete the old structure after it is unused.

Use this when a direct replacement would be too risky.

### Parallel Change for Method Signatures

Use when changing a non-trivial method signature.

Workflow:

1. Implement and test the new method.
2. Make the old method a small adapter delegating to the new method.
3. Run tests that target the old method.
4. Ensure every old-method edge case is covered for the new method.
5. Change callers to the new method gradually.
6. Remove the old method and obsolete tests.

### Narrow Change

Use when a broad change is hard to perform directly.

Workflow:

1. Create indirection as scaffolding.
   - method: extract the whole body into a new method
   - constructor: wrap with a factory method
   - variable: extract a temporary variable
2. Make the change inside the indirection.
3. Inline the indirection when no longer needed.
4. Simplify callers if useful.

### Safely Remove Duplicates

Use when similar code should become one implementation.

Workflow:

1. Refactor similar code until it is identical except for explicit differences.
2. Run tests.
3. Replace one duplicate with a delegation to the other.
4. Run tests.
5. Inline the delegating method if it adds no value.
6. Move the final implementation if another class owns the concept better.

### Duplicate and Reduce

Use when splitting mixed responsibilities or branches.

Workflow:

1. Duplicate the code into separate locations for separate responsibilities.
2. Reduce each copy to only what that responsibility needs.
3. Rename each result according to its real responsibility.
4. Delete the original mixed structure.

This is especially useful for flag parameters, side effects, and simulated polymorphism.

### Replace Inheritance with Composition

Use when inheritance is only used for code reuse or creates excessive coupling.

Workflow:

1. Add a field referencing the parent/reused class.
2. Instantiate it in the child or inject it.
3. Add delegate methods only for behavior callers actually use.
4. Remove inheritance.
5. Remove or move delegates later if they become middle-men.

### Introduce Polymorphism Carefully

Use when conditional branches represent real behavioral variants.

Workflow:

1. Encapsulate construction behind a factory or creation method.
2. Create a separate type or strategy for each behavior.
3. Move branch-specific logic into the corresponding type.
4. Replace the central conditional with dispatch to the type/strategy.
5. Keep the factory as the only place that chooses the variant.

Prefer composition/strategy over inheritance unless inheritance expresses a real subtype relationship.

### Split Flag-Driven Methods

Use when a method behaves differently based on a boolean or mode.

Workflow:

1. Extract each branch into its own clearly named method.
2. Update callers that pass a constant flag to call the explicit method directly.
3. Remove the outer flag method when all callers are migrated.
4. If the flag is dynamic and meaningful, consider polymorphism or a strategy.

## DRY Guidance

DRY does not mean "remove all repeated text".

Duplication should be removed when:

- it is the same business concept
- it protects the same rule or invariant
- it will change for the same reason
- callers are homogeneous
- a clear name exists for the extracted abstraction
- extraction reduces inconsistency without hiding important differences

Duplication should stay when:

- code only looks similar by coincidence
- callers are heterogeneous
- the two uses may evolve differently
- extraction would introduce flags or mode switches
- the abstraction would be vague, such as `CommonHelper`
- the duplication is small and local
- the duplication is in independent vertical features and no stable shared concept has emerged

When unsure, ask from the caller perspective:

> Are all callers using this code for the same reason, and will that reason stay the same?

If not, avoid premature extraction.

## Algorithmic Code Readability Guidance

Use this skill for algorithmic code when the algorithm already exists or the user asks for a readable implementation, review, or refactoring. Clean Code still matters in algorithms because names, decomposition, tests, and clear invariants make correctness easier to preserve.

Do not use Clean Code as an excuse to hide the algorithm behind vague abstractions. The reader should still be able to see the essential steps.

Prefer:

- meaningful names for concepts, indices, bounds, states, and intermediate results
- small helper functions only when they express a real sub-step of the algorithm
- comments for non-obvious invariants, mathematical assumptions, or complexity trade-offs
- tests for edge cases and representative cases
- keeping performance-relevant data structures visible enough to reason about

Avoid:

- splitting a compact algorithm into many tiny methods that obscure the flow
- renaming standard algorithmic terms into misleading business language
- replacing a clear loop with a clever stream/comprehension/chaining expression
- changing complexity while claiming to only refactor readability
- extracting generic helpers that hide important preconditions

When changing algorithmic code, explicitly state whether behavior, complexity, memory use, or only readability is intended to change.

## Java-Specific Module: Streams, Lambdas, and Optionals

This section is language-specific. Use it only when the codebase is Java or a JVM language where these concepts apply.

The rest of this skill is language-agnostic and should still be used for TypeScript, Python, Go, C#, Kotlin, or other languages.

Use Streams, Lambdas, and Optionals only when they improve readability.

### Streams and Lambdas: Prefer

- Use streams/lambdas when they make data transformation easier to read.
- Keep lambdas short and self-explanatory.
- Use clear lambda parameter names.
- Use method references where they improve clarity.
- Put each stream operation on its own line.
- Chain small transformations instead of writing one large lambda.
- Extract complicated stream expressions into intention-revealing methods.
- Move extracted methods to appropriate classes when behavior belongs elsewhere.
- Annotate functional interfaces with `@FunctionalInterface`.

### Streams and Lambdas: Avoid

- Do not use streams only because they are newer.
- Do not replace simple loops with `forEach` when the loop is easier to debug and read.
- Avoid `forEach` for multi-dimensional transformations where `flatMap` or a loop is clearer.
- Do not specify lambda parameter types unless needed.
- Avoid parentheses for single lambda parameters when the language style allows it.
- Avoid explicit `return` and braces for one-line lambdas.
- Do not store lambdas in variables when a named method would be clearer.
- Do not mutate objects outside the lambda scope unless that side effect is intentional and clear.
- Avoid default methods in functional interfaces unless there is a strong reason.

### Optionals

Prefer:

- Use `Optional` for return values when absence is expected.
- Use `orElse`, `orElseGet`, or `orElseThrow` instead of `isPresent()` followed by `get()`.

Avoid:

- Optional fields.
- Optional method parameters.
- Optional as a way to avoid modeling a real domain state.

## Output Format for Agents

Use the smallest useful output format. Choose the format by risk and scope, not by how many smells were mentioned.

### Format Selection Heuristic

Use **Compact Format** when all of these are true:

- behavior is clear
- the change affects one class or one small local area
- the refactoring is low-risk
- no public contract, inheritance relationship, persistence behavior, or external integration is affected
- the main action is rename, extract variable/method, simplify condition, introduce guard clause, or remove obvious dead code

Use **Full Format** when any of these are true:

- behavior is unclear or tests are missing for important behavior
- more than one class/module is affected
- public APIs, persistence behavior, external integrations, threading, or error handling may change
- the refactoring touches inheritance, polymorphism, duplicated behavior shared by multiple callers, value objects, parameter objects, class splitting, or moving behavior between classes
- the code is legacy, risky, or difficult to reason about
- the user explicitly asks for a plan before implementation

If unsure, use the Full Format. It is safer to be explicit about behavior, risk, and stop conditions than to perform a vague refactoring.

### Compact Format

Use this structure when the Compact Format was selected by the heuristic above.

```text
Clean code focus: <main smell or principle>
Refactoring choice: <smallest useful refactoring>
Reason: <1-3 sentences>
Safety: <tests / behavior preservation>
Changes: <main files/classes/methods>
Follow-up check: <what to inspect after the change>
```

### Full Format

Use this structure when the Full Format was selected by the heuristic above.

```text
Clean code focus:
- <main smells detected>

Current risk:
- <why the current code is hard to read/test/change>

Refactoring strategy:
- <smallest useful strategy>
- <why not a larger rewrite>

Safety plan:
- <tests to keep/add>
- <baby steps / parallel change / adapter step>

Implementation steps:
1. <step>
2. <step>
3. <step>

Expected result:
- <how readability/testability/cohesion improves>

Stop condition:
- <when to stop refactoring>

Trade-offs:
- <accepted duplication/indirection/remaining smell>
```

## Implementation Checklists

### Naming Cleanup Checklist

- Rename from the caller perspective.
- Remove generic names unless they are established domain/pattern terms.
- Include units for numbers.
- Prefer domain language over technical placeholders.
- Ensure the implementation does only what the name promises.
- Delete comments made unnecessary by names.

### Long Method / Nested Control Flow Checklist

- Add characterization tests if behavior is unclear.
- Introduce guard clauses.
- Extract predicate methods.
- Extract composed methods at one abstraction level.
- Use comments as extraction hints.
- Move extracted methods into better classes if they depend heavily on another object.
- Stop before creating tiny methods that obscure flow.

### Side Effect Cleanup Checklist

- Identify queries that mutate state.
- Rename unsafe methods first if immediate split is risky.
- Split query from command.
- Make callers invoke both actions explicitly.
- Avoid output parameters and temporary fields as hidden side channels.

### Data Modeling Checklist

- Look for primitive obsession and data clumps.
- Introduce value objects for meaningful concepts.
- Move validation and behavior into the value object or entity.
- Keep DTOs as simple data when they are only boundary objects.
- Avoid generic tuples unless they are truly idiomatic and local.

### Class Splitting Checklist

- Identify responsibilities.
- Identify caller groups.
- Extract interfaces only to understand usage or define real boundaries.
- Split by cohesive behavior, not by arbitrary method count.
- Move behavior to data-owning classes when feature envy appears.
- Avoid creating lazy classes.

### Duplication Cleanup Checklist

- Decide whether duplication is conceptual or coincidental.
- Check whether callers are homogeneous.
- Make similar code identical before extracting.
- Use delegation before deleting.
- Run tests after each step.
- Avoid introducing flags to support unrelated callers.

### Inheritance Cleanup Checklist

- Check whether inheritance expresses real substitutability.
- Replace code-reuse inheritance with composition.
- Collapse speculative hierarchy levels.
- Replace template methods with strategy composition when testability and coupling improve.
- Keep inheritance when required by framework or domain model.

### Java-Specific Streams/Lambdas/Optionals Checklist

- Use streams only when they improve readability.
- Keep lambdas short.
- Extract complex logic into named methods.
- Avoid external mutation in lambdas.
- Use `Optional` mainly for return values.
- Prefer simple loops when they are clearer.

## Anti-Patterns to Avoid

- Renaming without checking whether the implementation matches the new name.
- Extracting methods that still mix abstraction levels.
- Removing all duplication automatically.
- Creating `CommonUtils`, `Helper`, or `Manager` classes as dumping grounds.
- Introducing interfaces for every class without a real boundary.
- Using inheritance for code reuse.
- Replacing a simple loop with a harder-to-debug stream.
- Treating DTOs as domain models with hidden business behavior elsewhere.
- Returning error codes that callers may forget to check.
- Mutating output parameters to return hidden results.
- Creating value objects without behavior, validation, or clearer language.
- Splitting a large class into many lazy classes.
- Doing a large rewrite when baby-step refactoring would preserve behavior more safely.

## Review Checklist

Before finishing, verify:

- Is the main smell actually addressed?
- Did the refactoring preserve behavior?
- Are tests still green, or was behavior checked explicitly?
- Are names more honest and intention-revealing?
- Does each method operate at a clear abstraction level?
- Are hidden side effects removed or clearly named?
- Are domain concepts visible instead of hidden in primitives?
- Are responsibilities more cohesive?
- Was duplication removed only when it represented the same concept?
- Did the change avoid speculative abstractions?
- Is inheritance still justified, or should composition be used?
- Is the result easier to read from the caller's perspective?
- Is there a clear stop condition, or are further changes mostly cosmetic?

## Example User Prompts and Response Shapes

### Compact Format Example

```text
Rename this confusing helper method and simplify the condition. Behavior should stay the same.
```

Example response shape:

```text
Clean code focus: Bad Name, Complicated Boolean Expression
Refactoring choice: Rename method, extract explaining variable, and simplify the condition locally.
Reason: The behavior is clear, the change is limited to one method, and the current names hide the intent from callers.
Safety: Keep the existing tests unchanged and verify the same branches are still covered.
Changes: Rename isValid(...) to canBeArchived(...); extract hasNoOpenInvoices and isPastRetentionPeriod.
Follow-up check: Re-read the caller to confirm the new name matches the actual behavior.
```

### Full Format Example

```text
Review this Java service for Clean Code issues and refactor it safely. Do not change behavior.
```

Example response shape:

```text
Clean code focus:
- Long Method
- Complicated Boolean Expression
- Feature Envy

Current risk:
- The service mixes orchestration, validation, and domain decisions, so behavior is hard to verify before changing structure.

Refactoring strategy:
- Extract predicates and composed methods first.
- Move validation behavior into the value object only after tests cover the current behavior.
- Avoid a larger rewrite until the existing behavior is protected.

Safety plan:
- Add characterization tests for the current success and failure paths.
- Keep the public method signature unchanged in the first step.

Implementation steps:
1. Extract guard clauses for invalid input.
2. Extract domain predicates with intention-revealing names.
3. Move repeated validation into the domain/value object.
4. Re-run tests and remove now-redundant comments.

Expected result:
- The public workflow reads at one abstraction level and repeated domain validation has a clearer home.

Stop condition:
- Stop once the method is readable, behavior is covered, and moving more code would mostly be cosmetic.

Trade-offs:
- Leave a small amount of duplication between unrelated callers until a stable shared concept emerges.
```
