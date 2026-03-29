package ca.soen342.taskmanager.interfaces;

import java.util.List;

import ca.soen342.taskmanager.domain.Task;

public interface CalenderExportGateway {
    String exportTasks(List<Task> tasks);
}
