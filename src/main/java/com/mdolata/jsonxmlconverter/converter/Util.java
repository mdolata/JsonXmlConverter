package com.mdolata.jsonxmlconverter.converter;

public class Util {
    public static boolean isJson(String input) {
        return input.trim().startsWith("{") && input.trim().endsWith("}");
    }

    public static boolean isXml(String input) {
        return input.trim().startsWith("<") && input.trim().endsWith(">");
    }
}
