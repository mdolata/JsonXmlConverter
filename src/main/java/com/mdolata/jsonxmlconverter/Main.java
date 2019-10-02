package com.mdolata.jsonxmlconverter;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String parametersList = scanner.nextLine().split("\\?")[1];

        String[] params = parametersList.split("&");

        Stream.of(params)
                .map(param -> param.split("="))
                .forEachOrdered(entry -> {
                    String value = (entry.length == 2) ? entry[1] : "not found";
                    System.out.println(String.format("%s : %s", entry[0], value));
                });

        String pass = Stream.of(params)
                .filter(s -> s.startsWith("pass"))
                .findFirst()
                .map(s -> s.split("=")[1])
                .map(s -> String.format("password : %s", s))
                .orElse("");

        System.out.println(pass);
    }
}

