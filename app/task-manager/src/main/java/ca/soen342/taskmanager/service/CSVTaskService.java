package ca.soen342.taskmanager.service;

import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.SubTask;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Category;
import ca.soen342.taskmanager.enums.Status;

public class CSVTaskService {
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
    private static String extractColumn(List<String> row, Column column) throws IllegalArgumentException {
        String data = row.get(column.getOrder());
        if(data.isBlank() && !column.isOptional()) {
            throw new IllegalArgumentException("Missing required column: " + column.getName());
        }
        return data;
    }
    public void exportToCSV() {
    
    }
    public static List<Task> importFromCSV(List<Project> projects, List<Collaborator> collaborators) {
        List<Task> tasks = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("C:\\Users\\josht\\Documents\\SOEN342\\import-tasks.txt"))) {
            while(scanner.hasNextLine()) {
                try {
                    String line = scanner.nextLine();
                    // ensure that if a field has , inside "" it doesnt split by that , for example descriptioon could be "hello, my name is" -> dont split this comma
                    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    List<String> row = Arrays.asList(tokens);

                    String taskName = extractColumn(row, Column.TASK_NAME);
                    String description = extractColumn(row, Column.DESCRIPTION);
                    String subTaskStr = extractColumn(row, Column.SUBTASK);
                    Status status = Status.stringToStatus(extractColumn(row, Column.STATUS));
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
                            throw new IllegalArgumentException(
                                "Collaborator category missing for: " + collaboratorName
                            );
                        }

                        collaboratorCategory = Category.stringToCategory(categoryStr);
                    } else {
                        if (!categoryStr.isBlank()) {
                            throw new IllegalArgumentException(
                                "Category provided without collaborator"
                            );
                        }
                    }// check if subTask is in the row
                    SubTask subTask = null;
                    if(!subTaskStr.isBlank()) {
                        subTask = new SubTask(subTaskStr, Status.OPEN);
                    }
                    
                    //  check if project exists, otherwise create it
                    Project project = null;
                    if(!projectName.isBlank()) {
                        for(Project p : projects) {
                            if(p.getName().equals(projectName)) {
                                project = p;
                            }
                        }
                        if(project == null) {
                            project = new Project(projectName, projectDescription);
                            projects.add(project);
                        }
                    }
                    Collaborator collaborator = null;
                    if(!collaboratorName.isBlank()) {
                        for(Collaborator c : collaborators) {
                            if(c.getName().equals((collaboratorName))) {
                                collaborator = c;
                            }
                        }
                        if(collaborator == null) {
                            collaborator = new Collaborator(collaboratorName, collaboratorCategory);
                            collaborators.add(collaborator);
                        }
                    }

                    Task task = new Task(taskName, description, priorityLevel, status, dueDate);
                    if(project != null) {
                        project.addCollaborator(collaborator);
                        project.addTask(task);
                    }
                    if(collaborator != null) {
                        // generic subtask created for collaborator
                        SubTask collabSubTask = new SubTask("Added to task: " + taskName, Status.OPEN);
                        collaborator.addSubTask(collabSubTask);
                        task.addSubTask(collabSubTask);
                    }
                    if(subTask != null) {
                        task.addSubTask(subTask);
                    }
                    tasks.add(task);
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Skipping row: " + e.getMessage());
                    continue; // skip this row
                }
                catch (DateTimeException e) {
                    // ignore this since date is optional
                }
            }
        }
        catch (Exception e) {

        }
        return tasks;
    }
}
