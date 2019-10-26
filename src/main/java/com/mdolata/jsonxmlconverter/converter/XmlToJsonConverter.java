package com.mdolata.jsonxmlconverter.converter;

import com.mdolata.jsonxmlconverter.model.Attribute;
import com.mdolata.jsonxmlconverter.model.Element;
import com.mdolata.jsonxmlconverter.model.ElementFactory;
import com.mdolata.jsonxmlconverter.model.Value;

import java.util.*;
import java.util.stream.Collectors;

public class XmlToJsonConverter implements Converter {

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

        Optional<String> tmp = value.getXmlValue();
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
                .map(Value::withXmlValue)
                .orElse(Value.withRawValue(value.orElse(null)));
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
