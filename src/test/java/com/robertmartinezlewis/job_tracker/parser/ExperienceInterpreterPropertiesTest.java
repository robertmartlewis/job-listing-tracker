package com.robertmartinezlewis.job_tracker.parser;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.WithNull;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExperienceInterpreterPropertiesTest {

    @Provide
    Arbitrary<String> oversizedExperience() {
        return Arbitraries.strings().numeric().ofMinLength(1).ofMaxLength(30);
    }

    @Property
    void singularYearNeverThrowsForHugeNumbers(@ForAll("oversizedExperience") String digits) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(digits + " año");
        assertNotNull(result);
    }

    @Property
    void pluralYearNeverThrowsForHugeNumbers(@ForAll("oversizedExperience") String digits) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(digits + " años");
        assertNotNull(result);
    }

    @Property
    void yearRangeNeverThrowsForHugeNumbers(
            @ForAll("oversizedExperience") String first,
            @ForAll("oversizedExperience") String second) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(first + "-" + second + " años");
        assertNotNull(result);
    }

    @Property
    void experienceLessThanNeverThrowsForHugeNumbers(@ForAll("oversizedExperience") String digits) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience("Menos de " + digits + " años");
        assertNotNull(result);
    }

    @Property
    void experienceRangeMinIsNotGreaterThanMax(
            @ForAll @IntRange(min = 0, max = 100) int first,
            @ForAll @IntRange(min = 0, max = 100) int second) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(first + "-" + second + " años");
        Assume.that(result.min() != null && result.max() != null);
        assertTrue(result.min() <= result.max(), "The experience is: "
                +  result.min() + "años - " + result.max() + "años");
    }

    @Property
    void neverThrowsOrReturnsNull(@ForAll @WithNull String input) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(input);
        assertNotNull(result);
    }

}
