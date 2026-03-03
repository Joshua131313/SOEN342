package ca.soen342.taskmanager.domain;

import java.util.ArrayList;
import java.util.List;

import ca.soen342.taskmanager.enums.Category;

public class Collaborator {
    private String name;
    private int limit;
    private Category category;
    private List<SubTask> subTasks;
    

    public Collaborator(String name, int limit, Category category) {
        this.name = name;
        this.category = category;
        this.subTasks = new ArrayList<>();
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
        subTasks.add(subTask);
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
