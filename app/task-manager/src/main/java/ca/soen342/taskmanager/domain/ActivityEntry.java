package ca.soen342.taskmanager.domain;

import java.time.LocalDate;

public class ActivityEntry {
    private LocalDate timestamp;
    private String description;
    
    public ActivityEntry(LocalDate timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
