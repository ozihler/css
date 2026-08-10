---
name: modern-application-architecture
description: Codeartify reference skill for implementing, changing, or reviewing software features and deciding how to structure them internally. Start with Vertical Slice Architecture by default, then escalate only when concrete feature pressure justifies Layered Architecture, Onion Architecture, Hexagonal Architecture, Clean Architecture, richer DDD-style domain modeling, CQRS, or domain events.
---

## About This Skill

This skill was created by **Codeartify** as a practical reference for AI-assisted software architecture decisions.

It is based on Codeartify's software architecture training material around internal application structure, Vertical
Slice Architecture, Clean Architecture, Hexagonal Architecture, DDD-style domain modeling, CQRS, and domain events.

Learn more at **codeartify.com**.

## Purpose

Use this skill when implementing, changing, reviewing, or planning application features and you need to decide how the
feature should be structured internally.

The skill helps an AI agent choose between Vertical Slice Architecture, simple layering, Onion Architecture, Hexagonal
Architecture, Clean Architecture, richer DDD-style domain modeling, CQRS, and domain events.

Do not blindly apply one pattern everywhere. Select the lightest structure that protects the business logic, keeps
change local, and makes the feature understandable, testable, and evolvable.

## When to Use This Skill

Use this skill when the user asks for things like:

- "Implement this feature using a good architecture."
- "Use Clean Architecture / Hexagonal / Vertical Slice / DDD."
- "Where should this code go?"
- "How should I structure this feature?"
- "Refactor this service into a better architecture."
- "Review whether this should use CQRS, events, DDD, or a simple CRUD structure."
- "Add a controller, service, repository, and domain model for this use case."
- "Implement this feature and decide the architecture based on the reference."

## Do Not Use This Skill For

Do not use this skill when the task is only about:

- deployment, infrastructure, Kubernetes, CI/CD, or hosting
- frontend styling
- database administration
- performance tuning without application-structure implications
- purely algorithmic tasks with no architectural design needed

## Core Principle

Architecture is about trade-offs, not pattern application.

Always ask:

- What kind of logic is this feature mainly about?
- Where are the business rules and invariants?
- What is likely to change independently?
- Which dependencies should the core logic avoid?
- Would additional boundaries reduce complexity or add unnecessary overhead?
- Can the feature be implemented as a clear vertical slice first?
- Is duplicated code accidental, or does it reveal a missing shared concept?

Prefer simple structure until the feature's business complexity, integration complexity, or expected evolution justifies
more indirection.

## Strong Default: Start With Vertical Slice

Start with **Vertical Slice Architecture** by default for new features and use cases.

A new feature should usually begin as one cohesive use-case-oriented slice because this keeps related code close
together and makes the feature easier to understand and evolve.

Only escalate beyond a vertical slice when the feature shows concrete pressure:

- shared domain invariants across use cases
- multiple inbound adapters
- external dependency isolation
- complex lifecycle rules
- diverging read/write models
- asynchronous domain reactions
- high testability requirements without framework wiring
- explicit use cases that are expected to grow

Default escalation path:

1. Start with a vertical slice for feature locality.
2. Add a small domain model when the feature has business rules.
3. Add a rich DDD-style domain model when invariants, lifecycle rules, and consistency protection matter.
4. Add Hexagonal or Onion boundaries when external dependencies should be isolated from the core.
5. Use Clean Architecture only when explicit use cases, protected business rules, ports/adapters, and strong testability
   justify the overhead.
6. Use CQRS only when read and write models genuinely diverge.
7. Use domain events only for meaningful domain facts that benefit from decoupled reactions.
8. Before finishing, perform a local duplication and responsibility review.

## Architectural Concerns

Separate these concerns deliberately.

### Presentation / Inbound Adapters

Responsible for user-facing or machine-facing entry points:

- HTTP controllers
- REST/GraphQL endpoints
- message listeners such as Kafka listeners
- CLI handlers
- request/response mapping
- basic technical validation

Presentation code should not contain core business rules.

### Application / Business Workflow

Responsible for use cases and workflows:

- application-specific business rules
- orchestration
- transaction boundaries
- authorization decisions when tied to use-case behavior
- coordination between domain objects, repositories, and external ports

### Domain / Core Model

Responsible for business rules that belong to the model itself:

- invariants
- lifecycle rules
- consistency protection
- domain behavior
- value objects
- entities
- aggregates where useful
- domain events where useful

### Infrastructure / Outbound Adapters

Responsible for communication with external systems:

