package com.robertmartinezlewis.job_tracker.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DescriptionParser {

    public static DescriptionValuesRaw parse(String html){
        Document document = Jsoup.parse(html);

        String company = valueOf(document, "Empresa");
        String province = valueOf(document, "Provincia");
        String town = valueOf(document, "Población");
        String description = wholeValueOf(document, "Descripción");
        String technologies = valueOf(document, "Tecnologías");
        String contractType = valueOf(document, "Tipo de Contrato");
        String salary = valueOf(document, "Salario");
        String experience = valueOf(document, "Experiencia");
        String functions = valueOf(document, "Funciones");
        String minimumEducation = valueOf(document, "Formación mínima");

        return new DescriptionValuesRaw(company, province, town, description, technologies, contractType, salary, experience, functions, minimumEducation);
    }

    private static TextNode findValueNode(Document document, String label) {
        for (Element bold : document.select("b")) {
            if (bold.text().equals(label+":")) {
                if (bold.nextSibling() instanceof TextNode textNode) {
                    return textNode;
                }
            }
        }
        return null;
    }

    private static String wholeValueOf(Document document, String label) {
        TextNode node = findValueNode(document, label);
        if (node == null) {
            return null;
        }
        String text = node.getWholeText().replace('\u00A0', ' ');
        return text.isBlank() ? null : text.strip();
    }

    private static String valueOf(Document document, String label) {
        TextNode node = findValueNode(document, label);
        if (node == null) {
            return null;
        }
        String text = node.text();
        return text.isBlank() ? null : text.strip();
    }


}
