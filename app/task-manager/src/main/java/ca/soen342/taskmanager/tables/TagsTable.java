package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagsTable {

    public void addTag(Tag tag) {
        String sql = """
            INSERT INTO tags (keyword)
            VALUES (?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tag.getKeyword());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                tag.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTagToTask(int taskId, int tagId) {
        String sql = """
            INSERT OR IGNORE INTO task_tags (task_id, tag_id)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            stmt.setInt(2, tagId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTagsToTask(int taskId, List<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getId() == 0) {
                addTag(tag);
            }
            addTagToTask(taskId, tag.getId());
        }
    }

    public List<Tag> getTagsByTaskId(int taskId) {
        List<Tag> tags = new ArrayList<>();

        String sql = """
            SELECT t.id, t.keyword
            FROM tags t
            INNER JOIN task_tags tt ON t.id = tt.tag_id
            WHERE tt.task_id = ?
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tag tag = new Tag(rs.getString("keyword"));
                tag.setId(rs.getInt("id"));
                tags.add(tag);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }
}