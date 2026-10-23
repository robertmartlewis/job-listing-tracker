package com.robertmartinezlewis.job_tracker.parser;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExperienceInterpreterPropertiesTest {

    @Provide
    Arbitrary<String> oversizedExperience() {
        return Arbitraries.strings().numeric().ofMinLength(1).ofMaxLength(30);
    }

    @Property
    void singularYearNeverThorwsForHugeNumbers(@ForAll("oversizedExperience") String digits) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(digits + " año");
        assertNotNull(result);
    }

    @Property
    void pluralYearNeverThorwsForHugeNumbers(@ForAll("oversizedExperience") String digits) {
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

}
