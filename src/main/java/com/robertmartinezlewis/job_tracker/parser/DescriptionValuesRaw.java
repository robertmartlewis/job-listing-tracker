package com.robertmartinezlewis.job_tracker.parser;

/**
 *Text of each labelled <b> field in a Tecnoempleo descirption, uninterpreted.
 * A field can be null when it's label is absent or is value is blank
 */
public record DescriptionValuesRaw(
        String company,
        String province,
        String town,
        String description,
        String technologies,
        String contractType,
        String salary,
        String experience,
        String functions,
        String minimumEducation
) {
}