- databases
- repositories
- external HTTP APIs
- file systems
- email sending
- messaging such as Kafka producers
- notification systems

Infrastructure code should depend inward, not force the business logic to depend on external technologies.

## Decision Workflow

When asked to implement, change, or review a feature, use this workflow before writing code.

### Step 1: Classify the Feature

Classify the feature as one or more of the following:

- Simple CRUD
- Workflow-heavy application use case
- Domain-heavy business feature
- Integration-heavy feature
- Reporting/read-heavy feature
- Event-driven/asynchronous workflow
- Cross-cutting technical concern

### Step 2: Identify the Business Rules

List the rules that must be protected:

- invariants
- lifecycle constraints
- consistency requirements
- authorization/business policy
- validation that is more than input shape validation
- important decisions the system makes

If there are no meaningful business rules, avoid heavyweight architecture.

### Step 3: Identify Change Axes

Identify what may change independently:

- HTTP/API contract
- persistence model
- external APIs
- messaging technology
- business rules
- read model/reporting needs
- workflow steps
- UI interaction model

Separate things that change for different reasons.

### Step 4: Start With a Vertical Slice

Unless the user explicitly requested another structure, first consider the feature as a vertical slice.

Ask:

- Can the whole use case be understood in one feature package?
- Would horizontal layers scatter related code?
- Is this feature likely to evolve independently?
- Can the feature start simple and grow later?

Use the vertical slice as the first organizing principle, then enrich it with domain modeling, ports/adapters, or Clean
Architecture only if justified.

### Step 5: Choose the Architecture Style Using Escalation Gates

Choose the lightest architecture that fits the feature.

Use the architecture selection guide and escalation gates to decide whether the feature should remain a Vertical Slice
or whether it should escalate to Layered Architecture, Onion Architecture, Hexagonal Architecture, Clean Architecture,
richer DDD-style modeling, CQRS, or domain events.

Do not escalate because a pattern sounds cleaner. Escalate only when concrete feature pressure justifies it.

When working in an existing codebase, match the surrounding codebase's dominant architecture pattern unless there is a
strong reason to deviate. Introducing a Vertical Slice into a codebase already organized around Clean Architecture - or
vice versa - adds inconsistency that must be justified.

### Step 6: Produce an Implementation Plan

Before editing code, produce a concise plan containing:

- chosen architecture style
- why it fits
- why lighter/heavier alternatives were not chosen
- package/module structure
- main classes/files to create or modify
- dependency direction
- test strategy
- trade-offs accepted

Use the compact output format for simple features and the full output format for complex features.

### Step 7: Implement the Smallest Useful Version

Implement only the boundaries that add value now.

Do not add ports, interfaces, events, factories, repositories, or abstractions just because a pattern allows them.

### Step 8: Perform a Final Local Refactoring Pass

Before considering the feature done, inspect the code that was added or changed.

Look for:

- duplicated business rules
- repeated validation that should be a value object
- repeated mapping that should be centralized locally
- duplicated query or persistence logic
- controller logic that belongs in the application layer
- application-service logic that belongs in the domain model
- domain behavior hidden in repositories or mappers
- abstractions that add ceremony but no clarity
- shared code extracted too early

Refactor only when it improves clarity, reduces coupling, protects business rules, or removes meaningful duplication.

Do not remove all duplication automatically. Some duplication between vertical slices is acceptable until it clearly
represents the same business concept or policy.

## Architecture Selection Guide

### Start With Vertical Slice Architecture by Default

Use Vertical Slice Architecture as the default starting point for new features and use cases.

Good fit:

- most new features
- feature locality matters
- developers should find all feature code in one place
- the feature can evolve independently
- the domain is not yet complex enough to justify a full layered or Clean Architecture structure
- the team wants low cognitive overhead
- you want to avoid broad god services

Typical structure:

```text
features/
  bookConsultation/
    BookConsultationController.java
    BookConsultationRequest.java
    BookConsultationHandler.java
    BookConsultationResult.java
    ConsultationSlot.java
    ConsultationSlotRepository.java
```

For a smaller feature:

```text
features/
  subscribeToNewsletter/
    SubscribeToNewsletterController.java
    SubscribeToNewsletterRequest.java
    SubscribeToNewsletterHandler.java
```

Rules:

- Organize by feature/use case first.
- Keep endpoint, request model, handler, persistence access, and business flow close together.
- Extract shared abstractions only when duplication proves meaningful.
- Avoid premature global service layers.
- Move rules into domain objects if invariants become important.
- Let the slice evolve into Clean, Hexagonal, or DDD-style structure only when needed.

