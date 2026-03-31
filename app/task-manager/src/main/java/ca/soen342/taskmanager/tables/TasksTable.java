package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TasksTable {

    public void addTask(Task task, Integer projectId) {

        String sql = """
            INSERT INTO tasks (title, description, creation_date, priority_level, status, due_date, project_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.connect()) {

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, task.getTitle());
                stmt.setString(2, task.getDescription());
                stmt.setString(3, task.getCreationDate().toString());
                stmt.setInt(4, task.getPriorityLevel());
                stmt.setString(5, task.getStatus().name());

                if (task.getDueDate() != null) {
                    stmt.setString(6, task.getDueDate().toString());
                } else {
                    stmt.setNull(6, Types.VARCHAR);
                }

                if (projectId != null) {
                    stmt.setInt(7, projectId);
                } else {
                    stmt.setNull(7, Types.INTEGER);
                }

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Failed to get generated task id");
                }
            }

            SubTasksTable subTasksTable = new SubTasksTable();

            if (task.getSubTasks() != null) {
                for (var subTask : task.getSubTasks()) {
                    subTasksTable.addSubTask(conn, subTask, task.getId());
                }
            }

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {

        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Task task = new Task(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("priority_level"),
                        Status.valueOf(rs.getString("status")),
                        rs.getString("due_date") != null
                                ? LocalDate.parse(rs.getString("due_date"))
                                : null,
                        rs.getInt("id") 
                );

                task.setCreationDate(LocalDate.parse(rs.getString("creation_date")));

                tasks.add(task);
            }

            SubTasksTable subTasksTable = new SubTasksTable();

            for (Task task : tasks) {
                task.setSubTasks(subTasksTable.getSubTasksByTaskId(task.getId()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }
}