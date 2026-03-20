package ca.soen342.taskmanager.enums;

public enum Category {
    JUNIOR,
    INTERMEDIATE,
    SENIOR;
    
    @Override 
    public String toString() {
        return name().toLowerCase();
    }

    public static Category fromString(String category) throws IllegalArgumentException {
        if (category == null) {
            throw new IllegalArgumentException("- Category cannot be null");
        }

        try {
            return Category.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "- Invalid category: " + category + "\n" +
                "- Valid categories: Junior, Intermediate, Senior"
            );
        }
    }
}
