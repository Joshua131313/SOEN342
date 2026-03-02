package ca.soen342.taskmanager.service;

import java.time.LocalDate;
import java.util.List;

import ca.soen342.taskmanager.domain.RecurrencePattern;
import ca.soen342.taskmanager.domain.Tag;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.domain.TaskOccurrence;
import ca.soen342.taskmanager.enums.DayOfWeek;
import ca.soen342.taskmanager.enums.Frequency;
import ca.soen342.taskmanager.enums.Status;

public class TaskService {
    private TagsService tagsService;
    public TaskService(TagsService tagsService) {
        this.tagsService = tagsService;
    }
    private void addTags(Task task, List<String> tags) {
        for(String tagName : tags) {
            Tag tag = tagsService.getOrCreateTag(tagName);
            task.addTag(tag);
        }
    }
    public Task createTask(
        String title, 
        String description, 
        int priorityLevel, 
        Status status, 
        LocalDate dueDate,
        List<String> tags
    ) {
        Task t = new Task(title, description, priorityLevel, status, dueDate);
        
        TaskOccurrence occurence = new TaskOccurrence(dueDate, Status.OPEN);
        addTags(t, tags);
        t.addOccurrence(occurence);
        
        return t;
    }
    public Task createRecurringTask(
        String title, 
        String description, 
        int priorityLevel, 
        Status status, 
        LocalDate dueDate,
        List<String> tags,
        Frequency frequency, 
        int interval, 
        LocalDate start, 
        LocalDate end,
        List<DayOfWeek> weekDays,
        Integer dayOfMonth
    ) {
        Task t = new Task(title, description, priorityLevel, status, dueDate);
        RecurrencePattern pattern = new RecurrencePattern(
            frequency, 
            interval, 
            start, 
            end,
            weekDays,
            dayOfMonth
        );
        
        t.setRecurrencePattern(pattern);
        TaskOccurrence occurrence = new TaskOccurrence(dueDate, Status.OPEN);
        t.addOccurrence(occurrence);

        return t;
    }
}