Benefits:

- strong feature locality
- easier navigation
- lower cognitive load
- simpler evolutionary structure
- reduces cross-layer scattering
- makes use cases explicit

Trade-offs:

- some duplication may appear
- boundaries may be less explicit initially
- shared abstractions emerge later
- inconsistent patterns can appear if not guided

Avoid when:

- rich shared domain model is central from the beginning
- strict dependency inversion is required
- cross-feature invariants are important and need a shared domain core
- multiple adapters must call the same use case from the start

### Use Simple Layered Architecture When

Use Layered Architecture for simple features where the main need is clear separation between presentation,
business/application logic, and infrastructure, especially in an existing layered codebase.

Good fit:

- CRUD-heavy features
- admin screens
- low domain complexity
- few business rules
- simple validation and persistence
- teams that need an easy-to-understand default
- existing codebase already uses layers consistently

Typical structure:

```text
feature/
  controller/
  service/
  repository/
  model/
```

or:

```text
presentation/
application/
domain/
infrastructure/
```

Rules:

- A layer below cannot access a layer above.
- Presentation calls application/service layer.
- Application/service layer coordinates business behavior.
- Application calls infrastructure layer.
- Infrastructure handles persistence and external systems.
- Avoid putting business rules into controllers or database entities by accident.

Benefits:

- easy to understand
- low overhead
- good starting point
- suitable for simple systems

Trade-offs:

- features may become scattered across layers
- services may grow too large
- domain models may become anemic
- relaxed layering can create hidden coupling to infrastructure

Avoid when:

- business rules are complex
- infrastructure independence matters strongly
- feature locality is more important than technical layering
- application services are already becoming god classes

### Use Onion Architecture Only After an Escalation Gate

Use Onion Architecture when you want layered structure, but the business core must not depend on infrastructure.

Do not choose Onion Architecture only because dependency inversion sounds cleaner.

Prefer Onion over simple layering only when both are true:

- the domain model is meaningful enough to protect from infrastructure concerns
- infrastructure replaceability, testability, or framework independence matters

If the feature is simple CRUD and the domain model has little behavior, prefer Vertical Slice or simple Layered
Architecture.

Good fit:

- business logic should be protected from database/framework details
- domain model matters
- infrastructure should be replaceable or mockable
- you want dependency inversion without a full Clean Architecture ceremony
- the system still feels naturally layered

Typical structure:

```text
domain/
  model/
  service/
application/
  ports/
  services/
infrastructure/
  persistence/
  external/
presentation/
  api/
```

Rules:

- Dependencies point inward.
- Domain must not depend on application, infrastructure, or presentation.
- Application may define ports/interfaces for infrastructure needs.
- Infrastructure implements those ports.

Benefits:

- business logic independent from infrastructure
- better testability
- clearer protection of the core model
- infrastructure can be replaced more easily
- less ceremony than full Clean Architecture

Trade-offs:

- more interfaces and indirection than simple layering
- can be unnecessary for simple features
- layers can still become broad and muddy if not organized by use case or domain concept

Avoid when:

- feature is simple CRUD
- interfaces would only mirror repositories without adding value
- the domain model is not meaningful
- explicit use-case input/output boundaries are the main design force

### Use Hexagonal Architecture / Ports and Adapters When

Use Hexagonal Architecture when the most important design force is separating the application core from external
technologies.

Good fit:

- multiple inbound channels call the same logic, such as REST, messaging, and CLI
- multiple outbound technologies exist or may change
- external systems should be replaceable
- testing the application core without frameworks is important
- integrations are complex

Typical structure:

```text
application/
  port/in/
  port/out/
adapter/in/web/
adapter/in/messaging/
adapter/out/persistence/
adapter/out/http/
adapter/out/messaging/
```

Rules:

- Application core defines ports.
- Inbound adapters call inbound ports/use cases.
- Outbound adapters implement outbound ports.
- The core does not depend on frameworks, databases, or messaging libraries.
- No fix structure of the application core. It can vary according to complexity.

Benefits:

- strong separation between application logic and external systems
- good testability
- flexible integration model
- infrastructure replaceability

Trade-offs:

- additional indirection
- can become interface-heavy
- says little about internal application structure
- inbound ports can become too broad or god-like if not split by use case

Avoid when:

- there is only one simple UI and one simple database
- no meaningful external dependency boundary exists
- ports would duplicate implementations without improving testability or clarity

### Onion vs Clean Architecture

Onion Architecture and Clean Architecture overlap in practice because both use inward-pointing dependencies and
dependency inversion.

