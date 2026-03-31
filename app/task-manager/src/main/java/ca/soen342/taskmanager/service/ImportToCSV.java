package ca.soen342.taskmanager.service;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.SubTask;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Category;
import ca.soen342.taskmanager.enums.Status;

public class ImportToCSV {

    private static enum Column {
        TASK_NAME("TaskName", 0, false),
        DESCRIPTION("Description", 1, true),
        SUBTASK("Subtask", 2, true),
        STATUS("Status", 3, false),
        PRIORITY("Priority", 4, false),
        DUE_DATE("DueDate", 5, true),
        PROJECT_NAME("ProjectName", 6, true),
        PROJECT_DESCRIPTION("ProjectDescription", 7, true),
        COLLABORATOR("Collaborator", 8, true),
        COLLABORATOR_CATEGORY("CollaboratorCategory", 9, true);

        private final String name;
        private final int order;
        private final boolean optional;

        Column(String name, int order, boolean optional) {
            this.name = name;
            this.order = order;
            this.optional = optional;
        }

        public String getName() { return name; }
        public int getOrder() { return order; }
        public boolean isOptional() { return optional; }
    }

    private static String extractColumn(List<String> row, Column column) {
        String data = row.get(column.getOrder()).trim();

        if (data.isBlank() && !column.isOptional()) {
            throw new IllegalArgumentException("- Missing required column: " + column.getName());
        }

        return data;
    }

    public static List<Task> importTasks(String filePath, List<Project> projects, List<Collaborator> collaborators) {

        List<Task> tasks = new ArrayList<>();
        int currentRow = 1;
        int skippedRows = 0;

        System.out.println("Import Log");

        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip the header of the csv file 
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                System.out.println("Processing line: " + currentRow);

                try {

                    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    List<String> row = Arrays.asList(tokens);

                    // Extract fields
                    String taskName = extractColumn(row, Column.TASK_NAME);
                    String description = extractColumn(row, Column.DESCRIPTION);
                    String subTaskStr = extractColumn(row, Column.SUBTASK);

                    Status status = Status.fromString(extractColumn(row, Column.STATUS));

                    int priorityLevel = Integer.parseInt(extractColumn(row, Column.PRIORITY));

                    String dueStr = extractColumn(row, Column.DUE_DATE);

                    String projectName = extractColumn(row, Column.PROJECT_NAME);
                    String projectDescription = extractColumn(row, Column.PROJECT_DESCRIPTION);

                    String collaboratorName = extractColumn(row, Column.COLLABORATOR);
                    String categoryStr = extractColumn(row, Column.COLLABORATOR_CATEGORY);

                    LocalDate dueDate = null;
                    if (!dueStr.isBlank()) {
                        dueDate = LocalDate.parse(dueStr);
                    }
                    Category collaboratorCategory = null;
                    if (!collaboratorName.isBlank()) {

                        if (categoryStr.isBlank()) {
                            throw new IllegalArgumentException("- Collaborator category missing for: " + collaboratorName);
                        }

                        collaboratorCategory = Category.fromString(categoryStr);
                    }

                    // Subtask
                    SubTask subTask = null;
                    if (!subTaskStr.isBlank()) {
                        subTask = new SubTask(subTaskStr, Status.OPEN);
                    }

                    // Project
                    Project project = null;
                    if (!projectName.isBlank()) {
                        for (Project p : projects) {
                            if (p.getName().equalsIgnoreCase(projectName)) {
                                project = p;
                                break;
                            }
                        }

                        if (project == null) {
                            project = new Project(projectName, projectDescription);
                            projects.add(project);
                        }
                    }

                    // Collaborator
                    Collaborator collaborator = null;
                    if (!collaboratorName.isBlank()) {

                        for (Collaborator c : collaborators) {
                            if (c.getName().equalsIgnoreCase(collaboratorName)) {
                                collaborator = c;
                                break;
                            }
                        }

                        if (collaborator == null) {
                            collaborator = new Collaborator(collaboratorName, collaboratorCategory);
                            collaborators.add(collaborator);
                        }
                    }

                    // Create task
                    Task task = new Task(taskName, description, priorityLevel, status, dueDate);

                    // Link project
                    if (project != null) {
                        project.addTask(task);
                        task.setProject(project);
                    }

                    // Handle collaborator + subtasks
                    if (collaborator != null) {

                        SubTask collabSubTask;

                        if (subTask != null) {
                            collabSubTask = subTask;
                        } else {
                            collabSubTask = new SubTask("Added to task: " + taskName, Status.OPEN);
                        }

                        task.addSubTask(collabSubTask);
                        collaborator.addSubTask(collabSubTask);

                    } else if (subTask != null) {
                        task.addSubTask(subTask);
                    }

                    tasks.add(task);

                } catch (Exception e) {
                    System.out.println(
                        "----------------------------------------\n" +
                        "- Skipping row: " + currentRow + "\n" +
                        e.getMessage() + "\n" +
                        "----------------------------------------\n"
                    );
                    skippedRows++;
                }

                currentRow++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(skippedRows + (skippedRows == 1 ? " task was " : " tasks were ") + " not imported.");
        System.out.println(tasks.size() + " tasks imported into DB.");

        return tasks;
    }
}