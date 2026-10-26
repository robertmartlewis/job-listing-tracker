package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.Modality;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RssFeedParserTest {

    @Test
    void returnsOneListingPerItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(4, parsedListings.size());
    }

    @Test
    void extractsUrlFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("https://www.tecnoempleo.com/ejemplo-a/springboot/rf-00000000000000000001",
                parsedListings.get(0).url());
    }

    @Test
    void extractsUrlWithoutSurroundingSpaces() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("https://www.tecnoempleo.com/ejemplo-b/java/rf-00000000000000000002",
                parsedListings.get(1).url());
    }

    @Test
    void extractsRfIdFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("rf-00000000000000000001",
                parsedListings.get(0).rfId());
    }

    @Test
    void extractsRfIdFromLinkWithSurroundingSpaces() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("rf-00000000000000000002",
                parsedListings.get(1).rfId());
    }

    @Test
    void extractTitleFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("Arquitecto/a Soluciones Java (EJEMPLO A)", parsedListings.get(0).title());
    }

    @Test
    void extractsTitleWithSurroundingSpaces() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("Desarrollador/a Java Backend (EJEMPLO B)", parsedListings.get(1).title());
    }

    @Test
    void extractsPublishedAtFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(Instant.parse("2026-09-25T13:13:16Z"), parsedListings.get(0).publishedAt());
    }

    @Test
    void extractsCompanyNameFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("Empresa Ejemplo A", parsedListings.get(0).company());
    }

    @Test
    void extractsDescriptionFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("""
                Buscamos arquitecto con experiencia en entornos API.
                
                Requisitos:
                
                - Tecnologías: Experto en soluciones técnicas. Spring boot, Kafka.
                - Idiomas: Castellano, Ingles B1
                
                100% de teletrabajo.""", parsedListings.get(0).description());
    }

    @Test
    void extractsModalityFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(Modality.REMOTE, parsedListings.get(0).modality());
    }

    @Test
    void extractsProvinceFromFourthItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("Asturias", parsedListings.get(3).province());
    }

    @Test
    void extractsNoProvinceFromSecondItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertNull(parsedListings.get(1).province());
    }

    @Test
    void extractsModalityFromSecondItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(Modality.HYBRID, parsedListings.get(1).modality());
    }

    @Test
    void extractsTechnologiesFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("Java, SpringBoot, Kafka", parsedListings.get(0).technologies());
    }

    @Test
    void extractsTechnologiesFromSecondItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("Java, Spring", parsedListings.get(1).technologies());
    }

    @Test
    void extractsSalaryTextFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("30000 € - 45000 € Bruto/año", parsedListings.get(0).salaryText());
    }

    @Test
    void extractsSalaryFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(30000, parsedListings.get(0).salaryMin());
        assertEquals(45000, parsedListings.get(0).salaryMax());
    }

    @Test
    void extractsSalaryFromSecondItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertNull(parsedListings.get(1).salaryMin());
        assertNull(parsedListings.get(1).salaryMax());
    }

    @Test
    void extractsSalaryFromThirdItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertNull(parsedListings.get(2).salaryMin());
        assertEquals(45000, parsedListings.get(2).salaryMax());
    }

    @Test
    void extractsExperienceTextFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals("3 años", parsedListings.get(0).experienceText());
    }

    @Test
    void extractsExperienceFromFirstItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(3, parsedListings.get(0).experienceMin());
        assertNull(parsedListings.get(0).experienceMax());
    }

    @Test
    void extractsExperienceFromSecondItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(3, parsedListings.get(1).experienceMin());
        assertEquals(5, parsedListings.get(1).experienceMax());
    }

    @Test
    void extractsExperienceFromThirdItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(5, parsedListings.get(2).experienceMin());
        assertNull(parsedListings.get(2).experienceMax());
    }

    @Test
    void extractsExperienceFromFourthItem() throws FeedException {
        List<ParsedListing> parsedListings = getParsedListings();
        assertEquals(0, parsedListings.get(3).experienceMin());
        assertNull(parsedListings.get(3).experienceMax());
    }

    private List<ParsedListing> getParsedListings() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        return parsedListings;
    }

    String readClasspathResourceAsText(String fileName) {
        String path = "/" +  fileName;
        try (InputStream stream =  RssFeedParserTest.class.getResourceAsStream(path)) {
            if (stream == null) throw new FileNotFoundException(path);

            byte[] bytes = stream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading " + fileName, e);
        }
    }
}
