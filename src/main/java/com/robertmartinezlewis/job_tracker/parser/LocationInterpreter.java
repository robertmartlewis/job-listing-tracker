package com.robertmartinezlewis.job_tracker.parser;

import com.robertmartinezlewis.job_tracker.Modality;
import com.robertmartinezlewis.job_tracker.SpanishProvinces;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class LocationInterpreter {

    private static final Pattern DIATRITICS = Pattern.compile("\\p{M}");

    private static final Map<String, Modality> MODALITY_BY_KEY = Map.of(
            "100% en remoto", Modality.REMOTE,
            "hibrido", Modality.HYBRID,
            "presencial", Modality.OFFICE
    );

    private static final Map<String, String> PROVINCE_BY_KEY = SpanishProvinces.PROVINCES.stream()
            .collect(Collectors.toUnmodifiableMap(
               LocationInterpreter::normalizeRawInput,
               Function.identity()
            ));

    private LocationInterpreter(){
    }

    private static String normalizeRawInput(String rawInput){
        String decomposed = Normalizer.normalize(rawInput, Normalizer.Form.NFD);
        return DIATRITICS.matcher(decomposed).replaceAll("").toLowerCase(Locale.ROOT);
    }

    public static InterpretedLocation interpretLocation(String rawLocation){

        if (rawLocation == null) return new InterpretedLocation(null, null);

        String key = normalizeRawInput(rawLocation);

        Modality modality = MODALITY_BY_KEY.get(key);
        if (modality != null) return new InterpretedLocation(modality, null);

        String province = PROVINCE_BY_KEY.get(key);
        if (province != null) return new InterpretedLocation(null, province);

        return new InterpretedLocation(null, null);
    }


}
