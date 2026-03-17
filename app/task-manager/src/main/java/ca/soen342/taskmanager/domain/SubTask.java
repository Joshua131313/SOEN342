package ca.soen342.taskmanager.domain;

import ca.soen342.taskmanager.enums.Status;

public class SubTask {
    private String title;
    private Status status;
    private Collaborator collaborator;

    public SubTask(String title, Collaborator collaborator) {
        this.title = title;
        this.status = Status.OPEN;
        this.collaborator = collaborator;
    }

      public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public SubTask(String title, Status status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void complete() {
        this.status = Status.COMPLETED;
    }
    
}
