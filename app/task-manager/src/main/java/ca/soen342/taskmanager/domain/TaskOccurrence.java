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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
        this.completedAt = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Occurrence\n" +
                "  dueDate: " + dueDate + ",\n" +
                "  status: " + status + ",\n" +
                "  completedAt: " + completedAt + "\n";
    }
}