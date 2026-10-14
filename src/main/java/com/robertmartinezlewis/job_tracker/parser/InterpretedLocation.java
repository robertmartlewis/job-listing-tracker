package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.Modality;

public record InterpretedLocation(
        Modality modality,
        String province
) {
}
