package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import javax.swing.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class RssFeedParser {

    private RssFeedParser(){
    }

    public static ParsedFeed parseRssFeed(String rssFeedText) throws FeedException {
        StringReader reader = new StringReader(rssFeedText);
        SyndFeed input = new SyndFeedInput().build(reader);


        List<ParsedListing> listings = new ArrayList<>();
        List<Rejection> rejections = new ArrayList<>();
        int entryIndex = 0;
        for (SyndEntry entry : input.getEntries()) {
            DescriptionValuesRaw descriptionValuesRaw = DescriptionParser.parse(entry.getDescription().getValue());
            Set<RejectionReason> rejectionReasons = new HashSet<>();
            String link = entry.getLink() != null ? entry.getLink().strip() : "";
            String rfId = RfIdInterpreter.interpretRfId(link);
            if (descriptionValuesRaw.company() == null) {
                rejectionReasons.add(RejectionReason.MISSING_COMPANY);
            }
            if (entry.getTitle() == null || entry.getTitle().isBlank()) {
                rejectionReasons.add(RejectionReason.MISSING_TITLE);
            }
            if (entry.getPublishedDate() == null) {
                rejectionReasons.add(RejectionReason.MISSING_PUBLISHED_DATE);
            }
            if (rfId == null) {
                rejectionReasons.add(RejectionReason.MISSING_RF_ID);
            }
            if (link.isEmpty()) {
                rejectionReasons.add(RejectionReason.MISSING_URL);
            }

            if (rejectionReasons.isEmpty()) {
                ParsedListing parsedListing = toParsedListing(entry, descriptionValuesRaw, link, rfId);
                listings.add(parsedListing);
            } else {
                rejections.add(new Rejection(rejectionReasons, entryIndex));
            }
            entryIndex++;
        }
        return new ParsedFeed(listings, rejections);
    }

    private static ParsedListing toParsedListing(SyndEntry entry,
                                                 DescriptionValuesRaw descriptionValuesRaw,
                                                 String link,
                                                 String rfId) {
        InterpretedLocation interpretedLocation = LocationInterpreter.interpretLocation(
                descriptionValuesRaw.province());
        InterpretedSalary interpretedSalary = SalaryInterpreter.interpretSalary(descriptionValuesRaw.salary());
        InterpretedExperience interpretedExperience = ExperienceInterpreter.interpretExperience(
                descriptionValuesRaw.experience());
        return new ParsedListing(
                rfId,
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
