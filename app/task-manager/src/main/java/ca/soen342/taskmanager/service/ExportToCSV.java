package ca.soen342.taskmanager.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ca.soen342.taskmanager.domain.Collaborator;
import ca.soen342.taskmanager.domain.Project;
import ca.soen342.taskmanager.domain.SubTask;
import ca.soen342.taskmanager.domain.Task;

public class ExportToCSV {

    private static Collaborator getCollaboratorOfSubTask(SubTask subTask, List<Collaborator> collaborators) {
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getSubTasks().contains(subTask)) {
                return collaborator;
            }
        }
        return null;
    }
    // overload writerow to avoid duplications
    private static String writeRow(Task task, Project project, SubTask subTask, Collaborator collaborator) {

        String projectName = "";
        String projectDescription = "";

        if (project != null) {
            projectName = project.getName();
            projectDescription = project.getDescription();
        }

        String subTaskTitle = "";
        if (subTask != null) {
            subTaskTitle = subTask.getTitle();
        }

        String collaboratorName = "";
        String collaboratorCategory = "";

        if (collaborator != null) {
            collaboratorName = collaborator.getName();
            collaboratorCategory = collaborator.getCategory().toString();
        }

        String dueDate = "";
        if (task.getDueDate() != null) {
            dueDate = task.getDueDate().toString();
        }

        return task.getTitle() + "," +
                task.getDescription() + "," +
                subTaskTitle + "," +
                task.getStatus() + "," +
                task.getPriorityLevel() + "," +
                dueDate + "," +
                projectName + "," +
                projectDescription + "," +
                collaboratorName + "," +
                collaboratorCategory;
    }

    private static String writeRow(Task task, Project project) {
        return writeRow(task, project, null, null);
    }

    public static void exportTasks(
            String filePath,
            List<Task> tasks,
            List<Collaborator> collaborators
        ) {

        try (FileWriter writer = new FileWriter(filePath)) {
            // write the columns to the export files
            writer.write(
                    "TaskName,Description,Subtask,Status,Priority,DueDate,ProjectName,ProjectDescription,Collaborator,CollaboratorCategory\n");

            for (Task task : tasks) {

                Project project = task.getProject();
                List<SubTask> subTasks = task.getSubTasks();

                if (subTasks.isEmpty()) {
                    // no subtask-> only 1 row added
                    writer.write(writeRow(task, project) + "\n");

                } else {
                    // if theres a subtask, then add rows for each subtask with the same info (just
                    // subtask will change in each row)
                    for (SubTask subTask : subTasks) {

                        Collaborator collaborator = getCollaboratorOfSubTask(subTask, collaborators);

                        writer.write(
                                writeRow(task, project, subTask, collaborator) + "\n");
                    }
                }
            }

            System.out.println("- Export completed at path: " + filePath);

        } catch (IOException e) {
            System.out.println("- Error exporting CSV: " + e.getMessage());
        }
    }
}