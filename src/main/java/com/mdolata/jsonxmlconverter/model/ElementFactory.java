package com.mdolata.jsonxmlconverter.model;

import com.mdolata.jsonxmlconverter.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElementFactory {
    public static Main.Element fromPath(String key) {
        return new Main.Element(key, Optional.empty(), List.of());
    }

    @Deprecated
    public static Main.Element fromPathAndValue(String key, Optional<String> value) {
        return new Main.Element(key, value, new ArrayList<>());
    }

    public static Main.Element fromPathAndValue(String key, Value value) {
        return new Main.Element(key, value, new ArrayList<>());
    }

    public static Main.Element fromPathAndAttributes(String key, List<Attribute> attributes) {
        return new Main.Element(key, Optional.empty(), attributes);
    }

    @Deprecated
    public static Main.Element fromAll(String key, Optional<String> value, List<Attribute> attributes) {
        return new Main.Element(key, value, attributes);
    }

    public static Main.Element fromAll(String key, Value value, List<Attribute> attributes) {
        return new Main.Element(key, value, attributes);
    }
}
