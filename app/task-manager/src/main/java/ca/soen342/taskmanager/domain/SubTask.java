package ca.soen342.taskmanager.domain;

import ca.soen342.taskmanager.enums.Status;

public class SubTask {
    private String title;
    private Status status;

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
    
}
