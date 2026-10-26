package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.Modality;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

public final class RssFeedParser {

    private RssFeedParser(){
    }

    public static List<ParsedListing> parseRssFeed(String rssFeedText) throws FeedException {
        StringReader reader = new StringReader(rssFeedText);
        SyndFeed input = new SyndFeedInput().build(reader);


        return input.getEntries().stream().map(RssFeedParser::toParsedListing).toList();
    }

    private static ParsedListing toParsedListing(SyndEntry entry) {
        String link = entry.getLink().strip();
        DescriptionValuesRaw descriptionValuesRaw = DescriptionParser.parse(entry.getDescription().getValue());
        InterpretedLocation interpretedLocation = LocationInterpreter.interpretLocation(
                descriptionValuesRaw.province());
        InterpretedSalary interpretedSalary = SalaryInterpreter.interpretSalary(descriptionValuesRaw.salary());
        return new ParsedListing(
                RfIdInterpreter.interpretRfId(link),
                link,
                entry.getTitle().strip(),
                descriptionValuesRaw.company(),
                descriptionValuesRaw.description(),
                entry.getPublishedDate().toInstant(),
                interpretedLocation.province(),
                interpretedLocation.modality(),
                descriptionValuesRaw.technologies(),
                descriptionValuesRaw.salary(),
                interpretedSalary.min(),
                interpretedSalary.max(),
                null,
                null,
                null
        );
    }
}
