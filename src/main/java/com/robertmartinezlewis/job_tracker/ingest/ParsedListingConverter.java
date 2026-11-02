package com.robertmartinezlewis.job_tracker.ingest;

import com.robertmartinezlewis.job_tracker.JobListing;
import com.robertmartinezlewis.job_tracker.Source;
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

    public JobListing toJobListing(ParsedListing parsedListing, Source source) {
        JobListing jobListing = new JobListing();
        jobListing.setFirstSeenDate(clock.instant());
        jobListing.setPostedDate(LocalDate.ofInstant(parsedListing.publishedAt(), MADRID));
        jobListing.setSource(source);
        jobListing.setRfId(parsedListing.rfId());
        jobListing.setUrl(parsedListing.url());
        jobListing.setTitle(parsedListing.title());
        jobListing.setCompany(parsedListing.company());
        jobListing.setDescription(parsedListing.description());
        jobListing.setProvince(parsedListing.province());
        jobListing.setModality(parsedListing.modality());
        jobListing.setTechnologies(parsedListing.technologies());
        return jobListing;
    }
}
