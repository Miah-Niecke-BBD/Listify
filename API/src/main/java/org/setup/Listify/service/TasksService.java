package org.setup.Listify.service;

import org.setup.Listify.exception.ListNotFoundException;
import org.setup.Listify.exception.SectionNotFoundException;
import org.setup.Listify.exception.TaskDependencyNotFoundException;
import org.setup.Listify.model.Tasks;
import org.setup.Listify.repo.TasksRepository;
import org.setup.Listify.exception.TaskNotFoundException;
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
        List<Tasks> tasks = repository.findAll();
        if (tasks.isEmpty()) {
            throw new ListNotFoundException("tasks");
        }
        return tasks;
    }


    public Tasks getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    public List<Tasks> getTaskBySectionId(Long sectionId) {
        List<Tasks> tasksInSection = repository.findTasksBySectionID(sectionId);
        if (tasksInSection.isEmpty()) {
            throw new SectionNotFoundException(sectionId);
        }
        return tasksInSection;
    }

    public List<Tasks> getAllSubtasksOfTask(Long parentTaskID) {
        List<Tasks> subtasks = repository.getAllSubtasksOfTask(parentTaskID);
        if (subtasks.isEmpty()) {
            throw new ListNotFoundException("subtasks");
        }
        return subtasks;
    }

    public Tasks getDependentTaskById(Long taskID) {
        Tasks dependentTask = repository.findDependentTaskByTaskID(taskID);
        if (dependentTask == null) {
            throw new TaskDependencyNotFoundException(taskID);
        }
        return dependentTask;
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
