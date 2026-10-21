package com.robertmartinezlewis.job_tracker.parser;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalaryInterpreterPropertiesTest {
    @Provide
    Arbitrary<String> salaryRanges() {
        Arbitrary<Integer> first = Arbitraries.integers().between(0, 1000000);
        Arbitrary<Integer> second = Arbitraries.integers().between(0, 1000000);
        return Combinators.combine(first, second).as((a, b) -> a + "€ - " + b + " €");
    }

    @Property
    void minIsNotGreaterThanMax(@ForAll("salaryRanges") String input) {
        InterpretedSalary result = SalaryInterpreter.interpretSalary(input);
        Assume.that(result.min() != null && result.max() != null);
        assertTrue(result.min() <= result.max(), "El salario tiene formato correcto");
    }
}
