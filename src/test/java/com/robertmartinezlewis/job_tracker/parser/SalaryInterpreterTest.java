package com.robertmartinezlewis.job_tracker.parser;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Provide;
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
                Arguments.of("50000 € - 30000 € Bruto/año", 50000, 30000),
                Arguments.of("45000 € - 0 € Bruto/año", 45000, null),
                Arguments.of(null, null, null)
        );
    }

}


