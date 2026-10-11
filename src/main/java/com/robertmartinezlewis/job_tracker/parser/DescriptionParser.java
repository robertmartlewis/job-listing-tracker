package com.robertmartinezlewis.job_tracker.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class DescriptionParser {

    public static DescriptionValuesRaw parse(String html){
        Document document = Jsoup.parse(html);

        String company = valueOf(document, "Empresa");


        return new DescriptionValuesRaw(company, null, null, null, null, null, null, null, null, null);
    }

    private static String valueOf(Document document, String label) {
        for (Element bold : document.select("b")) {
            if (bold.text().equals(label+":")) {
                Node valueNode = bold.nextSibling();
                if (valueNode instanceof TextNode textNode) {
                    String text = textNode.text();
                    if (!text.isBlank()) {
                        return text.strip();
                    }
                }
            }
        }
        return null;
    }
}