Choose **Onion Architecture** when:

- the main concern is protecting the domain model from infrastructure
- the system still feels naturally layered
- use cases do not need many explicit input/output models
- dependency inversion around persistence/external systems is enough
- you want less ceremony than Clean Architecture

Choose **Clean Architecture** when:

- explicit use cases are central
- interactors/application workflows need to be first-class concepts
- input/output boundaries matter
- multiple adapters call the same use cases
- rich entities and use-case interactors both matter
- the team benefits from strict separation between adapters, use cases, ports, and entities

Rule of thumb:

- Onion protects the domain model.
- Clean Architecture protects the domain model and organizes the application around explicit use cases.

### Use Clean Architecture Only After Escalation Gates

Use Clean Architecture when the feature has explicit use cases, important business rules, and a real need to protect the
core from delivery and infrastructure details.

Do not choose Clean Architecture only because the user says "good architecture" or because the feature has a controller,
service, and repository.

Choose Clean Architecture only when at least two of these are true:

- The feature has explicit use cases with non-trivial workflow.
- Business rules must be protected from frameworks and infrastructure.
- The domain model contains invariants or lifecycle rules.
- Multiple inbound adapters need to call the same use case.
- Multiple outbound technologies exist or are likely to change.
- The use case needs isolated tests without Spring, JPA, HTTP, Kafka, or other frameworks.
- The feature is expected to grow into several related use cases.

If only one gate is true, prefer Vertical Slice with a small domain model or Hexagonal boundaries around the specific
dependency.

Good fit:

- complex business domains
- use-case-oriented systems
- high testability requirements
- business rules must be framework-independent
- multiple adapters may interact with the same use cases
- you want explicit application boundaries
- rich domain model should be protected from frameworks and infrastructure

Typical feature-oriented structure:

```text
features/
  bookConsultation/
    domain/
      ConsultationSlot.java
      ConsultationSlotId.java
      ConsultationBooking.java
      ConsultationBooked.java
    application/
      port/in/
        BookConsultationUseCase.java
        BookConsultationCommand.java
      port/out/
        LoadConsultationSlotPort.java
        SaveConsultationSlotPort.java
        PublishConsultationEventPort.java
      usecase/
        BookConsultationUseCaseInteractor.java
    adapter/in/web/
      BookConsultationController.java
      BookConsultationRequest.java
      BookConsultationResponse.java
    adapter/out/persistence/
      JpaConsultationSlotRepository.java
      ConsultationSlotJpaEntity.java
      ConsultationSlotMapper.java
    adapter/out/messaging/
      ConsultationEventPublisher.java
```

Alternative package-by-layer structure:

```text
domain/
application/
adapter/in/
adapter/out/
```

Rules:

- Controllers translate transport-specific requests into use-case input models.
- Use case ports define application operations.
- Interactors implement use cases and orchestrate workflows.
- Outbound ports define what the application needs from the outside world.
- Gateways/out adapters implement outbound ports.
- Controllers/in adapters use inbound use case ports if present.
- Entities contain core business rules and invariants.
- Dependencies point inward.
- Framework annotations must stay out of the domain model.
- Prefer keeping framework annotations out of the application core as well.

Benefits:

- strong separation of concerns
- explicit application boundaries
- high testability
- good fit for complex domains
- business rules are protected

Trade-offs:

- significantly more indirection
- more abstractions and files
- higher cognitive overhead
- can feel heavyweight for simple CRUD

Avoid when:

- the feature is basic CRUD
- use cases do not contain meaningful behavior
- no important business rules need protection
- the team would pay more in ceremony than it gains in clarity
- only one Clean Architecture escalation gate is present

## Clean Architecture With a Rich DDD Entity Layer

In Clean Architecture, the entities layer can be more than simple data structures and vague critical business rules.
When the domain is complex enough, use the entities layer as a rich DDD-style domain model.

Use a rich DDD entity layer inside Clean Architecture when:

- business rules belong naturally to domain concepts
- invariants must always be protected
- invalid state should be impossible or hard to create
- lifecycle transitions matter
- the same rules are needed across multiple use cases
- application interactors are becoming too procedural
- domain language is important for understanding the code

### Responsibilities of the Entity / Domain Layer

The domain layer may contain:

- entities
- aggregates
- value objects
- domain services, only when behavior does not naturally belong to one entity
- domain events
- domain-specific exceptions or result types
- factories, only when creation is complex enough to justify them

Entities and aggregates should protect:

- invariants
- lifecycle rules
- state transitions
- consistency boundaries
- business decisions that belong to the model

