package ca.soen342.taskmanager.domain;

import java.util.ArrayList;
import java.util.List;

import ca.soen342.taskmanager.enums.Category;
import ca.soen342.taskmanager.enums.Status;

public class Collaborator {
    private String name;
    private int limit;
    private int assigned;
    private Category category;
    private List<SubTask> subTasks;
    private int id;
    
       
    public Collaborator(String name, Category category, int id) {
        this(name, category);
        this.id = id;
    }
    public Collaborator(String name, Category category) {
        this.name = name;
        this.category = category;
        this.subTasks = new ArrayList<>();
        this.assigned = 0;
        if(category == Category.SENIOR) {
            this.limit = 2;
        }
        else if(category == Category.INTERMEDIATE) {
            this.limit = 5;
        }
        else {
            // junior
            this.limit = 10;
        }
    }
    public void addSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("SubTask cannot be null");
        }

        if (subTask.getStatus() == Status.OPEN && assigned >= limit) {
            throw new IllegalArgumentException(
                "Cannot assign subtask: " + subTask.getTitle() +
                ". Collaborator: " + name +
                " (" + category + ") already has maximum " + limit + " open subtasks."
            );
        }

        subTasks.add(subTask);

        if (subTask.getStatus() == Status.OPEN) {
            assigned++;
        }
    }

    public boolean canTakeMoreTasks() {
        int openTasks = 0;

        for (SubTask s : subTasks) {
            if (s.getStatus() != Status.COMPLETED) {
                openTasks++;
            }
        }

        switch (category) {
            case JUNIOR:
                return openTasks < 10;
            case INTERMEDIATE:
                return openTasks < 5;
            case SENIOR:
                return openTasks < 2;
            default:
                return true;
        }
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void assignSubtask(SubTask subtask) {
        subTasks.add(subtask);
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getLimit() {
        return limit;
    }


    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public List<SubTask> getSubTasks() {
        return subTasks;
    }


    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    };
}
