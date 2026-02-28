package ca.soen342.taskmanager.domain;

import java.time.LocalDate;

import ca.soen342.taskmanager.enums.Status;

public class TaskOccurrence {
    private LocalDate dueDate;
    private Status status;
    private LocalDate completedAt;
    public TaskOccurrence(LocalDate dueDate, Status status) {
        this.dueDate = dueDate;
        this.status = status;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
}
