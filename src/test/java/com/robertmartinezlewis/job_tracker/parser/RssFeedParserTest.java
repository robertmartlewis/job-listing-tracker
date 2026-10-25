package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RssFeedParserTest {

    @Test
    void returnsOneListingPerItem() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample.xml");
        List<ParsedListing> parsedListings = RssFeedParser.parseRssFeed(xml);
        assertEquals(4, parsedListings.size());
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
