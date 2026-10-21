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
            Integer min = Integer.valueOf(m.group(1));
            Integer max = Integer.valueOf(m.group(2));
            if(min.equals(0)){
                min = null;
            }
            if(max.equals(0)){
                max = null;
            }
            if(min != null && max != null){
                Integer tmpMin = max;
                if(min > max){
                    max = min;
                    min = tmpMin;
                }
            }
            return new InterpretedSalary(min, max);
        }
        return new InterpretedSalary(null, null);
    }

}
