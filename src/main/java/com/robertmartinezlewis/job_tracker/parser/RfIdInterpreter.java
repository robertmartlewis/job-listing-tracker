package com.robertmartinezlewis.job_tracker.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RfIdInterpreter {

    private static final Pattern RFID =
            Pattern.compile("(rf-[0-9a-f]{20})$");

    private RfIdInterpreter(){}

    public static String interpretRfId(String url) {
        if(url == null) return null;
        Matcher matcher = RFID.matcher(url);
        if(matcher.find()) return matcher.group(1);
        return null;
    }
}
