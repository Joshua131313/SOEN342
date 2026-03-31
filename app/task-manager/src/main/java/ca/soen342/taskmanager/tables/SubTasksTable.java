package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.SubTask;
import ca.soen342.taskmanager.enums.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubTasksTable {

    public void addSubTask(Connection conn, SubTask subTask, int taskId) {

        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid taskId: " + taskId);
        }

        String sql = """
            INSERT INTO subtasks (title, status, task_id)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subTask.getTitle());
            stmt.setString(2, subTask.getStatus().name());
            stmt.setInt(3, taskId);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SubTask> getSubTasksByTaskId(int taskId) {

        List<SubTask> subTasks = new ArrayList<>();

        String sql = "SELECT * FROM subtasks WHERE task_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subTasks.add(new SubTask(
                        rs.getString("title"),
                        Status.valueOf(rs.getString("status"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return subTasks;
    }
}