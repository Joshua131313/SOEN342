package ca.soen342.taskmanager;
import java.util.*;

import ca.soen342.taskmanager.db.DBInit;
import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Category;
import ca.soen342.taskmanager.enums.Status;
import ca.soen342.taskmanager.gateway.ICSExportGateway;
import ca.soen342.taskmanager.interfaces.CalenderExportGateway;
import ca.soen342.taskmanager.service.*;
import ca.soen342.taskmanager.tables.*;
import ca.soen342.taskmanager.ui.TaskUI;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        DBInit.init();

        ProjectsTable projectsTable = new ProjectsTable();
        TasksTable tasksTable = new TasksTable();
        CollaboratorsTable collaboratorsTable = new CollaboratorsTable();

        TagsService tagsService = new TagsService();
        TaskService taskService = new TaskService(tagsService);
        TaskUI taskUI = new TaskUI();

        boolean running = true;

        while (running) {

            List<Project> projects = projectsTable.getAllProjects();
            List<Task> tasks = tasksTable.getAllTasks();
            List<Collaborator> collaborators = collaboratorsTable.getAllCollaborators();

            printMenu();
            int choice = getIntInput("Select an option: ");

            switch (choice) {

                case 1 -> {
                    System.out.println("Enter file path: ");
                    String path = scanner.nextLine();

                    List<Task> imported = ImportToCSV.importTasks(path, projects, collaborators);

                    for (Task t : imported) {
                        tasksTable.addTask(t, null);
                    }

                }

                case 2 -> {
                    System.out.println("Enter file path: ");
                    String path = scanner.nextLine();

                    ExportToCSV.exportTasks(path, tasks, collaborators);
                    System.out.println(tasks.size() + " tasks exported.");
                }

                case 3 -> {
                    Task createdTask = taskUI.createTask();

                    tasksTable.addTask(createdTask, null);

                    System.out.println("Task created and saved to DB.");
                }

                case 4 -> {
                    System.out.println("Enter name (or leave blank): ");
                    String name = scanner.nextLine();

                    System.out.println("Enter status (Open, Completed, Cancelled) or leave blank: ");
                    String statusInput = scanner.nextLine();

                    Status status = null;
                    if (!statusInput.isEmpty()) {
                        status = Status.valueOf(statusInput.toUpperCase());
                    }

                    List<Task> results = TaskService.searchTasks(
                            tasks,
                            name.isEmpty() ? null : name,
                            status,
                            null,
                            null);

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

                    Collaborator collaborator = findCollaboratorByName(collaborators, name);

                    if (collaborator == null) {
                        System.out.println("Enter category (JUNIOR, INTERMEDIATE, SENIOR):");
                        Category category = Category.valueOf(scanner.nextLine().toUpperCase());

                        collaborator = new Collaborator(name, category);

                        collaboratorsTable.addCollaborator(collaborator);
                    }

                    try {
                        taskService.assignCollaborator(task, collaborator);

                        System.out.println("Collaborator assigned.");

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                case 6 -> {
                    System.out.println("\n--- All tasks (DB) ---");
                    tasks.forEach(System.out::println);
                }

                case 7 -> {
                    CalenderExportGateway gateway = new ICSExportGateway();
                    CalendarExportService calendarExportService = new CalendarExportService(gateway);

                    System.out.println("Export ICS (all tasks)");
                    calendarExportService.export(tasks);
                }

                case 8 -> {
                    List<Collaborator> overloaded = TaskService.getOverloadedCollaborators(collaborators);

                    System.out.println("\n--- Overloaded Collaborators ---");

                    for (Collaborator c : overloaded) {
                        int openCount = (int) c.getSubTasks().stream()
                                .filter(s -> s.getStatus() != Status.COMPLETED)
                                .count();

                        System.out.printf(
                                "%s (%s) -> %d / %d%n",
                                c.getName(),
                                c.getCategory(),
                                openCount,
                                c.getLimit());
                    }
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
        System.out.println("1. Import Tasks (to DB)");
        System.out.println("2. Export Tasks");
        System.out.println("3. Create Task");
        System.out.println("4. Search Tasks");
        System.out.println("5. Assign Collaborator");
        System.out.println("6. View All Tasks");
        System.out.println("7. Export to ICS");
        System.out.println("8. List Overloaded Collaborators");
        System.out.println("0. Exit");
    }

    private static int getIntInput(String message) {
        System.out.print(message);
        return Integer.parseInt(scanner.nextLine());
    }

    private static Task selectTask(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + ": " + tasks.get(i).getTitle());
        }

        int index = getIntInput("Enter index: ");
        return tasks.get(index);
    }

    private static Collaborator findCollaboratorByName(List<Collaborator> collaborators, String name) {
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getName().equalsIgnoreCase(name)) {
                return collaborator;
            }
        }
        return null;
    }
}