package org.setup.listify.service;

import org.setup.listify.dto.SimpleTaskDTO;
import org.setup.listify.dto.SimpleUserDTO;
import org.setup.listify.dto.ViewTaskDTO;
import org.setup.listify.exception.BadRequestException;
import org.setup.listify.exception.ForbiddenException;
import org.setup.listify.exception.NotFoundException;
import org.setup.listify.model.Tasks;
import org.setup.listify.repo.TasksRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TasksService {

    private final TasksRepository repository;

    public TasksService(TasksRepository repository) {
        this.repository = repository;
    }


    public List<ViewTaskDTO> getAllTasks(Long userID) {
        List<Tasks> tasks = repository.findTasksByUserID(userID);
        if (tasks.isEmpty()) {
            throw new NotFoundException("No tasks found");
        }

        List<ViewTaskDTO> taskDTOs = new ArrayList<>();
        for (Tasks task : tasks) {
            ViewTaskDTO taskDTO = mapTaskToViewTaskDTO(task);
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }


    public ViewTaskDTO getTaskById(Long taskID, Long userID) {
        validateUserAccessToTask(userID, taskID);

        ViewTaskDTO taskDTO = repository.getTaskInformation(taskID);
        List<SimpleUserDTO> assignees = repository.getUsersAssignedToTask(taskID);
        SimpleTaskDTO dependentTask = getDependentTaskByTaskID(taskID);
        if (taskDTO == null) {
            throw new NotFoundException("Task not found");
        }
        taskDTO.setTaskAssignees(assignees);
        taskDTO.setDependantTask(dependentTask);
        return taskDTO;
    }

    public ViewTaskDTO getTaskDetails(Long userID, Long taskID) {
        validateUserAccessToTask(userID, taskID);

        ViewTaskDTO taskDTO = repository.getTaskInformation(taskID);
        List<SimpleUserDTO> assignees = repository.getUsersAssignedToTask(taskID);
        SimpleTaskDTO dependentTask = getDependentTaskByTaskID(taskID);
        if (taskDTO == null) {
            throw new NotFoundException("Task: "+taskID+" does not exist");
        }
        taskDTO.setTaskAssignees(assignees);
        taskDTO.setDependantTask(dependentTask);
        return taskDTO;
    }


    public List<ViewTaskDTO> getAllSubtasksOfTask(Long parentTaskID, Long userID) {
        validateUserAccessToTask(userID, parentTaskID);
        List<Tasks> subtasks = repository.getAllSubtasksOfTask(parentTaskID);
        if (subtasks.isEmpty()) {
            throw new NotFoundException("There are no subtasks for task: "+parentTaskID);
        }
        List<ViewTaskDTO> taskDTOs = new ArrayList<>();
        for (Tasks task : subtasks) {
            ViewTaskDTO taskDTO = mapTaskToViewTaskDTO(task);
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }

    public ViewTaskDTO getDependentTaskById(Long taskID, Long userID) {
        validateUserAccessToTask(userID, taskID);
        Tasks task = repository.findDependentTaskByTaskID(taskID);
        if (task == null) {
            throw new NotFoundException("There are no dependencies for task: " + taskID);
        }

        return mapTaskToViewTaskDTO(task);
    }


    public Long createTask(Long teamLeaderID, Long projectID, Long sectionID,
                           String taskName, String taskDescription,
                           Byte taskPriority, Byte taskPosition) {

        if (taskName.length() > 100) {
            throw new BadRequestException("Task name has a maximum of 100 characters");
        }

        if (taskDescription.length() > 500) {
            throw new BadRequestException("Task description has a maximum of 500 characters");
        }

        if (taskPriority > 3 || taskPriority < 1) {
            throw new BadRequestException("Task Priority should be between 1 & 3");
        }

        repository.createTask(teamLeaderID, projectID,
                sectionID, taskName, taskDescription,
                taskPriority, taskPosition);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }

    public Long createSubTask(Long teamLeaderID, Long parentTaskID, String taskName, String taskDescription, Long sectionID, LocalDateTime dueDate) {
        validateInputs(taskName, taskDescription, dueDate);
        repository.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }


    public void updateTaskDetails(Long taskID, Long teamLeaderID, String newTaskName,
                                  String newTaskDescription, Byte newTaskPriority,
                                  LocalDateTime newDate) {

        validateInputs(newTaskName, newTaskDescription, newDate);
        repository.updateTaskDetails(taskID, teamLeaderID, newTaskName,
                newTaskDescription, newTaskPriority, newDate);
    }


    public void updateTaskPosition (Long teamLeaderID, Long taskID, Long newTaskPosition, Long sectionID) {
        Integer taskInSection = repository.findTaskInSection(sectionID, taskID);
        if (taskInSection == null || taskInSection == 0) {
            throw new BadRequestException("Task: "+taskID+" does not exist in section: "+sectionID);
        }
        repository.updateTaskPosition(teamLeaderID, taskID, newTaskPosition, sectionID);
    }


    public void deleteTaskById(Long taskID, Long teamLeaderID) {
        validateUserAccessToTask(teamLeaderID, taskID);
        repository.deleteTasksById(taskID, teamLeaderID);
    }

    public SimpleTaskDTO getDependentTaskByTaskID(Long taskID) {
        return repository.getDependantTaskByTaskID(taskID);
    }

    public ViewTaskDTO mapTaskToViewTaskDTO(Tasks task) {
        String priorityLabelName = repository.getPriorityLabelNameByTaskID(task.getTaskID());
        ViewTaskDTO taskDTO = new ViewTaskDTO(
                task.getTaskID(),
                task.getTaskName(),
                task.getTaskDescription(),
                priorityLabelName,
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate(),
                null,
                null
        );

        List<SimpleUserDTO> assignees = repository.getUsersAssignedToTask(task.getTaskID());
        SimpleTaskDTO dependentTask = getDependentTaskByTaskID(task.getTaskID());
        taskDTO.setTaskAssignees(assignees);
        taskDTO.setDependantTask(dependentTask);
        return taskDTO;
    }


    public void validateUserAccessToTask(Long userID, Long taskID) {
        Integer hasAccess = repository.userHasAccessToTask(userID, taskID);
        if (hasAccess == null || hasAccess == 0) {
            throw new ForbiddenException("User does not have access to this task");
        }
    }


    private void validateInputs(String taskName, String taskDescription, LocalDateTime dueDate) {
        if (taskName.length() > 100) {
            throw new BadRequestException("Task name has a maximum of 100 characters");
        }

        if (taskDescription.length() > 500) {
            throw new BadRequestException("Task description has a maximum of 500 characters");
        }

        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Due dates can only be in the future");
        }
    }
}
