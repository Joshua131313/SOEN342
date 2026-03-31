package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.ActivityEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityEntriesTable {

    public void addActivityEntry(ActivityEntry entry, int taskId) {
        String sql = """
            INSERT INTO activity_entries (timestamp, description, task_id)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entry.getTimestamp().toString());
            stmt.setString(2, entry.getDescription());
            stmt.setInt(3, taskId);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ActivityEntry> getByTaskId(int taskId) {
        List<ActivityEntry> entries = new ArrayList<>();

        String sql = "SELECT * FROM activity_entries WHERE task_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ActivityEntry entry = new ActivityEntry(
                        LocalDate.parse(rs.getString("timestamp")),
                        rs.getString("description")
                );
                entries.add(entry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entries;
    }
}