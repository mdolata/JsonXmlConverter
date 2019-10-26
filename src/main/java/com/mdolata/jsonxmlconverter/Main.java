package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.converter.Converter;
import com.mdolata.jsonxmlconverter.converter.ConverterFactory;
import com.mdolata.jsonxmlconverter.model.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Main {

    public static void main(String[] args) throws IOException {

        String input = String.join("", Files.readAllLines(Paths.get("test.txt")));

        Converter converter = ConverterFactory.getConverter(input);

        List<Element> result = converter.convert2Elements(input, "");

        for (Element element : result) {

            System.out.println(element);
        }
    }
}

