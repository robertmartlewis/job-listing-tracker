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

    @Test
    void extractsAllFieldsFromFullItem() {
        String html = """
                <b>Empresa:</b>&nbsp;Empresa Ejemplo A                <br /><b>Provincia:</b>&nbsp;100% En remoto                <br /><b>Población:</b>&nbsp;Oviedo
                                            <br /><b>Descripción:</b>&nbsp;Buscamos arquitecto con experiencia en entornos API.
                
                Requisitos:
                
                - Tecnologías: Experto en soluciones técnicas. Spring boot, Kafka.
                - Idiomas: Castellano, Ingles B1
                
                100% de teletrabajo.            <br /><b>Tecnologías:</b>&nbsp;Java, SpringBoot, Kafka            <br /><b>Tipo de Contrato:</b>&nbsp;
                Indefinido            <br /><b>Salario:</b>&nbsp;30000 &euro; - 45000 &euro;  Bruto/año            <br /><b>Experiencia:</b>&nbsp;
                3 años            <br /><b>Funciones:</b>&nbsp;Arquitecto TIC            <br />Descubre más: https://www.tecnoempleo.com/ejemplo-a/springboot/rf-00000000000000000001<br />
                <br /><b>Formación mínima:</b>&nbsp;FP2/Grado Superior
                """;
        DescriptionValuesRaw result = DescriptionParser.parse(html);

        assertThat(result).usingRecursiveComparison().isEqualTo(new DescriptionValuesRaw(
                "Empresa Ejemplo A",
                "100% En remoto",
                "Oviedo",
                "Buscamos arquitecto con experiencia en entornos API.\n\nRequisitos:\n\n- Tecnologías: " +
                        "Experto en soluciones técnicas. Spring boot, Kafka.\n- Idiomas: Castellano, Ingles B1\n" +
                        "\n100% de teletrabajo.",
                "Java, SpringBoot, Kafka",
                "Indefinido",
                "30000 € - 45000 € Bruto/año",
                "3 años",
                "Arquitecto TIC",
                "FP2/Grado Superior"
        ));
    }

    @Test
    void extractsAllFieldsFromItemwithMissingFields() {
        String html = """
                <b>Empresa:</b>&nbsp;Empresa Ejemplo A                <br /><b>Provincia:</b>&nbsp;100% En remoto                <br /><b>Población:</b>&nbsp;
                                            <br /><b>Descripción:</b>&nbsp;Buscamos arquitecto con experiencia en entornos API.
                
                Requisitos:
                
                - Tecnologías: Experto en soluciones técnicas. Spring boot, Kafka.
                - Idiomas: Castellano, Ingles B1
                
                100% de teletrabajo.            <br /><b>Tecnologías:</b>&nbsp;Java, SpringBoot, Kafka            <br /><b>Tipo de Contrato:</b>&nbsp;
                Indefinido            <br /><b>Salario:</b>&nbsp;30000 &euro; - 45000 &euro;  Bruto/año            <br /><b>Experiencia:</b>&nbsp;
                3 años            <br /><b>Funciones:</b>&nbsp;Arquitecto TIC            <br />Descubre más: https://www.tecnoempleo.com/ejemplo-a/springboot/rf-00000000000000000001<br />
                """;
        DescriptionValuesRaw result = DescriptionParser.parse(html);

        assertThat(result).usingRecursiveComparison().isEqualTo(new DescriptionValuesRaw(
                "Empresa Ejemplo A",
                "100% En remoto",
                null,
                "Buscamos arquitecto con experiencia en entornos API.\n\nRequisitos:\n\n- Tecnologías: " +
                        "Experto en soluciones técnicas. Spring boot, Kafka.\n- Idiomas: Castellano, Ingles B1\n" +
                        "\n100% de teletrabajo.",
                "Java, SpringBoot, Kafka",
                "Indefinido",
                "30000 € - 45000 € Bruto/año",
                "3 años",
                "Arquitecto TIC",
                null
        ));
    }

    @Test
    void emptyHtmlGivesAllNullFields(){
        DescriptionValuesRaw result = DescriptionParser.parse("");

        assertThat(result).usingRecursiveComparison().isEqualTo(new DescriptionValuesRaw(
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
        ));

    }
}
