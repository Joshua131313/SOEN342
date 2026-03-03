package ca.soen342.taskmanager.enums;

public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayOfWeek stringToDayOfWeek(String dayOfWeek) throws IllegalArgumentException {
        if(dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        if(dayOfWeek.toLowerCase().equals("monday")) {
            return MONDAY;
        }
        else if(dayOfWeek.toLowerCase().equals("tuesday")) {
            return TUESDAY;
        }
        else if(dayOfWeek.toLowerCase().equals("wednesday")) {
            return WEDNESDAY;
        }
        else if(dayOfWeek.toLowerCase().equals("thursday")) {
            return THURSDAY;
        }
        else if(dayOfWeek.toLowerCase().equals("friday")) {
            return FRIDAY;
        }
        else if(dayOfWeek.toLowerCase().equals("saturday")) {
            return SATURDAY;
        }
        else if(dayOfWeek.toLowerCase().equals("sunday")) {
            return SUNDAY;
        }
        else {
            throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }
}
