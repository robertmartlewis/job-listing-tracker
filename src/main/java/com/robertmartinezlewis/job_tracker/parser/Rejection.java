package com.robertmartinezlewis.job_tracker.parser;

import java.util.Set;

public record Rejection(
        Set<RejectionReason> reasons
) {

}
