package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

public class RssFeedParserRejectionsTest {

    @Test
    void rejectsItemWithoutCompany()  throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed();

        assertEquals(1, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_COMPANY), parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void keepsValidItemWhenAnotherCompanyIsRejected()  throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed();

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("Empresa Ejemplo Válida", parsedFeed.listings().get(0).company());
    }

    @Test
    void reportsPositionOfRejectedItem()  throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed();

        assertEquals(1, parsedFeed.rejections().get(0).position());
    }

    private ParsedFeed getParsedFeed() throws FeedException {
        String xml = readClasspathResourceAsText("tecnoempleo-sample-invalid.xml");
        return RssFeedParser.parseRssFeed(xml);
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
