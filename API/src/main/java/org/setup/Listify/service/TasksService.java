package org.setup.Listify.service;

import org.setup.Listify.model.Tasks;
import org.setup.Listify.repo.TasksRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TasksService {

    private final TasksRepository repository;
    private final UserService userService;

    public TasksService(TasksRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }


    public List<Tasks> getAllTasks(Long userID) {
        return repository.findTasksByUserID(userID);
    }

    public Tasks getTaskById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        return repository.getTaskById(userID, id);
    }

    public List<Tasks> getAllSubtasksOfTask(Long parentTaskID) {
        getTaskById(parentTaskID);
        return repository.getAllSubtasksOfTask(parentTaskID);
    }

    public Tasks getDependentTaskById(Long taskID) {
        getTaskById(taskID);
        return repository.findDependentTaskByTaskID(taskID);
    }


    public Long createTask(int teamLeaderID, int projectID, int sectionID,
                           String taskName, String taskDescription,
                           byte taskPriority, byte taskPosition) {

        repository.createTask(teamLeaderID, projectID,
                sectionID, taskName, taskDescription,
                taskPriority, taskPosition);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }

    public Long createSubTask(int teamLeaderID, int parentTaskID, String taskName, String taskDescription, int sectionID, LocalDateTime dueDate) {

        repository.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
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
