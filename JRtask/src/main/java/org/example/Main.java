package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        printTheData();
    }

    public static void printTheData() {
        try {
            FileReader fileReader = new FileReader("C:\\Users\\bright_pc\\Desktop\\Coding\\Training\\createCSV\\new\\data.txt");
            BufferedReader reader = new BufferedReader(fileReader);

            
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}