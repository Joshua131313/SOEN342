package ca.soen342.taskmanager.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ca.soen342.taskmanager.enums.DayOfWeek;

public class InputHelper {
    private Scanner scanner;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public InputHelper() {
        scanner = new Scanner(System.in);
    }

    public String askString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    public int askInt(String message) {
        System.out.println(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid number. Try again:");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public boolean askBoolean(String message) {
        System.out.println(message + " (true/false)");
        boolean value = scanner.nextBoolean();
        scanner.nextLine();
        return value;
    }
    public <T extends Enum<T>> T askOption(String message, Class<T> enumType) {

        while (true) {

            System.out.println(message);
            System.out.println("Available options:");

            for (T constant : enumType.getEnumConstants()) {
                System.out.println("- " + constant.name());
            }

            String input = scanner.nextLine().trim().toUpperCase();

            try {
                return Enum.valueOf(enumType, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public LocalDate askDate(String message) {
        while (true) {
            System.out.println(message + " (yyyy-MM-dd):");
            String input = scanner.nextLine();

            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
        }
    }
    public List<DayOfWeek> askWeekDays(String message) {
        DayOfWeek[] days = DayOfWeek.values();
        List<DayOfWeek> selectedDays = new ArrayList<>();

        while (true) {
            System.out.println(message);
            System.out.println("Select one more more days (separate by commas): ");
            for(int i = 0; i < days.length; i++) {
                System.out.println((i + 1) + ": " + days[i]);
            }
            String input = scanner.nextLine();
            String[] parts = input.split(",");
            selectedDays.clear();
            try {
                for(String part : parts) {
                    int choice = Integer.parseInt(part.trim());
                    if(choice < 1 || choice > days.length) {
                        throw new NumberFormatException();
                    }
                    selectedDays.add(days[choice - 1]);
                }
                if(!selectedDays.isEmpty()) {
                    return selectedDays;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
    public int askDayOfMonth(String message) {
        while(true) {
            int day = askInt(message);
            if(day >= 1 && day <= 31) {
                return day;
            }

            System.out.println("Invalid day. Enter a valid day.");
        }
    }
    public List<String> askTags(String message) {
        String tagInput = askString(
            "Enter tags (comma separated):"
        );

        List<String> tagNames =
            Arrays.stream(tagInput.split(","))
                .map(String::trim)
                .toList();
        return tagNames;
    }
    public void closeScanner() {
        scanner.close();
    }
}
