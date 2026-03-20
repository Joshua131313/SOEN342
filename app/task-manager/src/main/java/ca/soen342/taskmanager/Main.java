package ca.soen342.taskmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Category;
import ca.soen342.taskmanager.enums.Frequency;
import ca.soen342.taskmanager.enums.Status;
import ca.soen342.taskmanager.service.ExportToCSV;
import ca.soen342.taskmanager.service.ImportToCSV;
import ca.soen342.taskmanager.service.TagsService;
import ca.soen342.taskmanager.service.TaskService;
import ca.soen342.taskmanager.ui.TaskUI;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        List<Project> projects = new ArrayList<>();
        List<Collaborator> collaborators = new ArrayList<>();

        TagsService tagsService = new TagsService();
        TaskService taskService = new TaskService(tagsService);
        TaskUI taskUI = new TaskUI();

        List<Task> tasks = new ArrayList<>();

        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Select an option: ");

            switch (choice) {

                case 1 -> {
                    // Import
                    System.out.println("Enter file path: ");
                    String path = scanner.nextLine();
                    tasks = ImportToCSV.importTasks(path, projects, collaborators);
                    System.out.println(tasks.size() + " tasks imported successfully.");
                }

                case 2 -> {
                    // Export
                    System.out.println("Enter file path: ");
                    String path = scanner.nextLine();
                    ExportToCSV.exportTasks(path, tasks, projects, collaborators);
                    System.out.println(tasks.size() + " tasks exported successfully.");
                }

                case 3 -> {
                    // Create Task
                    taskUI.createTask();
                }

                case 4 -> {
                    // Search Tasks
                    System.out.println("Enter name (or leave blank): ");
                    String name = scanner.nextLine();

                    System.out.println("Enter status (Open, Completed, Cancelled) or leave blank: ");
                    String statusInput = scanner.nextLine();

                    Status status = null;
                    if (!statusInput.isEmpty()) {
                        status = Status.valueOf(statusInput.toUpperCase());
                    }

                    List<Task> results = TaskService.searchTasks(tasks, 
                        name.isEmpty() ? null : name, 
                        status, 
                        null, 
                        null
                    );

                    System.out.println("\n--- Results ---");
                    results.forEach(System.out::println);
                }

                case 5 -> {
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks available.");
                        break;
                    }
                    Task task = selectTask(tasks);
                    System.out.println("Enter collaborator name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter category (JUNIOR, INTERMEDIATE, SENIOR):");
                    Category category = Category.valueOf(scanner.nextLine().toUpperCase());
                    Collaborator collaborator = new Collaborator(name, category);
                    try {
                        taskService.assignCollaborator(task, collaborator);
                        System.out.println("Collaborator assigned.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                case 6 -> {
                    // View all tasks
                    System.out.println("\n--- All tasks ---");
                    tasks.forEach(System.out::println);
                }

                case 0 -> {
                    running = false;
                    System.out.println("Exiting...");
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== TASK MANAGER CLI =====");
        System.out.println("1. Import Tasks from CSV");
        System.out.println("2. Export Tasks to CSV");
        System.out.println("3. Create Task");
        System.out.println("4. Search Tasks");
        System.out.println("5. Assign Collaborator");
        System.out.println("6. View All Tasks");
        System.out.println("7. Create Sample Recurring Task");
        System.out.println("0. Exit");
    }

    private static int getIntInput(String message) {
        System.out.print(message);
        return Integer.parseInt(scanner.nextLine());
    }

    private static Task selectTask(List<Task> tasks) {
        System.out.println("\nSelect a task:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + ": " + tasks.get(i).getTitle());
        }

        int index = getIntInput("Enter index: ");
        return tasks.get(index);
    }
}