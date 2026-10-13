package com.robertmartinezlewis.job_tracker.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExperienceInterpreter {

    private static final Pattern EXPERIENCE_RANGE =
            Pattern.compile("(\\d+)(?:\\s*-\\s*(\\d+))?\\s*años?",
                    Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);

    private ExperienceInterpreter() {
    }

    public static InterpretedExperience interpretExperience(String experience){

        if(experience == null) return new InterpretedExperience(null, null);

        if(experience.equals("No requerida")) return new InterpretedExperience(0, null);

        Matcher m = EXPERIENCE_RANGE.matcher(experience);
        if(m.find()) {
            Integer min = Integer.valueOf(m.group(1));
            Integer max = null;
            if(m.group(2) != null){
                max = Integer.valueOf(m.group(2));
            }
            return new InterpretedExperience(min, max);
        }

        return new InterpretedExperience(null, null);
    }

}
