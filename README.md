# Slovak Regional Referendum Portal

A Java desktop application for creating, managing, participating in, and evaluating regional referendums in Slovakia.

The application combines a **JavaFX** user interface with **Spring Boot**, **Spring Data JPA**, and an in-memory **H2 database**. It demonstrates object-oriented design through user and referendum hierarchies, polymorphic vote weighting, multiple referendum-evaluation strategies, notifications, custom exceptions, and design patterns.

The project was created as a university assignment at the Faculty of Informatics and Information Technologies, Slovak University of Technology in Bratislava.

## Features

- Sign-in screen with predefined demonstration accounts
- Interactive map of all eight Slovak regions
- Regional descriptions and demographic information
- Region-specific referendum browsing
- Simple, quota-based, and conditional referendums
- Referendums containing one or more yes/no questions
- Voting permissions based on the user type
- Prevention of duplicate participation
- Weighted voting for local and travelling voters
- Referendum likes and notification subscriptions
- Administrator-only referendum creation and evaluation
- Result states: successful, failed, or invalid
- In-memory persistence through Spring Data JPA and H2
- JavaFX interface defined with FXML and CSS
- Generated Javadoc documentation

## Repository structure

```text
.
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   └── documentation/
│   └── test/
├── Dokumentacia.pdf
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Application roles

The application uses an inheritance hierarchy rooted in `AppUser`.

### Administrator

An administrator can:

- browse regions and referendums,
- create new referendums,
- choose the referendum type,
- define questions and evaluation conditions,
- evaluate completed referendums,
- trigger result notifications for subscribed users.

### Viewer

A viewer can:

- browse the regional referendum portal,
- inspect referendum details,
- like or unlike referendums,
- subscribe to notifications about evaluation results,
- view received notifications.

A viewer cannot vote.

### Local voter

A local voter is associated with one home region and can participate in referendums.

For weighted referendum types:

- a vote in the voter's home region has weight `1.0`,
- a vote outside the home region has weight `0.25`.

### Travelling voter

A travelling voter is associated with multiple supported regions.

For weighted referendum types:

- a vote in one of the associated regions has weight `0.5`,
- a vote in another region has weight `0.25`.

For simple referendums, every vote has weight `1.0`, regardless of voter type.

## Referendum types

The application contains a referendum inheritance hierarchy with three concrete types.

### Simple referendum

A simple referendum is always considered valid. It succeeds when the weighted ratio of positive votes exceeds the configured pass threshold.

### Quota referendum

A quota referendum extends the simple referendum with a minimum-participation requirement.

It is valid only when the number of participating voters reaches the configured minimum. A valid referendum succeeds when the positive-vote ratio exceeds the pass threshold.

### Conditional referendum

A conditional referendum extends the quota referendum with a local-participation requirement.

It is valid only when:

- the minimum total participation is reached, and
- the ratio of local voters reaches the configured local-vote quota.

A valid referendum succeeds when the positive-vote ratio exceeds the pass threshold.

## Evaluation results

A referendum evaluation produces one of three states:

| Result | Meaning |
| --- | --- |
| `SUCCESSFUL` | The referendum is valid and meets the pass threshold |
| `FAILED` | The referendum is valid but does not meet the pass threshold |
| `INVALID` | Participation or local-voter conditions are not satisfied |

The evaluation process also stores vote counts and the referendum result in the database.

## Design and architecture

The project separates responsibilities into several layers:

```text
JavaFX views and controllers
            ↓
        Services
            ↓
 Spring Data repositories
            ↓
     JPA domain model
            ↓
      H2 database
```

### Domain model

Important entities include:

- `AppUser`
- `Admin`
- `Viewer`
- `Voter`
- `LocalVoter`
- `TravelVoter`
- `Region`
- `Referendum`
- `SimpleReferendum`
- `QuotaReferendum`
- `ConditionalReferendum`
- `Question`
- `ReferendumForm`
- `ReferendumResult`
- `Notification`

### Service layer

The service layer contains the application logic for:

- authentication,
- liked and voted referendum management,
- region retrieval,
- referendum creation,
- vote-form persistence,
- referendum evaluation,
- notifications.

### Repository layer

Spring Data repositories provide persistence operations for:

- users,
- regions,
- referendums,
- referendum forms,
- referendum results,
- notifications.

## Object-oriented concepts

The project demonstrates several object-oriented principles.

### Inheritance

Two main inheritance hierarchies are used:

```text
AppUser
├── Admin
└── Viewer
    └── Voter
        ├── LocalVoter
        └── TravelVoter
```

```text
Referendum
└── SimpleReferendum
    └── QuotaReferendum
        └── ConditionalReferendum
