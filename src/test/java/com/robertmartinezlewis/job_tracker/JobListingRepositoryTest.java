package com.robertmartinezlewis.job_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobListingRepositoryTest {

    @Autowired
    private JobListingRepository repository;

    @Autowired
    private TestEntityManager manager;

    @Test
    void savedListingCanBeReadBack() {

        JobListing jobListing = new JobListing();
        jobListing.setTitle("jr dev");
        jobListing.setUrl("https://somewhere/jrdev.com");
        jobListing.setSource(Source.INFOJOBS);
        jobListing.setCompany("Empresa01");
        jobListing.setDescription("descripcion");
        jobListing.setSalaryMin(1000);
        jobListing.setSalaryMax(1200);
        jobListing.setPostedDate(LocalDate.of(2026, 10, 3));
        Instant seen = Instant.now();
        jobListing.setFirstSeenDate(seen);

        JobListing saved = repository.save(jobListing);
        manager.flush();
        manager.clear();
        Optional<JobListing> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("jr dev");
        assertThat(found.get().getUrl()).isEqualTo("https://somewhere/jrdev.com");
        assertThat(found.get().getSource()).isEqualTo(Source.INFOJOBS);
        assertThat(found.get().getStatus()).isEqualTo(Status.NEW);
        assertThat(found.get().getCompany()).isEqualTo("Empresa01");
        assertThat(found.get().getDescription()).isEqualTo("descripcion");
        assertThat(found.get().getSalaryMin()).isEqualTo(1000);
        assertThat(found.get().getSalaryMax()).isEqualTo(1200);
        assertThat(found.get().getPostedDate()).isEqualTo(LocalDate.of(2026, 10, 3));
        assertThat(found.get().getFirstSeenDate()).isCloseTo(seen, within(1, ChronoUnit.MILLIS));
    }
}