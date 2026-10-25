package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.SpanishProvinces;
import net.jqwik.api.*;
import net.jqwik.api.constraints.WithNull;

import static org.junit.jupiter.api.Assertions.*;

public class LocationInterpreterPropertiesTest {

    @Provide
    Arbitrary<String> provinces () {
        return Arbitraries.of(SpanishProvinces.PROVINCES);
    }

    @Property(generation = GenerationMode.EXHAUSTIVE)
    void everyCanonicalProvinceIsRecognizedAsItself (@ForAll("provinces") String province){
        InterpretedLocation result = LocationInterpreter.interpretLocation(province);
        assertNull(result.modality());
        assertEquals(province, result.province());
    }

    @Property(generation = GenerationMode.EXHAUSTIVE)
    void provinceIsRecognizedInUpperCase (@ForAll("provinces") String province){
        InterpretedLocation result = LocationInterpreter.interpretLocation(province.toUpperCase());
        assertNull(result.modality());
        assertEquals(province, result.province());
    }

    @Property(generation = GenerationMode.EXHAUSTIVE)
    void provinceIsRecognizedInLowerCase (@ForAll("provinces") String province){
        InterpretedLocation result = LocationInterpreter.interpretLocation(province.toLowerCase());
        assertNull(result.modality());
        assertEquals(province, result.province());
    }

    @Property
    void neverThrowsOrReturnsNull(@ForAll @WithNull String input) {
        InterpretedLocation result = LocationInterpreter.interpretLocation(input);
        assertNotNull(result);
    }

    @Provide
    Arbitrary<String> modalities () {
        return Arbitraries.of("100% en remoto", "Híbrido", "Presencial");
    }

    @Provide
    Arbitrary<String> anyLocationInput () {
        return Arbitraries.oneOf(provinces(), modalities(), Arbitraries.strings());
    }

    @Property
    void interpretLocationNeverReturnsNull(@ForAll("anyLocationInput") @WithNull String input) {
        InterpretedLocation result = LocationInterpreter.interpretLocation(input);
        assertNotNull(result);
    }

    @Property
    void modalityAndProvinceAreNeverBothPresent(@ForAll("anyLocationInput") @WithNull String input) {
        InterpretedLocation result = LocationInterpreter.interpretLocation(input);
        assertFalse(result.modality() != null && result.province() != null);
    }

}
