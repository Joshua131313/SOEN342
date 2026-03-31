package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.Task;
import ca.soen342.taskmanager.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ProjectsTable {

    public void addProject(Project project) {
        String sql = """
                    INSERT INTO projects (name, description)
                    VALUES (?, ?)
                """;

        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1)); 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM projects";

        try (Connection conn = DBConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Project project = new Project(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("id"));
                project.setTasks(getTasksForProject(rs.getInt("id"), project));

                projects.add(project);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return projects;
    }

    private List<Task> getTasksForProject(int projectId, Project project) {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE project_id = ?";

        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Task task = new Task(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("priority_level"),
                        Status.valueOf(rs.getString("status")),
                        rs.getString("due_date") != null
                                ? LocalDate.parse(rs.getString("due_date"))
                                : null,
                        rs.getInt("id"));

                tasks.add(task);
                task.setProject(project);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }
}