Value objects should protect:

- validation of domain values
- equality by value
- formatting or normalization rules when domain-relevant
- small business concepts such as EmailAddress, Money, TimeSlot, or BookingReference

Domain events should represent:

- meaningful facts that happened in the domain
- facts other parts of the system may react to
- completed state changes, named in past tense

### Responsibilities of the Application Interactor

When using a rich domain model, the interactor should not contain all business decisions.

The interactor should:

- load aggregates/entities through outbound ports
- call intention-revealing domain methods
- coordinate transaction boundaries
- call external ports
- persist changed aggregates/entities
- publish domain events if needed
- return use-case results

The interactor should not:

- freely mutate entity fields through setters
- duplicate invariants already protected by the domain model
- contain lifecycle rules that belong to entities
- become a procedural god service

### Example: Booking a Consultation Slot

A rich domain model might contain:

```text
domain/
  ConsultationSlot.java
  ConsultationSlotId.java
  ConsultationSlotStatus.java
  ConsultationBooking.java
  EmailAddress.java
  TimeRange.java
  ConsultationBooked.java
```

The aggregate could expose behavior like:

```text
ConsultationSlot.reserveFor(customerEmail, customerName)
ConsultationSlot.cancel(reason)
ConsultationSlot.markAsCompleted()
```

The use case interactor then orchestrates:

```text
1. Load ConsultationSlot through LoadConsultationSlotPort.
2. Call slot.reserveFor(customerEmail, customerName).
3. Save the changed slot through SaveConsultationSlotPort.
4. Publish ConsultationBooked if decoupled reactions are useful.
5. Return BookConsultationResult.
```

The business rule "a slot must not be double-booked" belongs in `ConsultationSlot`, not only in the controller,
repository, or interactor.

### Rich Entity Layer Checklist

Use this checklist when Clean Architecture contains a DDD-style entity layer:

- Are invariants enforced inside entities, aggregates, or value objects?
- Are domain methods named by business intention?
- Are setters avoided for state that must be protected?
- Does the application interactor orchestrate instead of deciding everything?
- Are persistence entities separate from domain entities if the ORM would pollute the model?
- Are value objects used for meaningful domain values?
- Are domain events named as past-tense facts?
- Is the aggregate boundary small enough to stay understandable?
- Are domain rules testable without Spring, JPA, HTTP, Kafka, or other frameworks?

## Domain Modeling Guidance

Use richer domain modeling when business behavior and consistency matter.

Choose a richer domain model when:

- the feature has lifecycle rules
- invalid state must be prevented
- decisions belong naturally to business objects
- invariants must be protected consistently
- domain language matters
- coordination/change cost is high
- multiple use cases depend on the same core rules

Prefer anemic/simple models when:

- the feature mainly stores and retrieves data
- behavior is trivial
- business rules are minimal
- a richer model would only add ceremony

### Rich Domain Model

Use entities and value objects with behavior.

Example responsibilities:

- `Membership.reactivate()` decides whether reactivation is allowed.
- `BillingReference.applyPayment()` protects payment consistency.
- `BookingSlot.reserve()` prevents double booking.

Rules:

- Keep invariants close to the data they protect.
- Prefer intention-revealing methods over setters.
- Avoid allowing external services to mutate domain state freely.
- Keep domain objects independent from infrastructure details.
- Test domain behavior directly.
- Model each invariant with the smallest amount of state that can still enforce the rule correctly.

### Anemic Domain Model

Use simple data structures when behavior is not meaningful.

Rules:

- Do not force behavior into objects if it does not improve clarity.
- Keep business logic in an application service or handler if the model is only data.
- Watch for growth: if service logic becomes complex, move rules into a domain model.

## DDD Guidance

Use DDD ideas when they reduce business ambiguity and coordination cost.

Use DDD when:

- language between business and developers matters
- business concepts are complex
- boundaries between concepts are unclear
- multiple teams or modules overlap
- domain rules change often
- invariants and lifecycle rules need explicit modeling

DDD does not always require full tactical patterns. Sometimes the useful step is simply:

- use better names
- identify invariants
- define cohesive business concepts
- separate unrelated models
- make boundaries explicit

Avoid deep DDD ceremony when:

- the system is mostly CRUD
- domain behavior is trivial
- terminology is already obvious
- the added structure does not reduce real complexity

## Bounded Context Guidance

Use bounded contexts as a higher-level boundary when different parts of the system need different domain models,
language, rules, or ownership.

A bounded context sits above features and use cases:

