package ca.soen342.taskmanager.db;

import java.sql.Connection;
import java.sql.Statement;

public class DBInit {

    public static void init() {
        try (
                Connection conn = DBConnection.connect();
                Statement statement = conn.createStatement()
        ) {

            statement.execute("PRAGMA foreign_keys = ON");
            // Projects table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS projects (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT
                )
            """);
            // Collaborators table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS collaborators (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    category TEXT NOT NULL
                )
            """);
            // Tasks table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    creation_date TEXT,
                    priority_level INTEGER,
                    status TEXT,
                    due_date TEXT,
                    project_id INTEGER,
                    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
                )
            """);
            // Subtasks table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS subtasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    status TEXT,
                    task_id INTEGER,
                    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
                )
            """);
            // Activity entries table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS activity_entries (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timestamp TEXT,
                    description TEXT,
                    task_id INTEGER,
                    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
                )
            """);
            // Tags table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS tags (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    keyword TEXT NOT NULL
                )
            """);
            // TaskTags table for the many to many relation
            statement.execute("""
                CREATE TABLE IF NOT EXISTS task_tags (
                    task_id INTEGER,
                    tag_id INTEGER,
                    PRIMARY KEY (task_id, tag_id),
                    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
                    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
                )
            """);
            // Task occurences table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS task_occurrences (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    task_id INTEGER,
                    due_date TEXT,
                    status TEXT,
                    completed_at TEXT,
                    UNIQUE(task_id, due_date),
                    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
                )
            """);
            // Recurrence patterns table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS recurrence_patterns (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    task_id INTEGER UNIQUE,
                    frequency TEXT,
                    interval INTEGER,
                    start_date TEXT,
                    end_date TEXT,
                    day_of_month INTEGER,
                    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
                )
            """);
            // Recurrence weekdays table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS recurrence_weekdays (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    recurrence_id INTEGER,
                    day TEXT,
                    FOREIGN KEY (recurrence_id) REFERENCES recurrence_patterns(id) ON DELETE CASCADE
                )
            """);

            System.out.println("Database initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}