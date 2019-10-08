package com.mdolata.jsonxmlconverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Converter converter = ConverterFactory.getConverter(input);

        String result = converter.convert(input);

        System.out.println(result);
    }

    interface Converter {
        String convert(String input);
    }

    static class ConverterFactory {
        private final static Converter xmlConverter = new XmlToJsonConverter();
        private final static Converter jsonConverter = new JsonToXmlConverter();

        public static Converter getConverter(String input) {
            if (isXml(input)) return xmlConverter;
            else if (isJson(input)) return jsonConverter;
            else return (String string) -> string;
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

            map.put(tagName, value);

            return convertMapToJson(map);
        }

        private String convertMapToJson(Map<String, Optional<String>> map) {
            StringBuilder stringBuilder = new StringBuilder("{");
            map.forEach((key, value) -> stringBuilder.append(String.format("\"%s\":%s", key, (value.isEmpty()) ? null : "\"" + value.get() + "\"")));
            return stringBuilder.append("}").toString();
        }

        private Optional<String> getValue(String xml, String tagName) {
            if (!hasValue(xml)) return Optional.empty();

            return Optional.of(xml
                    .replace(String.format("<%s>", tagName), "")
                    .replace(String.format("</%s>", tagName), ""));
        }

        private boolean hasValue(String xml) {
            int indexOfCloseTag = xml.indexOf(">");
            return xml.charAt(indexOfCloseTag - 1) != '/';
        }

        private String getTagName(String xml) {
            String trim = xml.trim();
            int indexOfOpenTag = trim.indexOf("<");
            String closeTag = (hasValue(xml)) ? ">" : "/>";
            int indexOfCloseTag = trim.indexOf(closeTag);
            return trim
                    .substring(indexOfOpenTag + 1, indexOfCloseTag);
        }
    }

    static class JsonToXmlConverter implements Converter {

        @Override
        public String convert(String json) {
            Map<String, String> map = convertToMap(json);
            return convertMapToXml(map);
        }

        private Map<String, String> convertToMap(String json) {
            Map<String, String> map = new HashMap<>();

            String keyName = getKeyName(json);
            String value = getValue(json, keyName);

            map.put(keyName, value);

            return map;
        }

        private String convertMapToXml(Map<String, String> map) {
            StringBuilder stringBuilder = new StringBuilder();

            map.forEach((key, value) -> stringBuilder.append(getXmlTag(key, value)));


            return stringBuilder.toString();
        }

        private String getXmlTag(String key, String value) {
            if (value == null) return String.format("<%s/>", key);
            return String.format("<%s>%s</%s>", key, value, key);
        }

        private String getValue(String json, String keyName) {
            int indexOfStartKey = json.indexOf(keyName);
            int indexOfEndValue = json.indexOf("}");

            String thisJson = json.substring(indexOfStartKey, indexOfEndValue);

            int indexOfStartValue = thisJson.indexOf(":");
            String thisValue = thisJson.substring(indexOfStartValue + 1).trim();

            if (!thisValue.contains("\"")) return null;

            return thisValue.trim().substring(1, thisValue.trim().length() - 1);
        }

        private String getKeyName(String json) {
            int indexOfOpen = json.indexOf("\"");
            int indexOfClose = json.indexOf("\"", indexOfOpen + 1);
            return json.substring(indexOfOpen + 1, indexOfClose);
        }
    }
}

