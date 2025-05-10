package util;

import java.util.Scanner;

public class ConsoleHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readIntFromConsole(String prompt, int min, int max) {
        int choice;
        while (true) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static double readDoubleFromConsole(String prompt, double min) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min) {
                    return value;
                } else {
                    System.out.println("Please enter a value greater than or equal to " + min);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String readStringFromConsole(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static void printMenuHeader(String title) {
        String line = createRepeatedString("=", 50);
        System.out.println("\n" + line);
        System.out.println("\t" + title);
        System.out.println(line);
    }

    public static void pause(String message) {
        System.out.println(message);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private static String createRepeatedString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}