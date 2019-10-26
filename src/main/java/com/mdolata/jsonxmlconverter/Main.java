package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.converter.Converter;
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

    static class ConverterFactory {
        private final static Converter xmlConverter = new XmlToJsonConverter();
        private final static Converter jsonConverter = new JsonToXmlConverter();

        public static Converter getConverter(String input) {
            if (isXml(input)) return xmlConverter;
            else if (isJson(input)) return jsonConverter;
            else {
                Converter converter = new Converter() {
                    @Override
                    public String convert(String input) {
                        return input;
                    }

                    @Override
                    public List<Element> convert2Elements(String input, String key) {
                        return List.of();
                    }
                };
                return converter;
            }
        }

        private static boolean isJson(String input) {
            return input.trim().startsWith("{") && input.trim().endsWith("}");
        }

        private static boolean isXml(String input) {
            return input.trim().startsWith("<") && input.trim().endsWith(">");
        }
    }

    static class XmlToJsonConverter implements Converter {

        @Override
        public String convert(String xml) {
            Map<String, Optional<String>> map = new HashMap<>();

            String tagName = getTagName(xml);
            Optional<String> value = getValue(xml, tagName);
            List<Attribute> attributes = getAttributes(xml, tagName);

            map.put(tagName, value);

            return convertMapToJson(map, tagName, attributes);
        }

        @Override
        public List<Element> convert2Elements(String xml, String key) {

            String currentTag = getTagName(xml);
            String tagName = ((key.isEmpty()) ? "" : key + ", ") + currentTag;
            Value value = getValueOrNestedXml(xml, currentTag);
            List<Attribute> attributes = getAttributes(xml, currentTag);

            Element element = ElementFactory.fromAll(tagName, value, attributes);

            List<Element> elements = new ArrayList<>(List.of(element));

            Optional<String> tmp = value.xmlValue;
            if (tmp.isPresent()) {
                while(hasNextTag(tmp)) {
                    String nextTagName = getNextTagName(tmp.get());
                    String nextTag = getNextTag(tmp.get(), nextTagName);
                    List<Element> elements1 = convert2Elements(nextTag, tagName);
                    elements.addAll(elements1);

                    int i = tmp.get().indexOf(nextTag)+nextTag.length();
                    tmp = Optional.of(tmp.get().substring(i));
                }
            }
            return elements;
        }

        private boolean hasNextTag(Optional<String> tmp) {
            return tmp.get().trim().startsWith("<") && tmp.get().trim().endsWith(">");
        }

        private String getNextTag(String tag, String nextTagName) {
            boolean b = hasValue(tag);
            String closedTag;
            if (b) {
                closedTag = String.format("</%s>", nextTagName);
            } else {
                closedTag = "/>";
            }
            int endIndex = tag.indexOf(closedTag);

            return tag.substring(0, endIndex) + closedTag;
        }

        private String getNextTagName(String value) {
            return getTagName(value);
        }

        private Value getValueOrNestedXml(String xml, String tagName) {
            return mapValue(getValue(xml, tagName));
        }

        private Value mapValue(Optional<String> value) {
            return value
                    .filter(ConverterFactory::isXml)
                    .map(v -> Value.withXmlValue(Optional.ofNullable(v)))
                    .orElse(Value.withRawValue(value));
        }

        private List<Attribute> getAttributes(String xml, String tagName) {
            int indexOfStartAttributes = xml.indexOf(String.format("<%s", tagName)) + String.format("<%s", tagName).length();
            String closeTag = (hasValue(xml)) ? ">" : "/>";
            int indexOfEndAttributes = xml.indexOf(closeTag);

            ArrayList<Attribute> attributes = new ArrayList<>();

            String rawAttributes = xml.substring(indexOfStartAttributes, indexOfEndAttributes).trim();

            while (isNextAttribute(rawAttributes)) {
                String name = findNextAttribute(rawAttributes);
                String value = getNextAttributeValue(rawAttributes);

                Attribute attribute = new Attribute(name, value);
                attributes.add(attribute);

                rawAttributes = removeAttributeWithValue(rawAttributes, name, value);
            }


            return attributes;
        }

        private String removeAttributeWithValue(String rawAttributes, String attribute, String value) {
            return rawAttributes.replaceFirst(attribute, "")
                    .replaceFirst("=", "")
                    .replaceFirst(String.format("\"%s\"", value), "")
                    .trim();
        }

        private String getNextAttributeValue(String rawAttributes) {
            int positionOfEqualSign = rawAttributes.indexOf("=");
            String withoutAttribute = rawAttributes.substring(positionOfEqualSign + 1).trim().replaceFirst("\"", "");

            String secondQuotePosition = withoutAttribute
                    .chars()
                    .mapToObj(value -> String.valueOf((char) value))
                    .takeWhile(s -> !s.equals("\""))
                    .collect(Collectors.joining());
            return secondQuotePosition.replaceAll("\"", "");
        }

        private boolean isNextAttribute(String rawAttributes) {
            return rawAttributes.contains("=");
        }

        private String findNextAttribute(String rawAttributes) {
            int positionOfEqualSign = rawAttributes.indexOf("=");
            return rawAttributes.substring(0, positionOfEqualSign).trim();
        }

        private String convertMapToJson(Map<String, Optional<String>> map, String tagName, List<Attribute> attributes) {
            StringBuilder stringBuilder = new StringBuilder(String.format("{\"%s\":{", tagName));
            attributes.forEach(attribute -> stringBuilder.append(String.format("\"@%s\":\"%s\",", attribute.name, attribute.value)));
            map.forEach((key, value) -> stringBuilder.append(String.format("\"#%s\":%s", key, (value.isEmpty()) ? null : "\"" + value.get() + "\"")));
            return stringBuilder.append("}}").toString();
        }

        private Optional<String> getValue(String xml, String tagName) {
            if (!hasValue(xml)) return Optional.empty();

            int indexOfEnd = xml.indexOf(">");

            return Optional.of(xml
                    .substring(indexOfEnd + 1)
                    .replace(String.format("</%s>", tagName), ""));
        }

        private boolean hasValue(String xml) {
            int indexOfCloseTag = xml.indexOf(">");
            return xml.charAt(indexOfCloseTag - 1) != '/';
        }

        private String getTagName(String xml) {
            String trimmedXml = xml.trim();
            String closeTag = (hasValue(xml)) ? ">" : "/>";

            int indexOfOpenTag = trimmedXml.indexOf("<");
            int indexOfCloseTag = trimmedXml.indexOf(closeTag);
            String tagNameCandidate = trimmedXml
                    .substring(indexOfOpenTag + 1, indexOfCloseTag);

            String finalTagName;
            if (tagNameCandidate.contains(" ")) {
                int separatorIndex = tagNameCandidate.indexOf(" ");
                finalTagName = tagNameCandidate.substring(0, separatorIndex);
            } else {
                finalTagName = tagNameCandidate;
            }
            return finalTagName;
        }
    }

    static class JsonToXmlConverter implements Converter {

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

