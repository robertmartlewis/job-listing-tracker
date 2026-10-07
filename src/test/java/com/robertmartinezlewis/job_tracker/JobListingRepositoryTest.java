package com.robertmartinezlewis.job_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobListingRepositoryTest {

    // a way to get the JobListingRepository into this class
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

        JobListing saved = repository.save(jobListing);
        manager.flush();
        manager.clear();
        Optional<JobListing> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("jr dev");
        assertThat(found.get().getUrl()).isEqualTo("https://somewhere/jrdev.com");
        assertThat(found.get().getSource()).isEqualTo(Source.INFOJOBS);
        assertThat(found.get().getStatus()).isEqualTo(Status.NEW);
    }
}