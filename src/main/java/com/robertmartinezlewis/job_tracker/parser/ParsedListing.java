package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.Modality;
import com.robertmartinezlewis.job_tracker.SpanishProvinces;

import java.time.Instant;

public record ParsedListing(
        String rfId,
        String url,
        String title,
        String company,
        String description,
        Instant publishedAt,
        String province,
        Modality modality,
        String technologies,
        String salaryText,
        Integer salaryMin,
        Integer salaryMax,
        String experienceText,
        Integer experienceMin,
        Integer experienceMax
) {
    public ParsedListing {
        if (province != null && !SpanishProvinces.PROVINCES.contains(province)) {
            throw new IllegalArgumentException("Invalid province: \"" + province + "\"");
        }
    }
}
