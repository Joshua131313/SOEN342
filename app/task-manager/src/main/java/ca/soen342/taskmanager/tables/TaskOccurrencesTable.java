package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.TaskOccurrence;
import ca.soen342.taskmanager.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskOccurrencesTable {

    public void addTaskOccurrence(TaskOccurrence occurrence, int taskId) {
        String sql = """
            INSERT INTO task_occurrences (task_id, due_date, status, completed_at)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            stmt.setString(2, occurrence.getDueDate().toString());
            stmt.setString(3, occurrence.getStatus().name());

            if (occurrence.getCompletedAt() != null) {
                stmt.setString(4, occurrence.getCompletedAt().toString());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TaskOccurrence> getByTaskId(int taskId) {
        List<TaskOccurrence> occurrences = new ArrayList<>();

        String sql = "SELECT * FROM task_occurrences WHERE task_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TaskOccurrence occurrence = new TaskOccurrence(
                        LocalDate.parse(rs.getString("due_date")),
                        Status.valueOf(rs.getString("status"))
                );

                if (rs.getString("completed_at") != null &&
                    occurrence.getStatus() == Status.COMPLETED) {
                }

                occurrences.add(occurrence);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return occurrences;
    }
}