package com.mdolata.jsonxmlconverter.model;

import java.util.Objects;
import java.util.Optional;

public class Value {
    private final String rawValue;
    private final String xmlValue;

    private Value(String rawValue, String xmlValue) {
        this.rawValue = rawValue;
        this.xmlValue = xmlValue;
    }

    public static Value empty() {
        return new Value(null, null);
    }

    public static Value withRawValue(String value) {
        return new Value(value, null);
    }

    public static Value withXmlValue(String value) {
        return new Value(null, value);
    }

    public Optional<String> getRawValue() {
        return Optional.ofNullable(rawValue);
    }

    public Optional<String> getXmlValue() {
        return Optional.ofNullable(xmlValue);
    }

    @Override
    public String toString() {
        return "Value{" +
                "rawValue=" + rawValue +
                ", xmlValue=" + xmlValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value = (Value) o;

        if (!Objects.equals(rawValue, value.rawValue)) return false;
        return Objects.equals(xmlValue, value.xmlValue);
    }

    @Override
    public int hashCode() {
        int result = rawValue != null ? rawValue.hashCode() : 0;
        result = 31 * result + (xmlValue != null ? xmlValue.hashCode() : 0);
        return result;
    }
}
