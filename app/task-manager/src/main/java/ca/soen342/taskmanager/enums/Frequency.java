package ca.soen342.taskmanager.enums;

public enum Frequency {
    DAILY, 
    WEEKLY, 
    MONTHLY;
    public static Frequency stringToFreequency(String frequency) throws IllegalArgumentException {
        if(frequency == null) {
            throw new IllegalArgumentException("Frequency cannot be null");
        }
        if(frequency.toLowerCase().equals("daily")) {
            return DAILY;
        }
        else if(frequency.toLowerCase().equals("weekly")) {
            return WEEKLY;
        }
        else if(frequency.toLowerCase().equals("monthly")) {
            return MONTHLY;
        }
        else {
            throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }
}