```text
bounded-context/
  features/
    <feature-or-use-case>/
      <handler/controller/domain/repository/etc.>
```

or, for a larger feature-oriented codebase:

```text
contexts/
  consultationBooking/
    features/
      bookConsultation/
      cancelConsultation/
      listAvailableSlots/
  marketing/
    features/
      subscribeToNewsletter/
      downloadLeadMagnet/
```

A bounded context is not just a folder. It is a boundary around a model, language, and set of business rules.

Introduce a bounded context when one or more of these pressures exist:

- the same term means different things in different parts of the system
- different business capabilities have different rules and lifecycles
- different teams or modules own different parts of the domain
- one model is becoming too broad or overloaded
- changes in one area should not ripple into another area
- integration between two parts needs translation rather than direct sharing
- separate persistence, deployment, or transaction boundaries may become useful

Do not introduce bounded contexts just to create a nicer folder structure.

Avoid bounded contexts when:

- the application is still small
- the language and model are shared and unambiguous
- the feature can be clearly expressed as a single vertical slice
- the boundary would only add ceremony
- there is no real difference in business rules, ownership, or model language

Rule of thumb:

- Use vertical slices to organize use cases.
- Use bounded contexts to separate models.

## CQRS Guidance

Use CQRS only when separating reads and writes solves a real problem.

Use CQRS when:

- read and write models differ significantly
- queries require different data shapes than commands
- reporting is complex
- performance requirements differ between reads and writes
- writes need strong behavior and reads need optimized projections

Typical structure:

```text
feature/
  command/
    CreateSomethingCommand.java
    CreateSomethingHandler.java
  query/
    GetSomethingQuery.java
    GetSomethingHandler.java
    SomethingReadModel.java
```

Rules:

- Commands change state.
- Queries do not change state.
- Do not share write aggregates as read DTOs if that creates coupling.
- Keep CQRS local to the feature unless the whole system needs it.

Avoid CQRS when:

- CRUD is simple
- read and write models are almost identical
- the split would only duplicate code
- consistency and synchronization concerns would add more cost than value

## Domain Events Guidance

Use domain events when important domain facts should trigger decoupled behavior.

Use domain events when:

- something important happened in the domain
- multiple reactions should happen without tightly coupling the workflow
- long-running processes need coordination
- asynchronous boundaries provide value
- other parts of the system need to react to business facts

Examples:

- `MembershipActivated`
- `PaymentReceived`
- `ConsultationBooked`
- `SubscriptionRequested`

Rules:

- Name events in past tense.
- Events represent facts, not commands.
- The domain or application layer may publish events after successful state changes.
- Event handlers must be idempotent when delivery may be retried.
- Consider transaction boundaries carefully.
- Keep event payloads meaningful and stable.

Trade-offs:

- eventual consistency
- retries
- duplicate handling
- ordering concerns
- harder debugging
- additional operational complexity

Avoid events when:

- a direct method call is clearer
- the workflow must stay simple and synchronous
- no real decoupling is needed
- the event would only be a technical notification without domain meaning

## Architecture Decision Matrix

| Feature type                                         | Recommended default                                         | Escalate when                                                                 |
|------------------------------------------------------|-------------------------------------------------------------|-------------------------------------------------------------------------------|
| Simple CRUD                                          | Vertical Slice or Layered                                   | business rules grow                                                           |
| Feature with clear use case but limited domain rules | Vertical Slice                                              | infrastructure independence matters                                           |
| Domain-heavy feature                                 | Vertical Slice with rich domain model                       | use cases and adapters need explicit boundaries                               |
| Integration-heavy feature                            | Vertical Slice with a local port, or Hexagonal              | multiple technologies/adapters appear                                         |
| Complex workflow                                     | Vertical Slice with clear handler                           | workflow needs ports/adapters or protected domain invariants                  |
| Reporting/read-heavy                                 | Vertical Slice query handler                                | reads and writes diverge significantly                                        |
| Async reaction to business fact                      | Direct call first, then Domain Events                       | decoupling is useful and retry/idempotency cost is acceptable                 |
| Multiple delivery mechanisms                         | Hexagonal or Clean                                          | use-case boundaries must be explicit                                          |
| Explicit use cases plus protected business rules     | Vertical Slice with use-case handler, or Clean Architecture | choose Clean Architecture only when at least two escalation gates are present |
| Multiple domain models or overloaded language        | Bounded Contexts containing vertical slices                 | terms/rules/ownership diverge                                                 |

## Output Format for Agents

Use the smallest useful decision format.

### Compact Format

Use this for simple CRUD, small vertical slices, or low-risk changes.

