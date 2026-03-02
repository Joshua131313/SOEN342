package ca.soen342.taskmanager;

import ca.soen342.taskmanager.ui.TaskUI;

public class Main {
    public static void main(String[] args) {
        TaskUI taskUI = new TaskUI();
        taskUI.createTask();
    }
}