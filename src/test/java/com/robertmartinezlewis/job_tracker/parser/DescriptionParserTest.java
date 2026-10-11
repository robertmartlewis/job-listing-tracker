package com.robertmartinezlewis.job_tracker.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DescriptionParserTest {

    @Test
    void extractsCompanyWithoutSurroundingSpaces() {
        String html = "<b>Empresa:</b>&nbsp;Empresa Ejemplo A                   <br />";

        DescriptionValuesRaw result = DescriptionParser.parse(html);

        assertThat(result.company()).isEqualTo("Empresa Ejemplo A");
    }

    @Test
    void blankCompanyBecomesNull() {
        String html = "<b>Empresa:</b>&nbsp;                   <br />";

        DescriptionValuesRaw result = DescriptionParser.parse(html);

        assertThat(result.company()).isNull();
    }

    @Test
    void ignoresTechnologiesLabelInsideFreeText() {
        String html = """
                <b>Descripción:</b>&nbsp;Buscamos arquitecto.
                - Tecnologías: Experto en soluciones técnicas. Spring boot, Kafka.
                <br /><b>Tecnologías:</b>&nbsp;Java, SpringBoot, Kafka              <br />
                <b>Tipo de Contrato:</b>&nbsp;Indefinido              <br />
                """;

        DescriptionValuesRaw result = DescriptionParser.parse(html);

        assertThat(result.technologies()).isEqualTo("Java, SpringBoot, Kafka");
    }
}
