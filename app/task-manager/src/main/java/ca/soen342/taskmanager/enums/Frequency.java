package ca.soen342.taskmanager.enums;

public enum Frequency {
    DAILY, 
    WEEKLY, 
    MONTHLY;

    public static Frequency fromString(String frequency) throws IllegalArgumentException {
        if (frequency == null) {
            throw new IllegalArgumentException("Frequency cannot be null");
        }

        try {
            return Frequency.valueOf(frequency.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }
}