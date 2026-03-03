package ca.soen342.taskmanager;

import java.util.ArrayList;
import java.util.List;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.service.CSVTaskService;
import ca.soen342.taskmanager.ui.TaskUI;

public class Main {
    public static void main(String[] args) {
        
        // TaskUI taskUI = new TaskUI();
        // taskUI.createTask();
        List<Project> projects = new ArrayList<>();
        List<Collaborator> collaborators = new ArrayList<>();
        List<Task> tasks = CSVTaskService.importFromCSV(projects, collaborators);
        for(Task task : tasks) {
            System.out.println(task);
        }
    }
}