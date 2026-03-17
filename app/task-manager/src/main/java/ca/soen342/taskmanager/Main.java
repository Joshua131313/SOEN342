package ca.soen342.taskmanager;

import java.util.ArrayList;
import java.util.List;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Status;
import ca.soen342.taskmanager.service.ExportToCSV;
import ca.soen342.taskmanager.service.ImportToCSV;
import ca.soen342.taskmanager.service.TaskService;

public class Main {

    public static void main(String[] args) {

        List<Project> projects = new ArrayList<>();
        List<Collaborator> collaborators = new ArrayList<>();

        // Import tasks
        List<Task> tasks = ImportToCSV.importTasks(projects, collaborators);

        System.out.println("----- Imported Tasks -----");
        for (Task task : tasks) {
            System.out.println(task);
        }

        // Export tasks
        ExportToCSV.exportTasks(tasks, projects, collaborators);

        System.out.println("\n----- Export completed -----");
        System.out.println("Check export.csv and compare it with the original import file.");

        // Search by name
        System.out.println("\n--- Search All Tasks with name \"Task L\" ---");

        List<Task> resultsByName = TaskService.searchTasks(tasks, "Task L", null, null, null);

        for (Task task : resultsByName) {
            System.out.println(task);
        }

        // Search by status
        System.out.println("\n--- Search All Tasks with Status OPEN ---");

        List<Task> resultsByStatus = TaskService.searchTasks(tasks,null, Status.OPEN, null, null);

        for (Task task : resultsByStatus) {
            System.out.println(task);
        }
    }
}