package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.converter.Converter;
import com.mdolata.jsonxmlconverter.converter.ConverterFactory;
import com.mdolata.jsonxmlconverter.model.Attribute;
import com.mdolata.jsonxmlconverter.model.Element;
import com.mdolata.jsonxmlconverter.model.ElementFactory;
import com.mdolata.jsonxmlconverter.model.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) throws IOException {

        String input = Files.readAllLines(Paths.get("test.txt"))
                .stream()
                .collect(Collectors.joining());


        Converter converter = ConverterFactory.getConverter(input);

        List<Element> result = converter.convert2Elements(input, "");

        for (Element element : result) {

            System.out.println(element);
        }
    }

    public static class JsonToXmlConverter implements Converter {

        @Override
        public String convert(String json) {
            Element map = convertToMap(json);
            return convertMapToXml(map);
        }

        @Override
        public List<Element> convert2Elements(String input, String key) {
            return List.of();
        }

        private Element convertToMap(String json) {

            String keyName = getKeyName(json);
            Optional<String> value = getValue(json, keyName);
            List<Attribute> attributes = getAttributes(json);

            return new Element(keyName, value, attributes);
        }

        private List<Attribute> getAttributes(String json) {
            String val = revealValue(json);
            String[] parameters = val.split(",");
            ArrayList<Attribute> attributes = new ArrayList<>();

            for (String param : parameters) {
                if (param.trim().startsWith("\"@")) {
                    String[] split = param.split(":");
                    String name = split[0].trim()
                            .replaceFirst("@", "")
                            .replace("\"", "");

                    String value = split[1].trim()
                            .replace("\"", "");

                    Attribute attribute = new Attribute(name, value);
                    attributes.add(attribute);
                }
            }

            return attributes;
        }

        private String revealValue(String json) {
            int firstBracket = json.indexOf("{");
            String substring = json.substring(firstBracket + 1);
            int secondBracket = substring.indexOf("{");
            int lastBracket = substring.lastIndexOf("}");

            return substring.substring(secondBracket + 1, lastBracket - 1).trim();
        }


        private String convertMapToXml(Element element) {
            String attributes = element.attributes.stream()
                    .map(attribute -> String.format("%s = \"%s\"", attribute.name, attribute.value))
                    .collect(Collectors.joining(" "));

            if (!attributes.isBlank()) attributes = " " + attributes;

            if (element.value.isEmpty())
                return String.format("<%s%s/>", element.key, (attributes.isBlank()) ? "" : attributes + " ");
            return String.format("<%s%s>%s</%s>", element.key, attributes, element.value.get(), element.key);
        }

        private Optional<String> getValue(String json, String keyName) {
            int indexOfStartKey = json.indexOf("#" + keyName);
            int indexOfEndValue = json.indexOf("}");

            String thisJson = json.substring(indexOfStartKey, indexOfEndValue);

            int indexOfStartValue = thisJson.indexOf(":");
            String thisValue = thisJson.substring(indexOfStartValue + 1).trim();

            if (!thisValue.contains("\""))
                try {
                    int i = Integer.parseInt(thisValue);
                    return Optional.of(String.valueOf(i));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }

            return Optional.of(thisValue.trim().substring(1, thisValue.trim().length() - 1));
        }

        private String getKeyName(String json) {
            int indexOfOpen = json.indexOf("\"");
            int indexOfClose = json.indexOf("\"", indexOfOpen + 1);
            return json.substring(indexOfOpen + 1, indexOfClose);
        }
    }
}

