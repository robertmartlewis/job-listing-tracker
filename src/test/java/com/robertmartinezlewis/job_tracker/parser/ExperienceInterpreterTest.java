package com.robertmartinezlewis.job_tracker.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExperienceInterpreterTest {

    @ParameterizedTest(name = "\"{0}\" -> min={1}, max={2}")
    @MethodSource("experienceCases")
    void interpretExperience_parseRange(String input, Integer expectedMin, Integer expectedMax) {
        InterpretedExperience result = ExperienceInterpreter.interpretExperience(input);

        assertEquals(expectedMin, result.min());
        assertEquals(expectedMax, result.max());
    }

    static Stream<Arguments> experienceCases() {
        return Stream.of(
                Arguments.of("No requerida", 0, null),
                Arguments.of("1 año", 1, null),
                Arguments.of("3 años", 3, null),
                Arguments.of("3-5 años", 3, 5),
                Arguments.of("Más de 5 años", 5, null),
                Arguments.of(null, null, null)
        );
    }
}
