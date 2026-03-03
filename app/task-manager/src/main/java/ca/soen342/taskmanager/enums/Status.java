package ca.soen342.taskmanager.enums;

public enum Status {
    OPEN,
    COMPLETED,
    CANCELLED;

    public static Status stringToStatus(String status) throws IllegalArgumentException {
        if(status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if(status.toLowerCase().equals("open")) {
            return OPEN;
        }
        else if(status.toLowerCase().equals("completed")) {
            return COMPLETED;
        }
        else if(status.toLowerCase().equals("cancelled")) {
            return CANCELLED;
        }
        else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
