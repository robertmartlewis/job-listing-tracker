package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.Modality;
import com.robertmartinezlewis.job_tracker.SpanishProvinces;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationInterpreterTest {

    @ParameterizedTest(name = "\"{0}\" -> min={1}, max={2}")
    @MethodSource("locationCases")
    void interpretLocation_parseRange(String input, Modality modality, String province) {
        InterpretedLocation result = LocationInterpreter.interpretLocation(input);

        assertEquals(modality, result.modality());
        assertEquals(province, result.province());
    }

    static Stream<Arguments> locationCases() {
        return Stream.of(
                Arguments.of("100% En remoto", Modality.REMOTE, null),
                Arguments.of("hibrido", Modality.HYBRID, null),
                Arguments.of("presencial", Modality.OFFICE, null),
                Arguments.of("Asturias", null, "Asturias"),
                Arguments.of("MADRID", null, "Madrid"),
                Arguments.of("Qwerty", null, null),
                Arguments.of("híbrido", Modality.HYBRID, null),
                Arguments.of(null, null, null)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allProvinces")
    void interpretLocation_everyCanonicalProvince_isRecognizedAsItself(String province) {
        assertEquals(new InterpretedLocation(null, province),
                LocationInterpreter.interpretLocation(province));
    }

    static Stream<String> allProvinces() {
        return SpanishProvinces.PROVINCES.stream();
    }
}
