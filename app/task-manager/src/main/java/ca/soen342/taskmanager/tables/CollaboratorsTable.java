package ca.soen342.taskmanager.tables;

import ca.soen342.taskmanager.db.DBConnection;
import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.enums.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollaboratorsTable {

    public void addCollaborator(Collaborator collaborator) {
        String sql = """
            INSERT INTO collaborators (name, category)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, collaborator.getName());
            stmt.setString(2, collaborator.getCategory().name());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                collaborator.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Collaborator> getAllCollaborators() {
        List<Collaborator> collaborators = new ArrayList<>();

        String sql = "SELECT * FROM collaborators";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Collaborator collaborator = new Collaborator(
                        rs.getString("name"),
                        Category.valueOf(rs.getString("category"))
                );
                collaborator.setId(rs.getInt("id"));
                collaborators.add(collaborator);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return collaborators;
    }

    public Collaborator getCollaboratorById(int collaboratorId) {
        String sql = "SELECT * FROM collaborators WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collaboratorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Collaborator collaborator = new Collaborator(
                        rs.getString("name"),
                        Category.valueOf(rs.getString("category"))
                );
                collaborator.setId(rs.getInt("id"));
                return collaborator;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}