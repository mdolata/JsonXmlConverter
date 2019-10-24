package com.mdolata.jsonxmlconverter;

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

        String result = converter.convert(input);

        System.out.println(result);
    }

    interface Converter {
        String convert(String input);
        List<Element> convert2Elements(String input);
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
                    public List<Element> convert2Elements(String input) {
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

    static class Attribute {
        final String name;
        final String value;

        Attribute(String name, String value) {
            this.name = name;
            this.value = value;
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
        public List<Element> convert2Elements(String input) {
            return List.of();
        }

        private List<Attribute> getAttributes(String xml, String tagName) {
            int indexOfStartAttributes = xml.indexOf(String.format("<%s", tagName)) + String.format("<%s", tagName).length();
            String closeTag = (hasValue(xml)) ? ">" : "/>";
            int indexOfEndAttributes = xml.indexOf(closeTag);

            ArrayList<Attribute> attributes = new ArrayList<>();

            String rawAttributes = xml.substring(indexOfStartAttributes, indexOfEndAttributes).trim();

            while(isNextAttribute(rawAttributes)){
                String name = findNextAttribute(rawAttributes);
                String value = getNextAttributeValue(rawAttributes);

                Attribute attribute = new Attribute(name, value);
                attributes.add(attribute);

                rawAttributes = removeAttributeWithValue(rawAttributes, name, value);
            }


            return attributes;
        }

        private String removeAttributeWithValue(String rawAttributes, String attribute, String value) {
            return rawAttributes.replace(attribute, "")
                    .replaceFirst("=", "")
                    .replace(String.format("\"%s\"", value), "")
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
            if (tagNameCandidate.contains(" ")){
                int separatorIndex = tagNameCandidate.indexOf(" ");
                finalTagName = tagNameCandidate.substring(0, separatorIndex);
            } else {
                finalTagName = tagNameCandidate;
            }
            return finalTagName;
        }
    }

    static class Element {
        final String key;
        final Optional<String> value;
        final List<Attribute> attributes;

        Element(String key, Optional<String> value, List<Attribute> attributes) {
            this.key = key;
            this.value = value;
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    ", attributes=" + attributes +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Element element = (Element) o;

            if (!Objects.equals(key, element.key)) return false;
            if (!Objects.equals(value, element.value)) return false;
            return Objects.equals(attributes, element.attributes);
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
            return result;
        }
    }

    static class ElementFactory {
        static Element fromPath(String key) {
            return new Element(key, Optional.empty(), List.of());
        }

        static Element fromPathAndValue(String key, Optional<String> value) {
            return new Element(key, value, List.of());
        }

        static Element fromPathAndAttributes(String key, List<Attribute> attributes) {
            return new Element(key, Optional.empty(), attributes);
        }

        static Element fromAll(String key, Optional<String> value, List<Attribute> attributes) {
            return new Element(key, value, attributes);           }
    }

    static class JsonToXmlConverter implements Converter {

        @Override
        public String convert(String json) {
            Element map = convertToMap(json);
            return convertMapToXml(map);
        }

        @Override
        public List<Element> convert2Elements(String input) {
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

            for( String param: parameters){
               if (param.trim().startsWith("\"@")){
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

            return substring.substring(secondBracket + 1, lastBracket- 1).trim();
        }


        private String convertMapToXml(Element element) {
            String attributes = element.attributes.stream()
                    .map(attribute -> String.format("%s = \"%s\"", attribute.name, attribute.value))
                    .collect(Collectors.joining(" "));

            if (!attributes.isBlank()) attributes = " " + attributes;

            if (element.value.isEmpty()) return String.format("<%s%s/>", element.key, (attributes.isBlank())? "" : attributes + " ");
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

