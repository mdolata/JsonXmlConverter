package com.mdolata.jsonxmlconverter.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Element {
    public final String key;
    public final Optional<String> value;
    public final Value newValue;
    public final List<Attribute> attributes;

    @Deprecated
    public Element(String key, Optional<String> value, List<Attribute> attributes) {
        this.key = key;
        this.value = value;
        this.attributes = attributes;
        this.newValue = Value.empty();
    }

    public Element(String key, Value value, List<Attribute> attributes) {
        this.key = key;
        this.value = Optional.empty();
        this.attributes = attributes;
        this.newValue = value;
    }


    @Override
    public String toString() {
        return "Element:\n" +
                "path = " + this.key + "\n" +
                valueToString() +
                ((!attributes.isEmpty()) ? "attributes:\n" + attributesToString() : "");
//            return "Element{" +
//                    "key='" + key + '\'' +
//                    ", value=" + value +
//                    ", val=" + newValue +
//                    ", attributes=" + attributes +
//                    '}';
    }

    private String valueToString() {
        if (newValue.getRawValue().isEmpty() && newValue.getXmlValue().isEmpty()) return "value = null\n";
        return (newValue.getRawValue().isPresent()) ? ("value = \"" + newValue.getRawValue().get() + "\"\n") : "";
    }

    private String attributesToString() {
        StringBuilder tmp = new StringBuilder();

        for (Attribute attribute : attributes) {
            tmp.append(String.format("%s = \"%s\"\n", attribute.name, attribute.value));
        }

        return tmp.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (!Objects.equals(key, element.key)) return false;
        if (!Objects.equals(value, element.value)) return false;
        if (!Objects.equals(newValue, element.newValue)) return false;
        return Objects.equals(attributes, element.attributes);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (newValue != null ? newValue.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }
}