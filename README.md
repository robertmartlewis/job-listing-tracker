# Job Listing Tracker

A Spring Boot application that tracks junior Java job listings from Tecnoempleo's RSS feed (all of Spain): it parses them, stores them with database-enforced deduplication, and will serve a searchable, filterable dashboard.

**Status: in active development.** The parser and the persistence layer are done and tested; ingestion and the dashboard are next (see [Status](#status)). Built as a learning project and a real tool for my own internship search.

## What this project shows

- **Strict test-driven development.** Every behaviour starts as a red test with exact expected values, gets the smallest change that turns it green, and is then *sabotaged*: I break the code on purpose to check that the test fails for the right reason (see [How it is tested](#how-it-is-tested)).
- **Invariants enforced by the database, not by hope.** Uniqueness and `NOT NULL` live in versioned Flyway migrations, and each one has a test that fails when the constraint is removed.
- **Responsible data access.** The source site's `robots.txt` disallows the RSS endpoint, so I asked the site for permission before building on it (see [Data source and permission](#data-source-and-permission)).
- **Clean history.** Conventional Commits with a scope, one branch and one pull request per step, merge commits instead of squashing so the red/green/refactor steps stay visible.

## Status

| Area | State |
| --- | --- |
| RSS parser (Rome + Jsoup) | Done. Classifies every feed item as a valid listing or a rejection with reasons and position |
| Salary, experience and location interpreters | Done, with example-based and property-based tests |
| Persistence (`JobListing`, Spring Data JPA, PostgreSQL 17) | Done |
| Schema migrations (Flyway) | Done: `V1` table, `V2` stable `rf_id`, `V3` `province` |
| Remaining columns (`experienceMin`, `experienceMax`, `experienceText`, `technologies`) | In progress |
| Ingestion (parsed listing to stored listing, console command) | Next |
| Dashboard (search, filters, status tracking) | Planned |
| CI and deployment | Planned |

## Tech stack

- Java 25, Spring Boot 4, Maven
- Spring Data JPA, PostgreSQL 17 (Docker), Flyway
- Rome (RSS) and Jsoup (HTML descriptions)
- JUnit, AssertJ, jqwik (property-based tests), `@DataJpaTest` against a real PostgreSQL
- Planned: Thymeleaf and Bootstrap 5, Testcontainers, GitHub Actions

## Data source and permission

Tecnoempleo's `robots.txt` disallows the RSS endpoint (`/alertas-empleo-rss.php`). Instead of assuming it was fine, I emailed Tecnoempleo to ask. They replied on 28 October 2026 allowing the use of the feed as long as it is used responsibly.

The ingestion is designed around that condition:

- It runs as a **manual console command**, not a background crawler, and at most **once every 24 hours**.
- It identifies itself with a proper `User-Agent`.
- Data stays **local and personal**: nothing is republished, and each listing keeps its link back to the original page.
- Fetching the live feed is **off by default**. The code that obtains the feed text is separate from the parser, so every test runs on saved sample files and never touches the network.

This is a personal project and not a statement about anyone else's right to scrape the site.

## How it is tested

Each cycle is: red test with exact values, minimal green, then a sabotage per branch. I predict the result before every run.

The sabotage step found real gaps that a green test run would have hidden. For the database constraints it looks like this:

| Constraint (migration) | Sabotage | What must fail |
| --- | --- | --- |
| `url` unique (`V1`) | Remove the constraint | Saving two listings with the same url |
| `rf_id` unique (`V2`) | Remove the constraint | Saving two listings with the same `rf_id` |
| `rf_id` not null (`V2`) | Remove `NOT NULL` | The plain-SQL insert test. The entity-level test stays green, because Hibernate rejects the null before it reaches the database |
| `province` nullable (`V3`) | Add `NOT NULL` | Every test that saves a listing without a province |
| `province` stored | Comment out the setter assignment | The read-back test |

Two lessons from this:

- **`ddl-auto=validate` does not check `UNIQUE` or `NOT NULL`.** It compares tables, columns and types only. Only the repository tests caught a missing `UNIQUE`, so schema validation and repository tests are complementary.
- **A repository test cannot prove a `NOT NULL` column constraint on its own.** Hibernate checks `nullable = false` in Java before sending any SQL. The database-level test inserts with plain SQL through `JdbcTemplate`, bypassing Hibernate.

The property-based tests (jqwik) cover the interpreters, where the input space is too large for hand-picked examples.

## Design decisions

- **The parser classifies and never discards silently.** `parseRss` returns the valid listings plus a list of rejections, each with the set of missing fields and the item position. Rejected items are reported, not dropped.
- **Unknown means `null`.** A salary of "0 €", an open-ended experience range or an unrecognised province becomes `null`, never a sentinel number.
- **Deduplication lives in the database.** `url` and `rf_id` are both `UNIQUE`. The `rf_id` is the stable id Tecnoempleo puts at the end of each listing url, extracted with a strict pattern. `url` is kept as defence in depth.
- **Schema changes go through Flyway, one migration per column.** Hibernate only validates the schema at startup (`ddl-auto=validate`) and never alters it.
- **Raw and parsed values are both stored where parsing can lose information.** Salary keeps the original text plus nullable min and max. Experience will do the same.
- **Enums are stored as strings**, so reordering constants cannot corrupt existing rows.
- **Tests use a real PostgreSQL**, because the deduplication guarantee depends on the actual engine's constraint behaviour. Each test rolls back, and read-backs go through `flush()` and `clear()` so they hit the database and not the first-level cache.

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

If port 5432 is already in use, change the left side of `-p` (for example `5433:5432`) and update the port in `src/main/resources/application.properties`.

### 2. Set the password variable

The app reads the database password from `DB_PASSWORD`, so no credentials live in the repository. Use the value you chose above.

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
./mvnw test
```

The tests run against the database from step 1, so the container must be running. Flyway creates the schema on the first run.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

## What I would do next

1. Finish the remaining columns (experience range and raw text, technologies), one migration and one test cycle each.
2. Ingestion: convert parsed listings to stored ones (setting status, source and first-seen date), handle values that do not fit a column before saving so one bad item cannot abort a whole batch, and add the once-per-24-hours console command.
3. Dashboard with Thymeleaf: search, filters by province, modality, salary and experience, and status tracking (new, interested, applied, rejected).
4. Replace the locally running database with Testcontainers and add a GitHub Actions workflow that runs the tests on every push.
5. Deploy, keeping in mind that a scheduled job and a free tier that spins down do not mix.

## Known limitations

- The tests need a locally running database until Testcontainers replaces it.
- `province` is limited to 40 characters. A longer value makes the insert fail, so ingestion must check it first.
- The province list the parser recognises is a known list of Spanish provinces and grows test by test. Anything else is stored as unknown (`null`).