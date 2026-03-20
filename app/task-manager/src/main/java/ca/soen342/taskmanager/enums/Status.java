package ca.soen342.taskmanager.enums;

public enum Status {
    OPEN,
    COMPLETED,
    CANCELLED;

    public static Status fromString(String status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("- Status cannot be null");
        }

        try {
            return Status.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "- Invalid status: " + status + "\n" +
                "- Valid Statuses: Open, Completed, Cancelled"
            );
        }
    }
}
