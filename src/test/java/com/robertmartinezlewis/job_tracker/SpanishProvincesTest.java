package com.robertmartinezlewis.job_tracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpanishProvincesTest {

    @Test
    void provinces_has50ProvincesPlusCeutaAndMelilla() {
        assertEquals(52, SpanishProvinces.PROVINCES.size());
    }

    @Test
    void provinces_hasNoDuplicates() {
        assertEquals(SpanishProvinces.PROVINCES.size(),
                SpanishProvinces.PROVINCES.stream().distinct().count());
    }
}
