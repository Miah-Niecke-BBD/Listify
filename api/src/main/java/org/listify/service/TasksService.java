package org.listify.service;

import org.listify.exception.NotFoundException;
import org.listify.model.Tasks;
import org.listify.repo.TasksRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TasksService {

    private final TasksRepository repository;

    public TasksService(TasksRepository repository) {
        this.repository = repository;
    }


    public List<Tasks> getAllTasks(Long userID) {
        List<Tasks> tasks = repository.findTasksByUserID(userID);
        if (tasks.isEmpty()) {
            throw new NotFoundException("No tasks found");
        }
        return tasks;
    }


    public Tasks getTaskById(Long taskID, Long userID) {
        Tasks task = repository.getTaskById(userID, taskID);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        return task;
    }


    public List<Tasks> getAllSubtasksOfTask(Long parentTaskID, Long userID) {
        getTaskById(parentTaskID, userID);
        List<Tasks> subtasks = repository.getAllSubtasksOfTask(parentTaskID);
        if (subtasks.isEmpty()) {
            throw new NotFoundException("There are no subtasks for task: "+parentTaskID);
        }
        return subtasks;
    }

    public Tasks getDependentTaskById(Long taskID, Long userID) {
        getTaskById(taskID, userID);
        Tasks task = repository.findDependentTaskByTaskID(taskID);
        if (task == null) {
            throw new NotFoundException("There are no dependencies for task: " + taskID);
        }
        return task;
    }


    public Long createTask(Long teamLeaderID, Long projectID, Long sectionID,
                           String taskName, String taskDescription,
                           byte taskPriority, byte taskPosition) {

        repository.createTask(teamLeaderID, projectID,
                sectionID, taskName, taskDescription,
                taskPriority, taskPosition);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }

    public Long createSubTask(Long teamLeaderID, Long parentTaskID, String taskName, String taskDescription, Long sectionID, LocalDateTime dueDate) {

        repository.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }


    public void updateTaskDetails(Long taskID, Long teamLeaderID, String newTaskName,
                                  String newTaskDescription, byte newTaskPriority,
                                  LocalDateTime newDate) {

        repository.updateTaskDetails(taskID, teamLeaderID, newTaskName,
                newTaskDescription, newTaskPriority, newDate);
    }


    public void updateTaskPosition (Long teamLeaderID, Long taskID, Long newTaskPosition, Long sectionID) {
        repository.updateTaskPosition(teamLeaderID, taskID, newTaskPosition, sectionID);
    }


    public void deleteTaskById(Long id, Long teamLeaderID) {
        repository.deleteTasksById(id, teamLeaderID);
    }
}
