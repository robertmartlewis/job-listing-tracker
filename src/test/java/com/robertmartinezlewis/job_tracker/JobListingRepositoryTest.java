package com.robertmartinezlewis.job_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
        jobListing.setDescription("x".repeat(5500));
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
        assertThat(found.get().getDescription()).isEqualTo("x".repeat(5500));
        assertThat(found.get().getSalaryMin()).isEqualTo(1000);
        assertThat(found.get().getSalaryMax()).isEqualTo(1200);
        assertThat(found.get().getPostedDate()).isEqualTo(LocalDate.of(2026, 10, 3));
        assertThat(found.get().getFirstSeenDate()).isCloseTo(seen, within(1, ChronoUnit.MILLIS));
    }

    @Test
    void savingTwoListingsWithTheSameUrlIsRejected() {

        JobListing jobListing = new JobListing();
        jobListing.setTitle("jr dev");
        jobListing.setUrl("https://somewhere/jrdev.com");
        jobListing.setSource(Source.INFOJOBS);
        jobListing.setCompany("Empresa01");
        jobListing.setDescription("x".repeat(5500));
        jobListing.setSalaryMin(1000);
        jobListing.setSalaryMax(1200);
        jobListing.setPostedDate(LocalDate.of(2026, 10, 3));
        Instant seen = Instant.now();
        jobListing.setFirstSeenDate(seen);

        JobListing jobListingDuplicate = new JobListing();
        jobListingDuplicate.setTitle("jr dev 01");
        jobListingDuplicate.setUrl("https://somewhere/jrdev.com");
        jobListingDuplicate.setSource(Source.INFOJOBS);
        jobListingDuplicate.setCompany("Empresa02");
        jobListingDuplicate.setDescription("x".repeat(5500));
        jobListingDuplicate.setSalaryMin(1200);
        jobListingDuplicate.setSalaryMax(1400);
        jobListingDuplicate.setPostedDate(LocalDate.of(2026, 10, 3));
        Instant seenDup = Instant.now();
        jobListingDuplicate.setFirstSeenDate(seenDup);

        repository.save(jobListing);

        assertThatThrownBy(() -> repository.save(jobListingDuplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void savingTwoListingsWithTheUniqueUrls() {

        JobListing jobListing1 = new JobListing();
        jobListing1.setTitle("jr dev 01");
        jobListing1.setUrl("https://somewhere/01.com");
        jobListing1.setSource(Source.INFOJOBS);
        jobListing1.setCompany("Empresa01");
        jobListing1.setDescription("x".repeat(5500));
        jobListing1.setSalaryMin(1000);
        jobListing1.setSalaryMax(1200);
        jobListing1.setPostedDate(LocalDate.of(2026, 10, 3));
        jobListing1.setFirstSeenDate(Instant.now());

        JobListing jobListing2 = new JobListing();
        jobListing2.setTitle("jr dev 02");
        jobListing2.setUrl("https://somewhere/jrdev02.com");
        jobListing2.setSource(Source.INFOJOBS);
        jobListing2.setCompany("Empresa02");
        jobListing2.setDescription("x".repeat(5500));
        jobListing2.setSalaryMin(1200);
        jobListing2.setSalaryMax(1400);
        jobListing2.setPostedDate(LocalDate.of(2026, 10, 3));
        jobListing2.setFirstSeenDate(Instant.now());

        JobListing saved1 = repository.save(jobListing1);
        JobListing saved2 = repository.save(jobListing2);
        manager.flush();
        manager.clear();

        assertThat(saved1.getId()).isNotNull();
        assertThat(saved2.getId()).isNotNull();
        assertThat(saved1.getId()).isNotEqualTo(saved2.getId());

        assertThat(repository.findById(saved1.getId())).isPresent();
        assertThat(repository.findById(saved2.getId())).isPresent();
    }
}