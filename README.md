# Job Listing Tracker

A Spring Boot application that automatically tracks junior Java job listings for Asturias, Spain — pulling from Tecnoempleo's RSS feed, storing them with deduplication, and providing a searchable/filterable dashboard.

🚧 **Status: in active development.** Built as a learning project and CV centerpiece — see commit history for progress.

## Planned tech stack

- Java + Spring Boot
- Spring Data JPA + PostgreSQL
- Jsoup / Rome (RSS parsing)
- Thymeleaf + Bootstrap 5
- JUnit 5, Mockito, jqwik, Testcontainers
- Docker + GitHub Actions (CI)
- Deployed on Render

## Roadmap

- [ ] Data model + persistence layer
- [ ] Job ingestion from Tecnoempleo RSS feed + deduplication
- [ ] Dashboard: search, filters, status tracking
- [ ] Test suite (unit, property-based, integration)
- [ ] CI/CD + live deployment

## Setup

_Coming soon — instructions for running locally will be added once the persistence layer is in place._