```

### Polymorphism

Polymorphism is used for:

- calculating vote weight through `Voter.getVoteWeight`,
- validating different referendum types,
- determining whether a referendum is successful.

### Encapsulation and generics

`AbstractReferendumEvaluator<T>` provides a generic evaluation workflow and contains an encapsulated nested `InternalResult` class.

### Aggregation and composition

Examples include:

- users associated with regions,
- referendums associated with regions,
- questions contained within referendums,
- liked and voted referendum collections.

## Design patterns

### Template Method

`AbstractReferendumEvaluator` defines the common evaluation algorithm. Concrete evaluators override referendum-specific validity and success checks:

- `SimpleReferendumEvaluator`
- `QuotaReferendumEvaluator`
- `ConditionalReferendumEvaluator`

### Observer

`NotificationObserver` manages subscriptions to individual referendums. Users who liked a referendum can receive a notification after it is evaluated.

### Aspect-oriented notification trigger

`EvaluateAspect` intercepts successful returns from `ReferendumService.evaluate(...)` and invokes the notification observer.

## Technologies

- Java 21
- JavaFX 21
- Spring Boot 3.2.4
- Spring Data JPA
- H2 Database
- Maven Wrapper
- FXML
- CSSFX
- MaterialFX
- ControlsFX
- FormsFX
- BootstrapFX
- JUnit 5

## Requirements

- JDK 21
- Internet access during the first Maven build so dependencies can be downloaded
- A graphical desktop environment capable of running JavaFX

No external database server is required. The application uses an in-memory H2 database.

## Running the application

### Linux and macOS

Make the Maven wrapper executable:

```bash
chmod +x mvnw
```

Start the application with the explicit main class:

```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.main-class=com.oop.referendumserver.Application
```

### Windows

```powershell
mvnw.cmd spring-boot:run -Dspring-boot.run.main-class=com.oop.referendumserver.Application
```

The first build downloads the Maven dependencies and may take several minutes.

### IntelliJ IDEA

1. Open the directory as a Maven project.
2. Select JDK 21 as the project SDK.
3. Allow Maven to download the dependencies.
4. Run:

```text
com.oop.referendumserver.Application
```

## Demonstration accounts

The in-memory database is populated with several sample users at startup.

| Username | Password | Role | Region access |
| --- | --- | --- | --- |
| `admin` | `admin` | Administrator | Administrative functions |
| `dominik` | `heslo` | Viewer | Browsing and likes |
| `ema` | `heslo` | Local voter | Trnava region |
| `linda` | `heslo` | Local voter | Trnava region |
| `Peter` | `heslo` | Travelling voter | Bratislava and Trnava regions |

> [!WARNING]
> These accounts use plaintext demonstration passwords and are suitable only for local educational testing.

## Initial data

At application startup, `DatabaseLoader` initializes:

- all eight Slovak regions,
- sample users,
- sample referendums,
- region descriptions from `data.yml`.

Because the H2 database is configured in memory, all runtime changes are lost when the application is closed.

## User workflow

### Browse a referendum

1. Sign in.
2. Select a region on the interactive map.
3. Open an active referendum.
4. View its title, description, date, questions, and conditions.

### Participate in a referendum

1. Sign in as a voter.
2. Select a referendum.
3. Answer every question with yes or no.
4. Submit the form.

A voter cannot submit a second form for the same referendum.

### Follow a referendum

1. Sign in as a viewer or voter.
2. Like the referendum.
3. The notification observer subscribes the user to its result.
4. After administrator evaluation, a notification is created for the subscriber.

### Create and evaluate a referendum

1. Sign in as `admin`.
2. Select a region.
3. Create a referendum and choose its type.
4. Enter its title, description, questions, date, and required thresholds.
5. Evaluate the referendum after votes have been submitted.

## Documentation

The repository contains two forms of documentation:

- [`Dokumentacia.pdf`](Dokumentacia.pdf) - Slovak-language project report with the functional description and class diagrams.
- [`src/main/documentation/index.html`](src/main/documentation/index.html) - generated Javadoc documentation.

## Testing

The project contains a basic Spring context test:

```bash
./mvnw test
```

A full automated test suite for voting, evaluation, and user-interface workflows is not included.

## Known limitations

- Passwords are stored and compared as plaintext.
- The H2 database is in memory and is reset on every application restart.
- Authentication is implemented as application logic rather than with Spring Security.
- The application is a desktop JavaFX client despite the original artifact name `referendum-server`.
- The `javafx-maven-plugin` configuration in the original `pom.xml` references an outdated, nonexistent main class. Use the explicit `spring-boot:run` command shown above or update the plugin's `mainClass` to `com.oop.referendumserver.Application`.
- The application contains hard-coded demonstration users and referendums.
- Some visible interface text and sample data are not fully localized or grammatically normalized.
- The H2 console property is enabled, but the Spring application runs in non-web mode.
- Notification subscriptions are stored in application memory.
- The project has only a minimal automated test.
- The application has not been prepared for production deployment.

## Possible improvements

- Store passwords using a secure password-hashing algorithm.
- Add Spring Security and role-based authorization.
- Replace the in-memory database with PostgreSQL or another persistent database.
- Add registration and account-management workflows.
- Persist notification subscriptions.
- Add referendum closing dates and lifecycle states.
- Add validation for all administrator inputs.
- Add database migrations with Flyway or Liquibase.
- Add unit, repository, service, and JavaFX integration tests.
- Correct the JavaFX Maven plugin configuration.
- Add CI builds and static analysis.
- Add application screenshots to the repository.
- Improve localization and accessibility.
- Package the desktop application with `jlink` or `jpackage`.

## Author

**Dominik Jurkas**

Faculty of Informatics and Information Technologies  
Slovak University of Technology in Bratislava
