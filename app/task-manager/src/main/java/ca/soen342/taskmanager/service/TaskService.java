package ca.soen342.taskmanager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.RecurrencePattern;
import ca.soen342.taskmanager.domain.SubTask;
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
        for (String tagName : tags) {
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
            List<String> tags) {
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
            Integer dayOfMonth) {
        Task t = new Task(title, description, priorityLevel, status, dueDate);
        RecurrencePattern pattern = new RecurrencePattern(
                frequency,
                interval,
                start,
                end,
                weekDays,
                dayOfMonth);

        t.setRecurrencePattern(pattern);
        addTags(t, tags);

        List<LocalDate> occurrenceDates = generateOccurrenceDates(
                frequency,
                interval,
                start,
                end,
                weekDays,
                dayOfMonth);

        for (LocalDate occurrenceDate : occurrenceDates) {
            TaskOccurrence occurrence = new TaskOccurrence(occurrenceDate, Status.OPEN);
            t.addOccurrence(occurrence);
        }

        return t;
    }
    public static List<Task> searchTasks(
        List<Task> tasks,
        String titleKeyword,
        Status status,
        LocalDate dueDate,
        Integer priorityLevel
) {
    if (titleKeyword == null && status == null && dueDate == null && priorityLevel == null) {
    return tasks.stream()
        .filter(t -> t.getStatus() == Status.OPEN)
        .sorted((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
        .collect(java.util.stream.Collectors.toList());
}
    List<Task> results = new ArrayList<>();

    for (Task task : tasks) {

        boolean match = true;

        if (titleKeyword != null && !task.getTitle().toLowerCase().contains(titleKeyword.toLowerCase())) {
            match = false;
        }

        if (status != null && task.getStatus() != status) {
            match = false;
        }

        if (dueDate != null && !task.getDueDate().equals(dueDate)) {
            match = false;
        }

        if (priorityLevel != null && task.getPriorityLevel() != priorityLevel) {
            match = false;
        }

        if (match) {
            results.add(task);
        }
    }

    return results;
}
    public static List<Collaborator> getOverloadedCollaborators(List<Collaborator> collaborators) {
    List<Collaborator> overloaded = new ArrayList<>();
    for (Collaborator c : collaborators) {
        if (!c.canTakeMoreTasks()) {
            overloaded.add(c);
        }
    }
    return overloaded;
}

    public void assignCollaborator(Task task, Collaborator collaborator) {

            // 1. Check constraint
            if (!collaborator.canTakeMoreTasks()) {
                throw new IllegalStateException(
                    "- Cannot assign task: " + collaborator.getName() + " reached limit."
                );
            }

            // 2. Create subtask
            SubTask subtask = new SubTask(
                "Subtask for " + collaborator.getName(),
                collaborator
            );

            // 3. Link both sides
            task.addSubtask(subtask);
            collaborator.assignSubtask(subtask);
    }

    private List<LocalDate> generateOccurrenceDates(
        Frequency frequency,
        int interval,
        LocalDate start,
        LocalDate end,
        List<DayOfWeek> weekDays,
        Integer dayOfMonth) {

    List<LocalDate> dates = new ArrayList<>();

    if (start == null || end == null || start.isAfter(end)) {
        return dates;
    }

    if (interval <= 0) {
        interval = 1;
    }

    if (frequency == Frequency.DAILY) {
        LocalDate currentDate = start;

        while (!currentDate.isAfter(end)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(interval);
        }
    } else if (frequency == Frequency.WEEKLY) {
        LocalDate currentDate = start;

        while (!currentDate.isAfter(end)) {
            DayOfWeek currentDay = DayOfWeek.fromString(currentDate.getDayOfWeek().name());

            if (weekDays != null && weekDays.contains(currentDay)) {
                long weeksBetween = java.time.temporal.ChronoUnit.WEEKS.between(start, currentDate);
                if (weeksBetween % interval == 0) {
                    dates.add(currentDate);
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    } else if (frequency == Frequency.MONTHLY) {
        LocalDate currentMonth = start.withDayOfMonth(1);

        while (!currentMonth.isAfter(end.withDayOfMonth(1))) {
            if (dayOfMonth != null && dayOfMonth <= currentMonth.lengthOfMonth()) {
                LocalDate occurrenceDate = currentMonth.withDayOfMonth(dayOfMonth);

                if (!occurrenceDate.isBefore(start) && !occurrenceDate.isAfter(end)) {
                    dates.add(occurrenceDate);
                }
            }

            currentMonth = currentMonth.plusMonths(interval);
        }
    }

    return dates;
    }



}
