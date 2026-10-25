package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.SpanishProvinces;
import net.jqwik.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

}
