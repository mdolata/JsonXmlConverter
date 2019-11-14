package com.mdolata.jsonxmlconverter.converter;

import com.mdolata.jsonxmlconverter.model.Element;

import java.util.List;

public class ConverterFactory {
    private final static Converter xmlConverter = new XmlToJsonConverter();
    private final static Converter jsonConverter = new JsonToXmlConverter();

    public static Converter getConverter(String input) {
        if (Util.isXml(input)) return xmlConverter;
        else if (Util.isJson(input)) return jsonConverter;
        else {
            return new Converter() {
                @Override
                public String convert(String input1) {
                    return input1;
                }

                @Override
                public List<Element> convert2Elements(String input1, String key) {
                    return List.of();
                }
            };
        }
    }

}
