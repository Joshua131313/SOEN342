package ca.soen342.taskmanager.gateway;

import java.io.FileOutputStream;
import java.util.List;

import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.SubTask;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.interfaces.CalenderExportGateway;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

public class ICSExportGateway implements CalenderExportGateway {
    @Override
    public String exportTasks(List<Task> tasks) {
        try {
            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//TaskManager//iCal4j//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);

            for (Task task : tasks) {
                if (task.getDueDate() == null)
                    continue; // skip if the task does not have due date.
                DateTime start = new DateTime(
                        java.util.Date.from(
                                task.getDueDate()
                                        .atStartOfDay(java.time.ZoneId.systemDefault())
                                        .toInstant()));
                VEvent event = new VEvent(start, task.getTitle());
                List<SubTask> subTasks = task.getSubTasks();
                String description = task.getDescription() != null ? task.getDescription() + "\n" : "";
                // find project in which task belongs to
                Project project = task.getProject();
                // add project name here
                if (project != null) {
                    description += "Project: " + project.getName() + "\n";
                }

                if (subTasks == null || subTasks.isEmpty()) {
                    description += "No subtasks\n";
                } else {
                    for (SubTask subTask : subTasks) {
                        description += "- Subtask:\n"
                                + "\tTitle: " + subTask.getTitle() + "\n"
                                + "\tStatus: " + subTask.getStatus() + "\n"
                                + "\tCollaborator: "
                                + (subTask.getCollaborator() != null
                                        ? subTask.getCollaborator().getName()
                                        : "None")
                                + "\n";
                    }
                }
                event.getProperties().add(new Description(description));
                event.getProperties().add(new Uid(String.valueOf(task.getId())));
                ;
                event.getProperties().add(new Status(mapStatus(task.getStatus().toString())));
                event.getProperties().add(new Priority(task.getPriorityLevel()));
                calendar.getComponents().add(event);
            }
            String fileName = "tasks.ics";
            FileOutputStream fos = new FileOutputStream(fileName);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fos);

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to export tasks to ICS", e);
        }
    }

    private String mapStatus(String status) {
        if (status == null)
            return "NEEDS-ACTION";

        switch (status.toLowerCase()) {
            case "open":
                return "IN-PROCESS";
            case "completed":
                return "COMPLETED";
            case "cancelled":
                return "CANCELLED";
            default:
                return "NEEDS-ACTION";
        }

    }
}