```text
Architecture choice: <chosen style>
Reason: <1-3 sentences>
Structure: <main files/classes>
Refactoring check: <what to check before finishing>
```

### Full Format

Use this for domain-heavy, integration-heavy, workflow-heavy, CQRS, event-driven, Hexagonal, Onion, or Clean
Architecture decisions.

```text
Architecture choice: <chosen style>

Reason:
- <why this style fits>
- <why lighter/heavier alternatives were not chosen>

Feature classification:
- <CRUD / domain-heavy / integration-heavy / workflow-heavy / read-heavy / async>

Escalation gates:
- <which gates are present, if choosing a heavier architecture>

Proposed structure:
<folders/classes/modules>

Dependency direction:
- <what depends on what>

Implementation steps:
1. <step>
2. <step>
3. <step>

Tests:
- <unit tests>
- <integration tests if needed>

Final refactoring pass:
- <duplication/responsibility checks>

Trade-offs:
- <accepted trade-off>
```

## Implementation Checklists

Use these checklists after choosing an architecture style. The Architecture Selection Guide decides what to use; this
section verifies that the implementation follows the chosen style without repeating the full decision logic.

### Vertical Slice Feature Checklist

Use by default for new use cases.

```text
features/<feature-name>/
  <Feature>Controller
  <Feature>Request
  <Feature>Handler
  <Feature>Result
  <Feature>Repository
```

Implementation checklist:

- Keep the feature code together.
- Avoid global services unless actually shared.
- Let duplication exist briefly if extracting abstraction would be premature.
- Move rules into domain objects if invariants become important.
- Add ports/adapters only when there is a real boundary to protect.
- Before finishing, review whether duplicated code is accidental or meaningful.

### Simple Layered Feature Checklist

Use when the feature is simple and the codebase is already organized around horizontal layers.

```text
controller -> service -> repository
```

Implementation checklist:

- Controller contains request mapping only.
- Service contains use-case workflow and simple business rules.
- Repository contains persistence access.
- DTOs do not leak unnecessarily into domain logic.
- Tests cover service behavior and repository integration if needed.

### Onion Architecture Checklist

Use when the domain model is meaningful and infrastructure independence matters, but full Clean Architecture would add
too much ceremony.

```text
presentation -> application -> domain
infrastructure -> application/domain ports
dependencies point inward
```

Implementation checklist:

- Keep the domain model independent from infrastructure and frameworks.
- Define ports only where infrastructure details must be inverted.
- Let infrastructure implement the application/domain ports.
- Avoid creating use-case input/output models unless they improve clarity.
- Prefer this over Clean Architecture when protecting the domain is enough.
- Avoid Onion when the feature is simple CRUD or the domain model has little behavior.

### Hexagonal Feature Checklist

Use when external systems should stay outside the core.

```text
core/application defines ports
adapters call or implement ports
```

Implementation checklist:

- Define inbound port/use case if multiple inbound adapters or testability justify it.
- Define outbound port for each external dependency used by the core.
- Implement outbound ports in infrastructure adapters.
- Test core with fake adapters.

### Clean Architecture Feature Checklist

Use when explicit use cases and protected business rules matter, and the escalation gates justify the overhead.

```text
adapter/in -> application port/in -> interactor -> domain + port/out -> adapter/out
```

Implementation checklist:

- Create use-case input model/command.
- Create inbound use-case interface if useful.
- Implement use-case interactor.
- Put invariants into entities/value objects where appropriate.
- Define outbound ports for persistence/external systems.
- Implement adapters outside the core.
- Keep framework annotations out of domain and preferably out of application core.
- Consider rich DDD entities when the feature has meaningful domain behavior.

### Clean Architecture With Rich DDD Model Checklist

Use when Clean Architecture protects a domain model with real behavior.

```text
adapter/in -> use case interactor -> aggregate/entity/value objects -> ports -> adapter/out
```

Implementation checklist:

- Model important concepts as entities, aggregates, or value objects.
- Put invariants inside the model.
- Use intention-revealing methods instead of setters.
- Keep persistence models separate if ORM annotations would pollute the domain.
- Let the interactor orchestrate instead of making all decisions.
- Test domain behavior without framework dependencies.

### CQRS Feature Checklist

Use when reads and writes differ.

```text
commands mutate state
queries read optimized models
```

Implementation checklist:

- Separate command handlers from query handlers.
- Keep command model focused on behavior and invariants.
- Keep query model focused on read efficiency and response shape.
- Decide whether read model is updated synchronously or asynchronously.

