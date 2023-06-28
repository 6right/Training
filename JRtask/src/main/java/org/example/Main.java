package org.example;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.sql.Timestamp;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
//        sumTheTaskTime();
        sumOfTaskForEachUser();
        printTopTimeTasks();

    }

    public static void sumOfTaskForEachUser() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long memoryBefore = getUsedMemory(memoryBean);
        Timestamp start = new Timestamp(System.currentTimeMillis());
        HashMap<Integer, HashMap<Integer, Integer>> userTaskTime = new HashMap<>();
        try {
            FileReader fileReader = new FileReader("createCSV\\new\\teach_call.csv");
            BufferedReader reader = new BufferedReader(fileReader);

            reader.readLine(); // skip the first line

            String line;
            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");
                int userId = Integer.parseInt(data[0]);
                int taskId = Integer.parseInt(data[1]);
                int timeTask = Integer.parseInt(data[2]);

                if (!userTaskTime.containsKey(userId)) {
                    HashMap<Integer, Integer> taskTime = new HashMap<>();
                    taskTime.put(taskId, timeTask);
                    userTaskTime.put(userId, taskTime);
                } else {
                    HashMap<Integer, Integer> taskTime = userTaskTime.get(userId);
                    if (!taskTime.containsKey(taskId)) {
                        taskTime.put(taskId, timeTask);
                    } else {
                        taskTime.put(taskId, taskTime.get(taskId) + timeTask);
                    }
                }
            }

            for (int userId : userTaskTime.keySet()) {
                System.out.println();
                System.out.println("User " + userId + ":");
                HashMap<Integer, Integer> taskTime = userTaskTime.get(userId);
                System.out.print("    ");
                for (int taskId : taskTime.keySet()) {
                    System.out.print("Task " + taskId + ": " + taskTime.get(taskId) + " minutes | ");
                }
            }

            Timestamp end = new Timestamp(System.currentTimeMillis());
            System.out.println();
            double time = (double) (end.getTime() - start.getTime());
            System.out.println("Time taken: " + (time / 1000) + "sec " + "(" + (int) time + "ms)");

            long memoryAfter = getUsedMemory(memoryBean);
            System.out.println("Used memory after: " + memoryAfter / (1024 * 1024) + " mb");
            System.out.println("Memory used: " + ((memoryAfter - memoryBefore) / (1024 * 1024)) + " mb");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printTopTimeTasks() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long memoryBefore = getUsedMemory(memoryBean);
        Timestamp start = new Timestamp(System.currentTimeMillis());
        HashMap<Integer, Integer> taskTime = new HashMap<>();
        try {
            FileReader fileReader = new FileReader("createCSV\\new\\teach_call.csv");
            BufferedReader reader = new BufferedReader(fileReader);

            reader.readLine(); // skip the first line

            String line;
            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");
                int taskId = Integer.parseInt(data[1]);
                int timeTask = Integer.parseInt(data[2]);

                if (!taskTime.containsKey(taskId)) {
                    taskTime.put(taskId, timeTask);
                } else {
                    taskTime.put(taskId, taskTime.get(taskId) + timeTask);
                }
            }

            int[] top10 = new int[10];
            int[] top10Time = new int[10];
            for (int i = 0; i < 10; i++) {
                int max = 0;
                int maxTime = 0;
                for (int taskId : taskTime.keySet()) {
                    if (taskTime.get(taskId) > max) {
                        max = taskTime.get(taskId);
                        maxTime = taskId;
                    }
                }
                top10[i] = maxTime;
                top10Time[i] = max;
                taskTime.remove(maxTime);
            }

            System.out.println("Top 10 tasks: ");
            for (int i = 0; i < 10; i++) {
                System.out.println("Task " + top10[i] + ": " + top10Time[i] + " hours");
            }
            Timestamp end = new Timestamp(System.currentTimeMillis());
            double time = (double) (end.getTime() - start.getTime());
            System.out.println("Time taken: " + (time / 1000) + "sec " + "(" + (int) time + "ms)");

            long memoryAfter = getUsedMemory(memoryBean);
            System.out.println("Used memory after: " + memoryAfter / (1024 * 1024) + " mb");
            System.out.println("Memory used: " + ((memoryAfter - memoryBefore) / (1024 * 1024)) + " mb");


            fileReader.close();
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static long getUsedMemory(MemoryMXBean memoryBean) {
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        return heapMemoryUsage.getUsed();
    }
}