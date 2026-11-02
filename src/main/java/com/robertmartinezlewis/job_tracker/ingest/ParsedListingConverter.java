package com.robertmartinezlewis.job_tracker.ingest;

import com.robertmartinezlewis.job_tracker.JobListing;
import com.robertmartinezlewis.job_tracker.parser.ParsedListing;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

public class ParsedListingConverter {

    private static final ZoneId MADRID = ZoneId.of("Europe/Madrid");

    private final Clock clock;

    public ParsedListingConverter(Clock clock) {
        this.clock = clock;
    }

    public JobListing toJobListing(ParsedListing parsedListing) {
        JobListing jobListing = new JobListing();
        jobListing.setFirstSeenDate(clock.instant());
        jobListing.setPostedDate(LocalDate.ofInstant(parsedListing.publishedAt(), MADRID));
        return jobListing;
    }
}
