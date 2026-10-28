package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

public class RssFeedParserRejectionsTest {

    @Test
    void rejectsItemWithoutCompany() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-company.xml");

        assertEquals(2, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_COMPANY), parsedFeed.rejections().get(0).reasons());
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
    void rejectsItemWithoutTitle() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-title.xml");

        assertEquals(2, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_TITLE), parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void keepsValidItemWhenAnotherTitleIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-title.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("Desarrollador/a Java (EJEMPLO VÁLIDO)", parsedFeed.listings().get(0).title());
    }

    @Test
    void rejectsItemWithBlankTitle() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-title.xml");

        assertEquals(Set.of(RejectionReason.MISSING_TITLE), parsedFeed.rejections().get(1).reasons());
    }

    @Test
    void rejectsItemWithoutPublishedDate() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-published-date.xml");

        assertEquals(2, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_PUBLISHED_DATE), parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void keepsValidItemWhenAnotherPublishedDateIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-published-date.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals(Instant.parse("2026-10-05T09:00:00Z"), parsedFeed.listings().get(0).publishedAt());
    }

    @Test
    void rejectsIllegiblePublishedDate() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-published-date.xml");

        assertEquals(Set.of(RejectionReason.MISSING_PUBLISHED_DATE), parsedFeed.rejections().get(1).reasons());
    }

    @Test
    void rejectsItemWithoutRfId() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-rf-id.xml");

        assertEquals(2, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_RF_ID), parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void keepsValidItemWhenAnotherRfIdIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-rf-id.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("rf-00000000000000000401", parsedFeed.listings().get(0).rfId());
    }

    @Test
    void rejectsInvalidRfId() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-rf-id.xml");

        assertEquals(Set.of(RejectionReason.MISSING_RF_ID), parsedFeed.rejections().get(1).reasons());
    }

    @Test
    void rejectsItemWithoutUrl() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-url.xml");

        assertEquals(2, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_URL, RejectionReason.MISSING_RF_ID),
                parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void keepsValidItemWhenAnotherUrlIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-url.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("https://www.tecnoempleo.com/ejemplo-valido/java/rf-00000000000000000501",
                parsedFeed.listings().get(0).url());
    }

    @Test
    void rejectsInvalidUrl() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-url.xml");

        assertEquals(Set.of(RejectionReason.MISSING_URL, RejectionReason.MISSING_RF_ID),
                parsedFeed.rejections().get(1).reasons());
    }

    @Test
    void rejectsItemWithoutDescription() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-description.xml");

        assertEquals(2, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_DESCRIPTION, RejectionReason.MISSING_COMPANY),
                parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void keepsValidItemWhenAnotherDescriptionIsRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-description.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("Oferta de control para el fixture de descripciones ausentes.",
                parsedFeed.listings().get(0).description());
    }

    @Test
    void rejectsItemWithBlankDescription() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-description.xml");

        assertEquals(Set.of(RejectionReason.MISSING_DESCRIPTION),
                parsedFeed.rejections().get(1).reasons());
    }

    @Test
    void keepsValidItemWhenSeveralOthersAreRejected() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(1, parsedFeed.listings().size());
        assertEquals("rf-00000000000000000701", parsedFeed.listings().get(0).rfId());
    }

    @Test
    void rejectsItemWithoutTileCompanyAndPublishedDate() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(3, parsedFeed.rejections().size());
        assertEquals(Set.of(RejectionReason.MISSING_TITLE,
                RejectionReason.MISSING_COMPANY,
                RejectionReason.MISSING_PUBLISHED_DATE), parsedFeed.rejections().get(0).reasons());
    }

    @Test
    void rejectsItemWithInvalidRfIdAndBlankDescription() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(Set.of(RejectionReason.MISSING_RF_ID,
                RejectionReason.MISSING_DESCRIPTION), parsedFeed.rejections().get(1).reasons());
    }

    @Test
    void rejectsItemWithMissingEveryRequiredField() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(Set.of(RejectionReason.MISSING_TITLE,
                    RejectionReason.MISSING_COMPANY,
                    RejectionReason.MISSING_PUBLISHED_DATE,
                    RejectionReason.MISSING_RF_ID,
                    RejectionReason.MISSING_URL,
                    RejectionReason.MISSING_DESCRIPTION),
                parsedFeed.rejections().get(2).reasons());
    }

    @Test
    void reportsPositionOfEachItemRejectedForSeveralReason() throws FeedException {
        ParsedFeed parsedFeed = getParsedFeed("tecnoempleo-sample-missing-several.xml");

        assertEquals(1, parsedFeed.rejections().get(0).position());
        assertEquals(2, parsedFeed.rejections().get(1).position());
        assertEquals(3, parsedFeed.rejections().get(2).position());
    }

    private ParsedFeed getParsedFeed(String fileName) throws FeedException {
        String xml = TestResources.readClasspathResourceAsText(fileName);
        return RssFeedParser.parseRssFeed(xml);
    }


}
