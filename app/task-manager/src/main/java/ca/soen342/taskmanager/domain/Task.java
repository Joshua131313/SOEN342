package ca.soen342.taskmanager.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.soen342.taskmanager.enums.Frequency;
import ca.soen342.taskmanager.enums.Status;


public class Task {

    private String title;
    private String description;
    private LocalDate creationDate;
    private int priorityLevel;
    private Status status;
    private LocalDate dueDate;
    private UUID id;

    private List<ActivityEntry> activityEntry;
    private List<Tag> tags;
    private List<SubTask> subTasks;
    // recurring task only
    private List<TaskOccurrence> taskOccurences;
    private RecurrencePattern recurrencePattern;

    public Task(String title, String description, int priorityLevel, Status status, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.creationDate = LocalDate.now();
        this.priorityLevel = priorityLevel;
        this.status = status;
        this.dueDate = dueDate;
        this.id = UUID.randomUUID();
        this.tags = new ArrayList<Tag>();
        this.taskOccurences = new ArrayList<>();
        this.activityEntry = new ArrayList<>();
        this.subTasks = new ArrayList<>();
    }
    public void setRecurrencePattern(RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
    public void addOccurrence(TaskOccurrence occurrence) {
        taskOccurences.add(occurrence);
    }
    public void addTag(Tag tag) {
        tags.add(tag);
    }
    @Override 
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id != null && id.equals(task.id);
    }
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
