package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.RecurrencePattern;
import ca.soen342.taskmanager.enums.DayOfWeek;
import ca.soen342.taskmanager.enums.Frequency;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecurrenceTable {

    public void addRecurrencePattern(RecurrencePattern recurrencePattern, int taskId) {
        String sql = """
            INSERT INTO recurrence_patterns (task_id, frequency, interval, start_date, end_date, day_of_month)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, taskId);
            stmt.setString(2, recurrencePattern.getFrequency().name());
            stmt.setInt(3, recurrencePattern.getInterval());
            stmt.setString(4, recurrencePattern.getStart().toString());

            if (recurrencePattern.getEnd() != null) {
                stmt.setString(5, recurrencePattern.getEnd().toString());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            if (recurrencePattern.getDayOfMonth() != null) {
                stmt.setInt(6, recurrencePattern.getDayOfMonth());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int recurrenceId = rs.getInt(1);
                addWeekDays(recurrenceId, recurrencePattern.getWeekDays());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWeekDays(int recurrenceId, List<DayOfWeek> weekDays) {
        if (weekDays == null || weekDays.isEmpty()) {
            return;
        }

        String sql = """
            INSERT INTO recurrence_weekdays (recurrence_id, day)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (DayOfWeek day : weekDays) {
                stmt.setInt(1, recurrenceId);
                stmt.setString(2, day.name());
                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecurrencePattern getByTaskId(int taskId) {
        String sql = "SELECT * FROM recurrence_patterns WHERE task_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int recurrenceId = rs.getInt("id");

                return new RecurrencePattern(
                        Frequency.valueOf(rs.getString("frequency")),
                        rs.getInt("interval"),
                        LocalDate.parse(rs.getString("start_date")),
                        rs.getString("end_date") != null
                                ? LocalDate.parse(rs.getString("end_date"))
                                : null,
                        getWeekDays(recurrenceId),
                        rs.getObject("day_of_month") != null
                                ? rs.getInt("day_of_month")
                                : null
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<DayOfWeek> getWeekDays(int recurrenceId) {
        List<DayOfWeek> weekDays = new ArrayList<>();

        String sql = "SELECT day FROM recurrence_weekdays WHERE recurrence_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recurrenceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                weekDays.add(DayOfWeek.valueOf(rs.getString("day")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return weekDays;
    }
}