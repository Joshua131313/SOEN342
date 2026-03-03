package ca.soen342.taskmanager.enums;

public enum Category {
    JUNIOR,
    INTERMEDIATE,
    SENIOR;

    public static Category stringToCategory(String category) throws IllegalArgumentException {
        if(category == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if(category.toLowerCase().equals("junior")) {
            return JUNIOR;
        }
        else if(category.toLowerCase().equals("intermediate")) {
            return INTERMEDIATE;
        }
        else if(category.toLowerCase().equals("senior")) {
            return SENIOR;
        }
        else {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
}
