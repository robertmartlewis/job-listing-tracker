package com.robertmartinezlewis.job_tracker.parser;

import net.jqwik.api.*;
import net.jqwik.api.constraints.WithNull;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertTrue(result.min() <= result.max(), "The salary is: " +  result.min() + "€ - " + result.max() + "€");
    }

    @Provide
    Arbitrary<String> salaryWithNegativeRanges() {
        Arbitrary<Integer> first = Arbitraries.integers().between(-1000000, 1000000);
        Arbitrary<Integer> second = Arbitraries.integers().between(-1000000, 1000000);
        return Combinators.combine(first, second).as((a, b) -> a + "€ - " + b + " €");
    }

    @Property(edgeCases = EdgeCasesMode.FIRST)
    void minIsPositiveWhenPresent(@ForAll("salaryWithNegativeRanges") String input) {
        InterpretedSalary result = SalaryInterpreter.interpretSalary(input);
        if(result.min() != null){
            assertTrue(result.min() > 0, "The min salary is: " +  result.min() + "€");
        }
    }

    @Property(edgeCases = EdgeCasesMode.FIRST)
    void maxIsPositiveWhenPresent(@ForAll("salaryWithNegativeRanges") String input) {
        InterpretedSalary result = SalaryInterpreter.interpretSalary(input);
        if(result.max() != null) {
            assertTrue(result.max() > 0, "The max salary is: " + result.max() + "€");
        }
    }

    @Property
    void neverThrowsOrReturnsNull(@ForAll @WithNull String input) {
        InterpretedSalary result = SalaryInterpreter.interpretSalary(input);
        assertNotNull(result);
    }

    @Provide
    Arbitrary<String> oversizedSalaryRanges() {
        Arbitrary<String> first = Arbitraries.strings().numeric().ofMinLength(1).ofMaxLength(30);
        Arbitrary<String> second = Arbitraries.strings().numeric().ofMinLength(1).ofMaxLength(30);
        return Combinators.combine(first, second).as((a, b) -> a + "€ - " + b + " €");
    }

    @Property
    void neverThrowsOrReturnsNullForHugeNumbers(@ForAll("oversizedSalaryRanges") String input) {
        InterpretedSalary result = SalaryInterpreter.interpretSalary(input);
        assertNotNull(result);
    }
}
