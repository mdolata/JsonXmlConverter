package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.converter.Converter;
import com.mdolata.jsonxmlconverter.converter.ConverterFactory;
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
}

