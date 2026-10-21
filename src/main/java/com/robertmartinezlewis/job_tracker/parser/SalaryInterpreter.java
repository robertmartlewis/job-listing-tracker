package com.robertmartinezlewis.job_tracker.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SalaryInterpreter {

    private static final Pattern SALARY_RANGE =
            Pattern.compile("(\\d+)\\s*€\\s*-\\s*(\\d+)\\s*€", Pattern.UNICODE_CHARACTER_CLASS);

    private SalaryInterpreter() {
    }

    public static InterpretedSalary interpretSalary(String salary){

        if(salary == null) return new InterpretedSalary(null, null);
        Matcher m = SALARY_RANGE.matcher(salary);

        if(m.find()) {
            Integer min = parseBound(m.group(1));
            Integer max = parseBound(m.group(2));

            if(min != null && max != null && min > max){
                Integer tmp =  min;
                min = max;
                max = tmp;
            }
            return new InterpretedSalary(min, max);
        }
        return new InterpretedSalary(null, null);
    }

    private static Integer parseBound(String digits) {
        Integer value;
        try {
            value = Integer.valueOf(digits);
        } catch (NumberFormatException e){
            return null;
        }
        if (value.equals(0)) {
            return null;
        }
        return value;
    }
}
