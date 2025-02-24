package org.setup.Listify.Service;

import org.setup.Listify.Model.Tasks;
import org.setup.Listify.Repo.TasksRepository;
import org.setup.Listify.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TasksService {

    private final TasksRepository repository;

    public TasksService(TasksRepository repository) {
        this.repository = repository;
    }


    public List<Tasks> getAllTasks() {
        return repository.findAll();
    }


    public Tasks getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    public List<Tasks> getTaskBySectionId(Long sectionId) {
        return repository.findTasksBySectionID(sectionId);
    }


    public Tasks getTaskByNameAndPosition(int sectionID, String taskName, byte taskPosition) {
        return repository.findBySectionIDAndTaskNameAndTaskPosition(sectionID, taskName, taskPosition);
    }


    public void createTask(int teamLeaderID, int projectID, int sectionID,
                           String taskName, String taskDescription,
                           byte taskPriority, byte taskPosition) {

        repository.createTask(teamLeaderID, projectID,
                sectionID, taskName, taskDescription,
                taskPriority, taskPosition);
    }

    public void createSubTask(int teamLeaderID, int parentTaskID, String taskName, String taskDescription, int sectionID, LocalDateTime dueDate) {
        repository.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);
    }


    public void updateTaskDetails(int taskID, int teamLeaderID, String newTaskName,
                                  String newTaskDescription, byte newTaskPriority,
                                  LocalDateTime newDate) {

        repository.updateTaskDetails(taskID, teamLeaderID, newTaskName,
                newTaskDescription, newTaskPriority, newDate);
    }


    public void updateTaskPosition (int teamLeaderID, int taskID, int newTaskPosition, int sectionID) {
        repository.updateTaskPosition(teamLeaderID, taskID, newTaskPosition, sectionID);
    }


    public void deleteTaskById(Long id, int teamLeaderID) {
        repository.deleteTasksById(id.intValue(), teamLeaderID);
    }
}
