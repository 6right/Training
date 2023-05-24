package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Main {
    private static final String CSV_FILE_PATH = "C:\\Users\\bright_pc\\Desktop\\Coding\\createCSV\\new\\data.csv";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";


    public static void main(String[] args) {
        generateCSVFile();
    }

    private static void generateCSVFile() {
        try {
            FileWriter writer = new FileWriter(CSV_FILE_PATH);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("user_id,task_id,work_duration,date,comment");
            bufferedWriter.newLine();

            Random random = new Random();
            int rows = 500_000;
            int numberOfUsers = 20;
            int numberOfTasks = 100;


            for (int i = 0; i < rows; i++) {
                int userId = random.nextInt(numberOfUsers) + 1;
                int taskId = random.nextInt(numberOfTasks) + 1;
                int workDuration = random.nextInt(8) + 1;
                Timestamp date = new Timestamp(new Date().getTime());
                String comment = generateRandomWord();

                // Write CSV data
                bufferedWriter.write(userId + "," + taskId + "," + workDuration + "," + date + "," + comment);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomWord() {
        Random random = new Random();
        int length = random.nextInt(100 - 1 + 1) + 1;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}