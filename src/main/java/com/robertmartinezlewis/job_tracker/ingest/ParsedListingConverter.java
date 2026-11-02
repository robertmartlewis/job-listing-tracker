package com.robertmartinezlewis.job_tracker.ingest;

import com.robertmartinezlewis.job_tracker.JobListing;
import com.robertmartinezlewis.job_tracker.parser.ParsedListing;

import java.time.Clock;

public class ParsedListingConverter {

    private final Clock clock;

    public ParsedListingConverter(Clock clock) {
        this.clock = clock;
    }

    public JobListing toJobListing(ParsedListing parsedListing) {
        JobListing jobListing = new JobListing();
        jobListing.setFirstSeenDate(clock.instant());
        return jobListing;
    }
}
