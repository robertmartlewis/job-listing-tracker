package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import java.io.StringReader;
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
        return new ParsedListing(
                RfIdInterpreter.interpretRfId(link),
                link,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
