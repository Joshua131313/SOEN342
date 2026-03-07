package ca.soen342.taskmanager.enums;

public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayOfWeek fromString(String dayOfWeek) throws IllegalArgumentException {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }

        try {
            return DayOfWeek.valueOf(dayOfWeek.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }
}
