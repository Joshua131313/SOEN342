package ca.soen342.taskmanager.service;

import java.util.List;

import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.interfaces.CalenderExportGateway;

public class CalendarExportService {
    private CalenderExportGateway gateway;

    public CalendarExportService(CalenderExportGateway gateway) {
        this.gateway = gateway;
    }
    public String export(List<Task> tasks) {
        return gateway.exportTasks(tasks);
    }
}
