package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) throws IOException {
        File file = new File("tasks.csv");
        if (Files.notExists(file.toPath())){
            System.err.println("File not found: " + file);
            System.err.println(file + " created");
            Files.createFile(file.toPath());

        }
        Scanner usrInput = new Scanner(System.in);
        String[][] tasks = getTasks(file);
        options();
        while (true) {
            switch (usrInput.nextLine()) {
                case "add" -> {
                    System.out.println("Add task");
                    tasks = addTask(usrInput, tasks);
                    System.out.println("Task added");
                    options();
                }
                case "remove" -> tasks = taskRemove(usrInput, tasks);
                case "list" -> getList(tasks);
                case "quit" -> {
                    System.out.println(ConsoleColors.RED + "Bye Bye");
                    taskQuit(tasks);
                    System.exit(0);
                }

                default -> {
                    System.out.println("Select option");
                    options();
                }
            }
        }


    }

    private static void taskQuit(String[][] tasks) {
        try {
            FileWriter fileWriter = new FileWriter("tasks.csv", false);
            String overwrite = null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tasks.length; i++) {
                for (int k = 0; k < 3; k++) {

                    sb.append(tasks[i][k]).append(",");
                }
                sb.append('\n');
                overwrite = sb.toString();
            }
            fileWriter.write(overwrite);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("File is missing");
        }
    }


    private static String[][] taskRemove(Scanner usrInput, String[][] tasks) {
       try {
           System.out.println("Select task number to remove: ");
           int rowToRemove = Integer.parseInt(usrInput.next());
           if (rowToRemove < 0) {
               System.out.println("Enter value greater or equal to 0");
               taskRemove(usrInput, tasks);
           } else if (rowToRemove > tasks.length - 1) {
               System.out.println("Enter value lesser or equal to " + (tasks.length - 1));
               taskRemove(usrInput, tasks);
           } else {
               tasks = ArrayUtils.remove(tasks, rowToRemove);
               System.out.println("Task removed successfully");
           }
       } catch (NumberFormatException e){
           System.out.println("Entered value is not a number");
           taskRemove(usrInput, tasks);
       }
        return tasks; // dopisać kod jeśli uzytkownik wpisze tekst
    }

    private static String[][] addTask(Scanner usrInput, String[][] tasks) {
        //dodaj Zadanie
        System.out.println("Please add task description: ");
        String addTask = usrInput.nextLine() + " ";
        //Dodaj termin
        System.out.println("Please add task due date: ");
        String addDue = usrInput.nextLine() + " ";
        //Czy zadanie jest ważne
        System.out.println("Is task important true/false: ");
        String addImportant = usrInput.nextLine() + " ";
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = addTask;
        tasks[tasks.length - 1][1] = addDue;
        tasks[tasks.length - 1][2] = addImportant;

        return tasks;
    }

    public static void getList(String[][] tasks) {
        int i = 0;
        for (String[] task : tasks) {
            System.out.print(i++ + ": ");
            for (String s : task) {
                System.out.print(s + "|");
            }
            System.out.println(" ");
        }
    }

    public static String[][] getTasks(File file) throws IOException {

        List<String> list = Files.readAllLines(file.toPath());
        Scanner scan = new Scanner(file);
        int lines = list.size();
        String[][] tasks = new String[lines][3];
        for (int i = 0; i < lines; i++) {
            String[] task = scan.nextLine().split(",");
            System.arraycopy(task, 0, tasks[i], 0, task.length);
        }
        return tasks;
    }

    private static void options() {
        String[] options = {"add", "remove", "list", "quit"};
        System.out.println(ConsoleColors.BLUE + "Please select an option:");
        for (String option : options) {
            System.out.println(ConsoleColors.RESET + option);
        }
    }
}
