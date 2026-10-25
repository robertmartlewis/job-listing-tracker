package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RssFeedParserTest {

    @Test
    void returnsOneListingPerItem() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals(4, parsedListings.size());
    }

    @Test
    void extractsUrlFromFirstItem() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals("https://www.tecnoempleo.com/ejemplo-a/springboot/rf-00000000000000000001",
                parsedListings.get(0).url());
    }

    @Test
    void extractsUrlWithoutSurroundingSpaces() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals("https://www.tecnoempleo.com/ejemplo-b/java/rf-00000000000000000002",
                parsedListings.get(1).url());
    }

    @Test
    void extractsRfIdFromFirstItem() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals("rf-00000000000000000001",
                parsedListings.get(0).rfId());
    }

    @Test
    void extractsRfIdFromLinkWithSurroundingSpaces() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals("rf-00000000000000000002",
                parsedListings.get(1).rfId());
    }

    @Test
    void extractTitleFromFirstItem() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals("Arquitecto/a Soluciones Java (EJEMPLO A)", parsedListings.get(0).title());
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
