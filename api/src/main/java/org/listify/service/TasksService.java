package org.listify.service;

import org.listify.dto.*;
import org.listify.exception.BadRequestException;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.Tasks;
import org.listify.repo.TasksRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        return tasks.stream()
                .map(this::mapTaskToViewTaskDTO)
                .collect(Collectors.toList());
    }


    public ViewTaskDTO getTaskById(Long taskID, Long userID) {
        validateUserAccessToTask(userID, taskID);

        Tasks task = repository.findById(taskID)
                .orElseThrow(() -> new NotFoundException("Task "+taskID+" is not found"));
        return mapTaskToViewTaskDTO(task);
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
        return subtasks.stream()
                .map(this::mapTaskToViewTaskDTO)
                .collect(Collectors.toList());
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
                           Byte taskPriority, OffsetDateTime dueDate) {

        validateTeamLeader(teamLeaderID, projectID);
        if (taskName.length() > 100) {
            throw new BadRequestException("Task name has a maximum of 100 characters");
        }

        if (taskDescription != null && taskDescription.length() > 500) {
            throw new BadRequestException("Task description has a maximum of 500 characters");
        }

        if (taskPriority > 3 || taskPriority < 1) {
            throw new BadRequestException("Task Priority should be between 1 & 3");
        }

        repository.createTask(teamLeaderID, projectID,
                sectionID, taskName, taskDescription,
                taskPriority, dueDate);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }


    public Long createSubTask(Long teamLeaderID, Long parentTaskID, String taskName, String taskDescription, Long sectionID, OffsetDateTime dueDate) {

        validateInputs(taskName, taskDescription, dueDate);
        repository.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);

        Tasks newlyAddedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAddedTask != null ? newlyAddedTask.getTaskID() : null;
    }


    public void updateTaskDetails(Long userID, Long taskID, UpdateTaskDTO updatedTask) {
        Tasks task = repository.findById(taskID)
                    .orElseThrow(() -> new NotFoundException("Task :"+taskID+" not found"));

        String newTaskName = task.getTaskName();
        String newTaskDescription = task.getTaskDescription();
        Long newTaskPriority = task.getTaskPriority();
        OffsetDateTime newDueDate = task.getDueDate();
        OffsetDateTime dateCompleted = task.getDateCompleted();

        if (updatedTask.getTaskName() != null) {
            if (updatedTask.getTaskName().trim().length() > 100) {
                throw new BadRequestException("Task name cannot be more than 100 characters");
            }
            newTaskName = updatedTask.getTaskName();
        }

        if (updatedTask.getTaskDescription() != null) {
            if (updatedTask.getTaskDescription().trim().length() > 500) {
                throw new BadRequestException("Task description cannot be more than 500 characters");
            }
            newTaskDescription = updatedTask.getTaskDescription();
        }

        if (updatedTask.getTaskPriority() != null) {
            if (updatedTask.getTaskPriority() < 1 || updatedTask.getTaskPriority() > 3) {
                throw new BadRequestException("Task Priority should be between 1 & 3");
            }
            newTaskPriority = Long.valueOf(updatedTask.getTaskPriority());
        }

        if (updatedTask.getDueDate() != null) {
            if (updatedTask.getDueDate().isBefore(OffsetDateTime.now())) {
                throw new BadRequestException("Due dates can only be in the future");
            }
            newDueDate = updatedTask.getDueDate();
        }

        if (updatedTask.getDateCompleted() != null) {
            dateCompleted = updatedTask.getDateCompleted();
        }

        repository.updateTaskDetails(taskID, userID, newTaskName, newTaskDescription, newTaskPriority, newDueDate, dateCompleted);
    }


    public void updateTaskPosition (Long teamLeaderID, Long taskID, UpdateTaskPositionDTO updatedPosition) {
        Integer taskInSection = repository.findTaskInSection(updatedPosition.getSectionID(), taskID);
        if (taskInSection == null || taskInSection == 0) {
            throw new BadRequestException("Task: "+taskID+" does not exist in section: "+updatedPosition.getSectionID());
        }
        repository.updateTaskPosition(teamLeaderID, taskID, updatedPosition.getTaskPosition(), updatedPosition.getSectionID());
    }


    public void deleteTaskById(Long taskID, Long teamLeaderID) {
        validateUserAccessToTask(teamLeaderID, taskID);
        repository.deleteTasksById(taskID, teamLeaderID);
    }

    public SimpleTaskDTO getDependentTaskByTaskID(Long taskID) {
        return repository.getDependantTaskByTaskID(taskID);
    }

    private ViewTaskDTO mapTaskToViewTaskDTO(Tasks task) {
        String priorityLabelName = repository.getPriorityLabelNameByTaskID(task.getTaskID());
        ViewTaskDTO taskDTO = new ViewTaskDTO(
                task.getTaskID(),
                task.getTaskName(),
                task.getTaskDescription(),
                priorityLabelName,
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate(),
                task.getDateCompleted(),
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

    private void validateTeamLeader(Long userID, Long taskID) {
        Integer isTeamLeader = repository.userIsTeamLeader(userID, taskID);
        if (isTeamLeader == null || isTeamLeader == 0) {
            throw new ForbiddenException("User: "+userID+" is not a team leader");
        }
    }

    private void validateInputs(String taskName, String taskDescription, OffsetDateTime dueDate) {
        if (taskName.length() > 100) {
            throw new BadRequestException("Task name has a maximum of 100 characters");
        }

        if (taskDescription != null && taskDescription.length() > 500) {
            throw new BadRequestException("Task description has a maximum of 500 characters");
        }

        if (dueDate != null && dueDate.isBefore(OffsetDateTime.now())) {
            throw new BadRequestException("Due dates can only be in the future");
        }
    }
}
