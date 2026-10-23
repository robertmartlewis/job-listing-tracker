package com.robertmartinezlewis.job_tracker.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExperienceInterpreter {

    private static final Pattern EXPERIENCE_LESS_THAN =
            Pattern.compile("\\s*menos de\\s*(\\d+)\\s*años?",
                    Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);

    private static final Pattern EXPERIENCE_RANGE =
            Pattern.compile("(\\d+)(?:\\s*-\\s*(\\d+))?\\s*años?",
                    Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);

    private ExperienceInterpreter() {
    }

    public static InterpretedExperience interpretExperience(String experience){

        if(experience == null) return new InterpretedExperience(null, null);

        if(experience.equals("No requerida")) return new InterpretedExperience(0, null);

        Matcher lessThanMatcher =  EXPERIENCE_LESS_THAN.matcher(experience);
        if(lessThanMatcher.find()){
            Integer max = parseInteger(lessThanMatcher.group(1));
            return new InterpretedExperience(0, max);
        }

        Matcher rangeMatcher = EXPERIENCE_RANGE.matcher(experience);
        if(rangeMatcher.find()) {
            Integer min = parseInteger(rangeMatcher.group(1));
            Integer max = null;
            if(rangeMatcher.group(2) != null){
                max = parseInteger(rangeMatcher.group(2));
            }

            if(min != null && max != null && min > max){
                Integer tmp =  min;
                min = max;
                max = tmp;
            }

            return new InterpretedExperience(min, max);
        }

        return new InterpretedExperience(null, null);
    }

    private static Integer parseInteger(String experience){
        Integer value;
        try {
            value = Integer.valueOf(experience);
        } catch (NumberFormatException e){
            return null;
        }
        if (value < 0) {
            return null;
        }
        return value;
    }
}
