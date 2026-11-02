package com.robertmartinezlewis.job_tracker.ingest;

import com.robertmartinezlewis.job_tracker.JobListing;
import com.robertmartinezlewis.job_tracker.Modality;
import com.robertmartinezlewis.job_tracker.Source;
import com.robertmartinezlewis.job_tracker.Status;
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
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);

        assertThat(jobListing.getFirstSeenDate()).isEqualTo(NOW);
    }
    
    @Test
    void setsPostedDateInMadridTime() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getPostedDate()).isEqualTo(LocalDate.of(2026, 9, 26));
    }

    @Test
    void setsSourceFromArgument() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.INFOJOBS);
        assertThat(jobListing.getSource()).isEqualTo(Source.INFOJOBS);
    }

    @Test
    void setsStatusNew() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getStatus()).isEqualTo(Status.NEW);
    }

    @Test
    void setsRfId() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getRfId()).isEqualTo("rf-0123456789abcdef0123");
    }

    @Test
    void setsUrl() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getUrl()).isEqualTo(
                "https://www.tecnoempleo.com/oferta-java/rf-0123456789abcdef0123");
    }

    @Test
    void setsTitle() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getTitle()).isEqualTo("Desarrollador Java Junior");
    }

    @Test
    void setsCompany() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getCompany()).isEqualTo("Empresa Ejemplo");
    }

    @Test
    void setsDescription() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getDescription()).isEqualTo("Descripción de ejemplo");
    }

    @Test
    void setsModality() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getModality()).isEqualTo(Modality.OFFICE);
    }

    @Test
    void setsProvince() {
        JobListing jobListing = converter.toJobListing(aParsedListingWithProvince(), Source.TECNOEMPLEO);
        assertThat(jobListing.getProvince()).isEqualTo("Asturias");
    }

    @Test
    void setsTechnologies() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getTechnologies()).isEqualTo("Java, Spring Boot");
    }

    @Test
    void setsSalaryText() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getSalaryText()).isEqualTo("30000 € - 45000 € Bruto/año");
    }

    @Test
    void setsSalaryMin() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getSalaryMin()).isEqualTo(Integer.valueOf(30000));
    }

    @Test
    void setsSalaryMax() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getSalaryMax()).isEqualTo(Integer.valueOf(45000));
    }

    @Test
    void setsSalaryMinWhenNull() {
        JobListing jobListing = converter.toJobListing(aParsedListingWithProvince(), Source.TECNOEMPLEO);
        assertThat(jobListing.getSalaryMin()).isNull();
    }

    @Test
    void setsSalaryMaxWhenNull() {
        JobListing jobListing = converter.toJobListing(aParsedListingWithProvince(), Source.TECNOEMPLEO);
        assertThat(jobListing.getSalaryMax()).isNull();
    }

    @Test
    void setsExperienceText() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getExperienceText()).isEqualTo("3-5 años");
    }

    @Test
    void setsExperienceMin() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getExperienceMin()).isEqualTo(Integer.valueOf(3));
    }

    @Test
    void setsExperienceMax() {
        JobListing jobListing = converter.toJobListing(aParsedListing(), Source.TECNOEMPLEO);
        assertThat(jobListing.getExperienceMax()).isEqualTo(Integer.valueOf(5));
    }

    @Test
    void setsExperienceTextWhenThereIsNoMaximum() {
        JobListing jobListing = converter.toJobListing(aParsedListingWithProvince(), Source.TECNOEMPLEO);
        assertThat(jobListing.getExperienceText()).isEqualTo("Más de 5 años");
    }

    @Test
    void setsExperienceMinWhenThereIsNoMaximum() {
        JobListing jobListing = converter.toJobListing(aParsedListingWithProvince(), Source.TECNOEMPLEO);
        assertThat(jobListing.getExperienceMin()).isEqualTo(Integer.valueOf(5));
    }

    @Test
    void setsExperienceMaxWhenThereIsNoMaximum() {
        JobListing jobListing = converter.toJobListing(aParsedListingWithProvince(), Source.TECNOEMPLEO);
        assertThat(jobListing.getExperienceMax()).isNull();
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
                Modality.OFFICE,
                "Java, Spring Boot",
                "30000 € - 45000 € Bruto/año",
                30000,
                45000,
                "3-5 años",
                3,
                5
        );
    }

    private static ParsedListing aParsedListingWithProvince() {
        return new ParsedListing(
                "rf-0123456789abcdef0123",
                "https://www.tecnoempleo.com/oferta-java/rf-0123456789abcdef0123",
                "Desarrollador Java Junior",
                "Empresa Ejemplo",
                "Descripción de ejemplo",
                Instant.parse("2026-09-25T22:30:00Z"),
                "Asturias",
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
