package com.robertmartinezlewis.job_tracker.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class DescriptionParser {

    public static DescriptionValuesRaw parse(String html){
        Document document = Jsoup.parse(html);

        String company = null;
        for (Element label : document.select("b")) {
            if (label.text().equals("Empresa:")) {
                Node valueNode = label.nextSibling();
                if (valueNode instanceof TextNode textNode) {
                    company = textNode.text().strip();
                }
            }
        }

        return new DescriptionValuesRaw(company, null, null, null, null, null, null, null, null, null);
    }
}
