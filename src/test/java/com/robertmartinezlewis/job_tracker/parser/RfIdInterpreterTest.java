package com.robertmartinezlewis.job_tracker.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RfIdInterpreterTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @MethodSource("rfIdCases")
    void interpretRfId_extractsRfIdFromUrl(String url, String expected) {
        assertEquals(expected, RfIdInterpreter.interpretRfId(url));
    }

    static Stream<Arguments> rfIdCases() {
        return Stream.of(
                Arguments.of(
                        "https://www.tecnoempleo.com/ejemplo-a/springboot/rf-00000000000000000001",
                        "rf-00000000000000000001"),
                Arguments.of(null, null),
                Arguments.of(
                        "https://www.tecnoempleo.com/ejemplo-a/springboot/00000000000000000001",
                        null),
                Arguments.of(
                        "https://www.tecnoempleo.com/ejemplo-a/springboot/rf-100a0b04050008000001",
                        "rf-100a0b04050008000001"),
                Arguments.of(
                        "https://www.tecnoempleo.com/ejemplo-a/springboot/rf-100a0b04050008000001estono",
                        null),
                Arguments.of(
                        "https://www.tecnoempleo.com/ejemplo-a/springboot/rf-100a0b04050008000001a",
                        null),
                Arguments.of(
                        "https://www.tecnoempleo.com/ejemplo-a/springboot/rf-100A0B04050008000001",
                        null)
        );
    }
}
