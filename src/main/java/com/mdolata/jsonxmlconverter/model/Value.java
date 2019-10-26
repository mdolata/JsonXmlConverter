package com.mdolata.jsonxmlconverter.model;

import java.util.Objects;
import java.util.Optional;

public class Value {
    private final String rawValue;
    public final Optional<String> xmlValue;

    private Value(String rawValue, Optional<String> xmlValue) {
        this.rawValue = rawValue;
        this.xmlValue = xmlValue;
    }

    public static Value empty() {
        return new Value(null, Optional.empty());
    }

    public static Value withRawValue(String value) {
        return new Value(value, Optional.empty());
    }

    public static Value withXmlValue(Optional<String> value) {
        return new Value(null, value);
    }

    public Optional<String> getRawValue() {
        return Optional.ofNullable(rawValue);
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
