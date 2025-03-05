package org.listify.controller;

import org.listify.model.Tasks;
import org.listify.service.TasksService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TasksService tasksService;
    private final UserService userService;

    public TasksController(TasksService tasksService, UserService userService) {
        this.tasksService = tasksService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Tasks>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<Tasks> tasks = tasksService.getAllTasks(userID);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskID}")
    public ResponseEntity<Object> getTaskById(@PathVariable("taskID") Long taskID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        Tasks task = tasksService.getTaskById(taskID, userID);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{parentTaskID}/subtask")
    public ResponseEntity<Object> getAllSubtasksOfTask(@PathVariable("parentTaskID") Long parentTaskID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<Tasks> subtasks = tasksService.getAllSubtasksOfTask(parentTaskID, userID);
        return ResponseEntity.ok(subtasks);
    }

    @GetMapping("/{taskID}/dependent")
    public ResponseEntity<Object> getDependentTaskByTaskId(@PathVariable("taskID") Long taskID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        Tasks dependentTask = tasksService.getDependentTaskById(taskID, userID);
        return ResponseEntity.ok(dependentTask);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> newTask(@RequestParam(name = "teamLeaderID") Long teamLeaderID,
                                     Authentication authentication,
                                     @RequestParam(name = "projectID") Long projectID,
                                     @RequestParam(name = "sectionID") Long sectionID,
                                     @RequestParam(name = "taskName") String taskName,
                                     @RequestParam(name = "taskDescription") String taskDescription,
                                     @RequestParam(name = "taskPriority") Byte taskPriority,
                                     @RequestParam(name = "taskPosition") Byte taskPosition) {

        Long userID = userService.getUserIDFromAuthentication(authentication);
        Long newTaskID = tasksService.createTask(teamLeaderID, projectID, sectionID, taskName, taskDescription, taskPriority, taskPosition);
        Tasks newTask = tasksService.getTaskById(newTaskID, userID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PostMapping("/{parentTaskID}/subtask")
    @Transactional
    public ResponseEntity<Object> newSubTask(@PathVariable("parentTaskID") Long parentTaskID,
                                        Authentication authentication,
                                        @RequestParam(name = "taskName") String taskName,
                                        @RequestParam(name = "taskDescription") String taskDescription,
                                        @RequestParam(name = "sectionID") Long sectionID,
                                        @RequestParam(name = "dueDate") LocalDateTime dueDate) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        Long newTaskID = tasksService.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);
        Tasks newTask = tasksService.getTaskById(newTaskID, teamLeaderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{taskID}")
    @Transactional
    ResponseEntity<Object> updateTask(@PathVariable("taskID") Long taskID,
                                 Authentication authentication,
                                 @RequestParam("newTaskName") String newTaskName,
                                 @RequestParam("newTaskDescription") String newTaskDescription,
                                 @RequestParam("newTaskPriority") Byte newTaskPriority,
                                 @RequestParam("newDueDate") LocalDateTime newDueDate) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        tasksService.updateTaskDetails(taskID, teamLeaderID, newTaskName, newTaskDescription, newTaskPriority, newDueDate);
        Tasks updatedTask = tasksService.getTaskById(taskID, teamLeaderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
    }


    @PutMapping("/{taskID}/position")
    @Transactional
    ResponseEntity<Object> updateTaskPosition(@PathVariable("taskID") Long taskID,
                                         Authentication authentication,
                                         @RequestParam(name = "newTaskPosition") Long newTaskPosition,
                                         @RequestParam(name = "sectionID") Long sectionID) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        tasksService.updateTaskPosition(teamLeaderID, taskID, newTaskPosition, sectionID);
        Tasks updatedTask = tasksService.getTaskById(taskID, teamLeaderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
    }


    @DeleteMapping("/{taskID}")
    @Transactional
    ResponseEntity<Object> deleteTask(@PathVariable("taskID") Long taskID, Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        tasksService.deleteTaskById(taskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
