package com.mdolata.jsonxmlconverter;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args ){
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();

        var collect = Stream.of(word.split(""))
                .map(s -> s + s)
                .collect(Collectors.joining());

        System.out.println(collect);

    }
}
