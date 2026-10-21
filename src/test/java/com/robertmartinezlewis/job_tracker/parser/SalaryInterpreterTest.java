package com.robertmartinezlewis.job_tracker.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalaryInterpreterTest {

    @ParameterizedTest(name = "\"{0}\" -> min={1}, max={2}")
    @MethodSource("salaryCases")
    void interpretSalary_parseRange(String input, Integer expectedMin, Integer expectedMax) {
        InterpretedSalary result = SalaryInterpreter.interpretSalary(input);

        assertEquals(expectedMin, result.min());
        assertEquals(expectedMax, result.max());
    }

    static Stream<Arguments> salaryCases() {
        return Stream.of(
                Arguments.of("30000 € - 45000 € Bruto/año", 30000, 45000),
                Arguments.of("Sin especificar", null, null),
                Arguments.of("0 € - 45000 € Bruto/año", null, 45000),
                Arguments.of("50000 € - 30000 € Bruto/año", 30000, 50000),
                Arguments.of("45000 € - 0 € Bruto/año", 45000, null),
                Arguments.of("30000 € - 999999999999 € Bruto/año", 30000, null),
                Arguments.of("999999999999 € - 30000 € Bruto/año", null, 30000),
                Arguments.of("999999999999 € - 999999999999 € Bruto/año", null, null),
                Arguments.of("000000000005 € - 6 € Bruto/año", 5, 6),
                Arguments.of("-45 € - 50 € Bruto/año", null, 50),
                Arguments.of("45 € - -50 € Bruto/año", 45, null),
                Arguments.of("-45 € - -50 € Bruto/año", null, null),
                Arguments.of("-45 € - 0 € Bruto/año", null, null),
                Arguments.of("50 € - -45 € Bruto/año", 50, null),
                Arguments.of("50 € -45 € Bruto/año", 45, 50),
                Arguments.of("-5 € - -3 € Bruto/año", null, null),
                Arguments.of(null, null, null)
        );
    }

}


