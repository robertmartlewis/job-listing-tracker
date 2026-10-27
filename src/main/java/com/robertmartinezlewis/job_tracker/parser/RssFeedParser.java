package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public final class RssFeedParser {

    private RssFeedParser(){
    }

    public static ParsedFeed parseRssFeed(String rssFeedText) throws FeedException {
        StringReader reader = new StringReader(rssFeedText);
        SyndFeed input = new SyndFeedInput().build(reader);


        List<ParsedListing> listings = new ArrayList<>();
        for (SyndEntry entry : input.getEntries()) {
            DescriptionValuesRaw descriptionValuesRaw = DescriptionParser.parse(entry.getDescription().getValue());
            ParsedListing parsedListing = toParsedListing(entry, descriptionValuesRaw);
            listings.add(parsedListing);
        }
        return new ParsedFeed(listings, List.of());
    }

    private static ParsedListing toParsedListing(SyndEntry entry, DescriptionValuesRaw descriptionValuesRaw) {
        String link = entry.getLink().strip();
        InterpretedLocation interpretedLocation = LocationInterpreter.interpretLocation(
                descriptionValuesRaw.province());
        InterpretedSalary interpretedSalary = SalaryInterpreter.interpretSalary(descriptionValuesRaw.salary());
        InterpretedExperience interpretedExperience = ExperienceInterpreter.interpretExperience(
                descriptionValuesRaw.experience());
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
                descriptionValuesRaw.experience(),
                interpretedExperience.min(),
                interpretedExperience.max()
        );
    }
}
