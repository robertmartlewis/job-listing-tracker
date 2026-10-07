# Job Listing Tracker

A Spring Boot application that automatically tracks junior Java job listings for Asturias, Spain — pulling from Tecnoempleo's RSS feed, storing them with deduplication, and providing a searchable/filterable dashboard.

🚧 **Status: in active development.** Built as a learning project and CV centerpiece — see commit history for progress.

## Tech stack

**In place**

- Java 25 + Spring Boot 4
- Spring Data JPA + PostgreSQL 17 (Docker)
- JUnit 5 + AssertJ, `@DataJpaTest` against a real Postgres

**Planned**

- Jsoup / Rome (RSS parsing)
- Thymeleaf + Bootstrap 5
- Mockito, jqwik, Testcontainers
- Docker + GitHub Actions (CI)
- Deployed on Render

## Roadmap

- [x] Data model + persistence layer
- [ ] Job ingestion from Tecnoempleo RSS feed + deduplication
- [ ] Dashboard: search, filters, status tracking
- [ ] Test suite (unit, property-based, integration)
- [ ] CI/CD + live deployment

## Setup

**Prerequisites:** JDK 25, Maven (or the included `mvnw`), Docker.

### 1. Start PostgreSQL

```bash
docker run --name jobtracker-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=<choose-a-local-password> \
  -e POSTGRES_DB=jobtracker \
  -p 5432:5432 \
  -v jobtracker-data:/var/lib/postgresql/data \
  -d postgres:17
```

If port 5432 is already in use, change the left side of `-p` (for example `5433:5432`) and update the port in `src/main/resources/application.properties` to match.

### 2. Set the password variable

The app reads the database password from the `DB_PASSWORD` environment variable, so no credentials live in the repository. Use the same value you chose above.

PowerShell:

```powershell
$env:DB_PASSWORD = "<choose-a-local-password>"
```

bash / zsh:

```bash
export DB_PASSWORD=<choose-a-local-password>
```

The variable only exists in the terminal where you set it. If you run from an IDE, add it to the run configuration too.

### 3. Run the tests

```bash
mvn verify
```

The tests run against the real database from step 1, so the container must be running.

### 4. Run the application

```bash
mvn spring-boot:run
```

On startup Hibernate creates or updates the `job_listing` table (`ddl-auto=update`, a development-only setting — see below).

## Design decisions

- **Deduplication lives in the database.** `url` has a `UNIQUE` constraint, so no code path can insert a duplicate. A test proves the second insert is rejected, and another proves different urls both save.
- **Salary is stored twice on purpose:** the raw scraped text (safe to display, whatever the format) plus parsed `salaryMin` / `salaryMax` integers (nullable) for filtering and sorting.
- **Enums are stored as strings** (`EnumType.STRING`), so reordering or adding constants cannot silently corrupt existing rows.
- **Tests use a real PostgreSQL, not an in-memory substitute,** because the deduplication guarantee depends on the actual engine's constraint behaviour. Each test runs in a transaction that rolls back. The read-back is forced through `flush()` and `clear()` so it hits the database rather than Hibernate's first-level cache.
- **`firstSeenDate` is set by the caller,** not defaulted inside the entity, so the ingestion code controls it and tests stay deterministic.

## Known limitations

- `spring.jpa.hibernate.ddl-auto=update` is a learning setting. It only adds tables and columns and never alters existing ones. Before deployment it will be replaced with `validate` plus proper migrations.
- Tests currently need a locally running database. Testcontainers will replace that when CI is added.