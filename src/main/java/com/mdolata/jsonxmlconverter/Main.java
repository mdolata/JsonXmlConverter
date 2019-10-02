package com.mdolata.jsonxmlconverter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args ){
        Scanner scanner = new Scanner(System.in);
        String[] arguments = scanner.nextLine().split(" ");
        String word = arguments[0];
        int n = Integer.parseInt(arguments[1]);

        if (word.length() > n) {
            word = word.substring(n) + word.substring(0, n);
        }
        System.out.println(word);
    }
}