### Domain Events Checklist

Use when a domain fact should cause decoupled reactions.

```text
state change -> domain event -> event handlers/reactions
```

Implementation checklist:

- Name event in past tense.
- Publish event only after successful state change.
- Keep event payload stable and meaningful.
- Make handlers idempotent.
- Decide whether processing is sync, async, transactional, or eventually consistent.

## Final Local Refactoring Pass

Before finishing a feature, review the added and changed code.

### Look for Duplication

Check whether the same idea appears in multiple places:

- same business rule
- same validation
- same mapping logic
- same persistence query
- same error handling
- same authorization decision
- same domain concept with different names

### Decide Whether to Refactor

Refactor duplicated code when:

- it represents the same business concept
- it protects the same invariant
- it would likely change for the same reason
- keeping it duplicated would create inconsistent behavior
- extraction improves naming and clarity

Do not refactor duplicated code when:

- the duplication is small and temporary
- the concepts only look similar but may evolve differently
- extraction would create a vague shared utility
- the abstraction would hide important feature-specific behavior
- the refactoring would couple independent vertical slices too early

### Prefer These Refactorings

- Move repeated business rules into a domain object or value object.
- Move repeated orchestration into an application service only if it is truly shared.
- Move repeated mapping into a local mapper inside the feature.
- Move repeated validation into a value object if it has domain meaning.
- Extract a shared concept only when the name is obvious and stable.
- Split large handlers or interactors when they contain multiple use cases.
- Move infrastructure concerns out of application/domain code.
- Prefer fewer abstractions when they do not improve clarity or testability.

## Anti-Patterns to Avoid

- Applying Clean Architecture to every CRUD endpoint.
- Creating interfaces that only have one implementation and no testing or dependency-inversion value.
- Putting business rules into controllers.
- Letting repositories decide business behavior.
- Building god services that contain unrelated use cases.
- Creating domain events for technical notifications without business meaning.
- Using CQRS when read and write models are almost identical.
- Hiding all feature code across many horizontal layers when feature locality matters more.
- Allowing infrastructure types to leak into the domain model.
- Using architecture patterns as branding instead of solving actual coupling/cohesion problems.
- Extracting shared abstractions too early from vertical slices.
- Keeping entities anemic while interactors contain all domain decisions.
- Using setters to bypass invariants in a supposedly rich domain model.
- Choosing Clean Architecture when fewer than two escalation gates are present.

## Review Checklist

Before finishing, verify:

- Is the chosen architecture justified by the feature's complexity?
- Did the implementation start from a vertical slice unless another style was clearly better?
- Are escalation gates stated when choosing a heavier architecture?
- Does the chosen style fit the existing codebase's dominant pattern, or is there a strong reason to deviate?
- Are business rules placed close to the concepts they protect?
- Are controllers/adapters free of core business logic?
- Are infrastructure details kept outside the core where needed?
- Are dependencies pointing in the intended direction?
- Is the feature easy to find and understand?
- Are abstractions useful rather than ceremonial?
- Are tests focused on behavior, not only wiring?
- Has duplicated code been reviewed before finishing?
- Was duplication removed only when the abstraction is meaningful?
- Have trade-offs been stated explicitly?

## Example User Prompt

```text
I want to implement a feature that lets users book a free 15-minute consultation slot.
Use the architecture reference to decide whether this should be Layered, Vertical Slice, Hexagonal, Clean Architecture, DDD, CQRS, or events, and then structure the implementation accordingly.
```

Example decision:

```text
Architecture choice: Vertical Slice with a small rich domain model.

Reason:
- The feature is centered around one use case: booking a slot.
- There is an invariant: a slot must not be double-booked.
- Vertical Slice keeps the use case cohesive and easy to find.
- Full Clean Architecture may be too heavy unless more booking workflows, adapters, or lifecycle rules appear.
- A domain object such as ConsultationSlot can protect booking rules.

Possible structure:
features/bookConsultation/
  BookConsultationController
  BookConsultationRequest
  BookConsultationHandler
  BookConsultationResult
  ConsultationSlot
  ConsultationSlotId
  TimeRange
  EmailAddress
  ConsultationSlotRepository

Refactoring check:
- Check whether booking rules, email validation, time-slot validation, or mapping logic are duplicated elsewhere.
- Refactor only if the duplication represents the same stable concept.
```

If the same feature later needs multiple adapters, external calendar integration, complex lifecycle rules, or
asynchronous reactions, evolve toward Hexagonal or Clean Architecture and consider domain events such as
`ConsultationBooked`.
