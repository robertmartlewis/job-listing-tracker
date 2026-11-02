package com.robertmartinezlewis.job_tracker.ingest;

import com.robertmartinezlewis.job_tracker.JobListing;
import com.robertmartinezlewis.job_tracker.Modality;
import com.robertmartinezlewis.job_tracker.parser.ParsedListing;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

class ParsedListingConverterTest {

    private static final Instant NOW = Instant.parse("2026-10-30T00:00:00Z");

    private final ParsedListingConverter converter =
            new ParsedListingConverter(Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void setsFirstSeenDateFromClock() {
        JobListing jobListing = converter.toJobListing(aParsedListing());

        assertThat(jobListing.getFirstSeenDate()).isEqualTo(NOW);
    }
    
    @Test
    void setsPostedDateInMadridTime() {
        JobListing jobListing = converter.toJobListing(aParsedListing());
        assertThat(jobListing.getPostedDate()).isEqualTo(LocalDate.of(2026, 9, 26));
    }

    private static ParsedListing aParsedListing() {
        return new ParsedListing(
                "rf-0123456789abcdef0123",
                "https://www.tecnoempleo.com/oferta-java/rf-0123456789abcdef0123",
                "Desarrollador Java Junior",
                "Empresa Ejemplo",
                "Descripción de ejemplo",
                Instant.parse("2026-09-25T22:30:00Z"),
                null,
                Modality.REMOTE,
                "Java, Spring Boot",
                "30000 € - 45000 € Bruto/año",
                30000,
                45000,
                "3-5 años",
                3,
                5
        );
    }
}
