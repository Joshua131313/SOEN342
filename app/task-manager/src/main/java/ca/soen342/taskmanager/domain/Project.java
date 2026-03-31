package ca.soen342.taskmanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private List<Task> tasks;
    private List<Collaborator> collaborators;
    private int id;

    public Project(String name, String description, int id) {
        this(name, description);
        this.id = id;
    }
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
        this.tasks = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addCollaborator(Collaborator collaborator) {
        collaborators.add(collaborator);
    }
}
