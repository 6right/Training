
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.sql.Timestamp;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        sumOfTaskForEachUser();
        printTopTimeTasks();

    }

    public static void sumOfTaskForEachUser() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long memoryBefore = getUsedMemory(memoryBean);
        Timestamp start = new Timestamp(System.currentTimeMillis());
        HashMap<Integer, HashMap<Integer, Integer>> userTaskTime = new HashMap<>();

        try {
            FileReader fileReader = new FileReader("teach_call.csv");
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
            reader.close();
            fileReader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("sumOfTaskForEachUser.csv"));
            writer.write("User ID, Task ID, Time");
            writer.newLine();
            for (int userId : userTaskTime.keySet()) {
                HashMap<Integer, Integer> taskTime = userTaskTime.get(userId);
                for (int taskId : taskTime.keySet()) {
                    writer.write(userId + "," + taskId + "," + taskTime.get(taskId));
                    writer.newLine();
                }
            }
            writer.close();

            Timestamp end = new Timestamp(System.currentTimeMillis());

            System.out.println();
            double time = (double) (end.getTime() - start.getTime());
            System.out.println("Time taken: " + (time / 1000) + "sec " + "(" + (int) time + "ms)");

            long memoryAfter = getUsedMemory(memoryBean);
            System.out.println("Used memory after: " + memoryAfter / (1024 * 1024) + " mb");
            System.out.println("Memory used: " + ((memoryAfter - memoryBefore) / (1024 * 1024)) + " mb");
            System.out.println();
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
            FileReader fileReader = new FileReader("teach_call.csv");
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

            BufferedWriter writer = new BufferedWriter(new FileWriter("top10TimeTasks.csv"));
            writer.write("Task ID, Time");
            writer.newLine();
            for (int i = 0; i < 10; i++) {
                writer.write(top10[i] + "," + top10Time[i]);
                writer.newLine();
            }
            writer.close();

            Timestamp end = new Timestamp(System.currentTimeMillis());
            double time = (double) (end.getTime() - start.getTime());
            System.out.println("Time taken: " + (time / 1000) + "sec " + "(" + (int) time + "ms)");

            long memoryAfter = getUsedMemory(memoryBean);
            System.out.println("Used memory after: " + memoryAfter / (1024 * 1024) + " mb");
            System.out.println("Memory used: " + ((memoryAfter - memoryBefore) / (1024 * 1024)) + " mb");

            reader.close();
            fileReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long getUsedMemory(MemoryMXBean memoryBean) {
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        return heapMemoryUsage.getUsed();
    }
}