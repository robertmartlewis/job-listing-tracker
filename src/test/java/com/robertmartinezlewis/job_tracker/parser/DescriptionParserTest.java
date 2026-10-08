package com.robertmartinezlewis.job_tracker.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DescriptionParserTest {

    @Test
    void extractsCompanyWithoutSurroundingSpaces() {
        String html = "<b>Empresa:</b>&nbsp;Empresa Ejemplo A                   <br />";

        DescriptionValuesRaw result = DescriptionParser.parse(html);

        assertThat(result.company()).isEqualTo("Empresa Ejemplo A");
    }
}
