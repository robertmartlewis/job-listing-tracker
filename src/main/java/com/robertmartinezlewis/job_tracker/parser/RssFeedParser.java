package com.robertmartinezlewis.job_tracker.parser;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public final class RssFeedParser {

    private RssFeedParser(){
    }

    public static List<ParsedListing> parseRssFeed(String rssFeedText) throws FeedException {
        List<ParsedListing> listings = new ArrayList<>();

        StringReader reader = new StringReader(rssFeedText);
        SyndFeed input = new SyndFeedInput().build(reader);

        return listings;
    }
}
