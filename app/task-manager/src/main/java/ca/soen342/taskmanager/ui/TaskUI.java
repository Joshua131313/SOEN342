package ca.soen342.taskmanager.ui;

import java.time.LocalDate;
import java.util.List;

import ca.soen342.taskmanager.enums.DayOfWeek;
import ca.soen342.taskmanager.enums.Frequency;
import ca.soen342.taskmanager.enums.Status;
import ca.soen342.taskmanager.service.TagsService;
import ca.soen342.taskmanager.service.TaskService;

public class TaskUI {
    private TagsService tagsService;
    private TaskService taskService = new TaskService(tagsService);
    private InputHelper input;

    public TaskUI() {
        tagsService = new TagsService();
        taskService = new TaskService(tagsService);
        input  = new InputHelper();
    }

    public void createTask() {
        String title = input.askString("Enter task title: ");
        String description = input.askString("Enter a description: ");
        int priorityLevel = input.askInt("Enter priority level: ");
        Status status = input.askOption("Enter status: ", Status.class);
        LocalDate dueDate = input.askDate("Enter due date");
        List<String> tags = input.askTags("Enter tags (separate by commas): ");
        boolean recurring = input.askBoolean("Is this a recurring task?");
        
        if(recurring) {
            Frequency frequency = input.askOption("Enter frequency: ", Frequency.class);
            int interval = input.askInt("Enter interval: ");
            LocalDate start = input.askDate("Enter start date: ");
            LocalDate end = input.askDate("Enter end date: ");
            List<DayOfWeek> weekDays = null;
            Integer dayOfMonth = null;

            if(frequency == Frequency.WEEKLY) {
                weekDays = input.askWeekDays("Select weekdays: ");
            }
            if(frequency == Frequency.MONTHLY) {
                dayOfMonth = input.askDayOfMonth("Enter day of month (1-31): ");
            }

            taskService.createRecurringTask(
                title, 
                description, 
                priorityLevel, 
                status, 
                dueDate, 
                tags,
                frequency, 
                interval,
                start, 
                end, 
                weekDays, 
                dayOfMonth
            );
        }
        else {
            taskService.createTask(title, description, priorityLevel, status, dueDate, tags);
        }
    }
}
