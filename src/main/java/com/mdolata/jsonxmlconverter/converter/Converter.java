package com.mdolata.jsonxmlconverter.converter;

import com.mdolata.jsonxmlconverter.model.Element;

import java.util.List;

public interface Converter {
    @Deprecated
    String convert(String input);

    List<Element> convert2Elements(String input, String key);
}
