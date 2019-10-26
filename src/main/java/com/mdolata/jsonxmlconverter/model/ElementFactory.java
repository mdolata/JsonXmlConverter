package com.mdolata.jsonxmlconverter.model;

import com.mdolata.jsonxmlconverter.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElementFactory {
    public static Element fromPath(String key) {
        return new Element(key, Optional.empty(), List.of());
    }

    @Deprecated
    public static Element fromPathAndValue(String key, Optional<String> value) {
        return new Element(key, value, new ArrayList<>());
    }

    public static Element fromPathAndValue(String key, Value value) {
        return new Element(key, value, new ArrayList<>());
    }

    public static Element fromPathAndAttributes(String key, List<Attribute> attributes) {
        return new Element(key, Optional.empty(), attributes);
    }

    @Deprecated
    public static Element fromAll(String key, Optional<String> value, List<Attribute> attributes) {
        return new Element(key, value, attributes);
    }

    public static Element fromAll(String key, Value value, List<Attribute> attributes) {
        return new Element(key, value, attributes);
    }
}
