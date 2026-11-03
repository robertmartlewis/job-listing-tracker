package com.robertmartinezlewis.job_tracker.parser;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

public class ParsedListingTest {

    @Test
    void rejectsNonExsitingProvince() {
        assertThatThrownBy(() -> aParsedListingWithProvince("Narnia"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid province: \"Narnia\"");
    }

    @Test
    void doesNotRejectNullProvince() {
        assertThatCode(() -> aParsedListingWithProvince(null)).doesNotThrowAnyException();
    }

    @Test
    void keepsProvinceAsGiven() {
        assertThat(aParsedListingWithProvince("Asturias").province()).isEqualTo("Asturias");
    }

    private static ParsedListing aParsedListingWithProvince(String province) {
        return new ParsedListing(
                "rf-0123456789abcdef0123",
                "https://www.tecnoempleo.com/oferta-java/rf-0123456789abcdef0123",
                "Desarrollador Java Junior",
                "Empresa de ejemplo",
                "Descripción de ejemplo",
                Instant.parse("2026-09-25T22:30:00Z"),
                province,
                null,
                "Java, Spring Boot",
                "Sin especificar",
                null,
                null,
                "Más de 5 años",
                5,
                null
        );
    }
}
