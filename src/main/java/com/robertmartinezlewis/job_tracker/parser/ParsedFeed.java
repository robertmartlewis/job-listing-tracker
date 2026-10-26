package com.robertmartinezlewis.job_tracker.parser;

import java.util.List;

public record ParsedFeed(
        List<ParsedListing> listings
) {
}
