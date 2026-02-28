package ca.soen342.taskmanager.domain;

import java.time.LocalDate;
import java.util.List;

import ca.soen342.taskmanager.enums.DayOfWeek;
import ca.soen342.taskmanager.enums.Frequency;

public class RecurrencePattern {
    private Frequency frequency;
    private int interval;
    private LocalDate start;
    private LocalDate end;
    private List<DayOfWeek> weekDays;
    private Integer dayOfMonth;

    public RecurrencePattern(
            Frequency frequency, 
            int interval, 
            LocalDate start, 
            LocalDate end,
            List<DayOfWeek> weekDays,
            Integer dayOfMonth
    ) {
        this.frequency = frequency;
        this.interval = interval;
        this.start = start;
        this.end = end;
        this.weekDays = weekDays;
        this.dayOfMonth = dayOfMonth;
    }

    public Frequency getFrequency() {
        return frequency;
    }
    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }
    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public LocalDate getStart() {
        return start;
    }
    public void setStart(LocalDate start) {
        this.start = start;
    }
    public LocalDate getEnd() {
        return end;
    }
    public void setEnd(LocalDate end) {
        this.end = end;
    }
    public List<DayOfWeek> getWeekDays() {
        return weekDays;
    }
    public void setWeekDays(List<DayOfWeek> weekDays) {
        this.weekDays = weekDays;
    }
    public Integer getDayOfMonth() {
        return dayOfMonth;
    }
    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
