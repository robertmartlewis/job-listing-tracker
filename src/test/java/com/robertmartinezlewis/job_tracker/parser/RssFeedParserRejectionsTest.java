package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Stream;

import static com.robertmartinezlewis.job_tracker.parser.RejectionReason.*;
import static org.junit.jupiter.api.Assertions.*;

public class RssFeedParserRejectionsTest {

    @ParameterizedTest(name = "{0}: rejection {1} has reasons {2}")
    @MethodSource("expectedRejectionReasons")
    void rejectsItemWithExpectedReasons(String fixture, int rejectionIndex, Set<RejectionReason> expectedReasons)
            throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed(fixture);

        assertEquals(expectedReasons, parsedFeed.rejections().get(rejectionIndex).reasons());
    }

    @ParameterizedTest(name = "{0}: {1} rejections")
    @MethodSource("expectedRejectionCounts")
    void rejectsExpectedNumberOfItems(String fixture, int expectedCount) throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed(fixture);

        assertEquals(expectedCount, parsedFeed.rejections().size());
    }

    @Test
    void keepsValidItemWhenAnotherCompanyIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-company.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("Empresa Ejemplo Válida", parsedFeed.listings().get(0).company());
    }

    @Test
    void reportsPositionOfRejectedItem() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-company.xml");

        assertEquals(1, parsedFeed.rejections().get(0).position());
        assertEquals(2, parsedFeed.rejections().get(1).position());
    }

    @Test
    void keepsValidItemWhenAnotherTitleIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-title.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("Desarrollador/a Java (EJEMPLO VÁLIDO)", parsedFeed.listings().get(0).title());
    }

    @Test
    void keepsValidItemWhenAnotherPublishedDateIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-published-date.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals(Instant.parse("2026-10-05T09:00:00Z"), parsedFeed.listings().get(0).publishedAt());
    }

    @Test
    void keepsValidItemWhenAnotherRfIdIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-rf-id.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("rf-00000000000000000401", parsedFeed.listings().get(0).rfId());
    }

    @Test
    void keepsValidItemWhenAnotherUrlIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-url.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("https://www.tecnoempleo.com/ejemplo-valido/java/rf-00000000000000000501",
                parsedFeed.listings().get(0).url());
    }

    @Test
    void keepsValidItemWhenAnotherDescriptionIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-description.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("Oferta de control para el fixture de descripciones ausentes.",
                parsedFeed.listings().get(0).description());
    }

    @Test
    void keepsValidItemWhenSeveralOthersAreRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("rf-00000000000000000701", parsedFeed.listings().get(0).rfId());
    }

    @Test
    void reportsPositionOfEachItemRejectedForSeveralReasons() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(1, parsedFeed.rejections().get(0).position());
        assertEquals(2, parsedFeed.rejections().get(1).position());
        assertEquals(3, parsedFeed.rejections().get(2).position());
    }

    static Stream<Arguments> expectedRejectionReasons() {
        return Stream.of(
                Arguments.of("tecnoempleo-sample-missing-company.xml", 0, Set.of(MISSING_COMPANY)),
                Arguments.of("tecnoempleo-sample-missing-title.xml", 0, Set.of(MISSING_TITLE)),
                Arguments.of("tecnoempleo-sample-missing-title.xml", 1, Set.of(MISSING_TITLE)),
                Arguments.of("tecnoempleo-sample-missing-published-date.xml", 0, Set.of(MISSING_PUBLISHED_DATE)),
                Arguments.of("tecnoempleo-sample-missing-published-date.xml", 1, Set.of(MISSING_PUBLISHED_DATE)),
                Arguments.of("tecnoempleo-sample-missing-rf-id.xml", 0, Set.of(MISSING_RF_ID)),
                Arguments.of("tecnoempleo-sample-missing-rf-id.xml", 1, Set.of(MISSING_RF_ID)),
                Arguments.of("tecnoempleo-sample-missing-url.xml", 0, Set.of(MISSING_URL, MISSING_RF_ID)),
                Arguments.of("tecnoempleo-sample-missing-url.xml", 1, Set.of(MISSING_URL, MISSING_RF_ID)),
                Arguments.of("tecnoempleo-sample-missing-description.xml", 0,
                        Set.of(MISSING_DESCRIPTION, MISSING_COMPANY)),
                Arguments.of("tecnoempleo-sample-missing-description.xml", 1, Set.of(MISSING_DESCRIPTION)),
                Arguments.of("tecnoempleo-sample-missing-several.xml", 0,
                        Set.of(MISSING_TITLE, MISSING_COMPANY, MISSING_PUBLISHED_DATE)),
                Arguments.of("tecnoempleo-sample-missing-several.xml", 1,
                        Set.of(MISSING_RF_ID, MISSING_DESCRIPTION)),
                Arguments.of("tecnoempleo-sample-missing-several.xml", 2,
                        Set.of(MISSING_TITLE, MISSING_COMPANY, MISSING_PUBLISHED_DATE,
                                MISSING_RF_ID, MISSING_URL, MISSING_DESCRIPTION))
        );
    }

    static Stream<Arguments> expectedRejectionCounts() {
        return Stream.of(
                Arguments.of("tecnoempleo-sample-missing-company.xml", 2),
                Arguments.of("tecnoempleo-sample-missing-title.xml", 2),
                Arguments.of("tecnoempleo-sample-missing-published-date.xml", 2),
                Arguments.of("tecnoempleo-sample-missing-rf-id.xml", 2),
                Arguments.of("tecnoempleo-sample-missing-url.xml", 2),
                Arguments.of("tecnoempleo-sample-missing-description.xml", 2),
                Arguments.of("tecnoempleo-sample-missing-several.xml", 3)
        );
    }

    private ParsedFeed getParsedFeed(String fileName) throws FeedException {
        String xml = TestResources.readClasspathResourceAsText(fileName);
        return RssFeedParser.parseRssFeed(xml);
    }
}