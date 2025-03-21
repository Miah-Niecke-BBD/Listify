package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.listify.dto.UpdateTaskDTO;
import org.listify.dto.UpdateTaskPositionDTO;
import org.listify.dto.ViewTaskDTO;
import org.listify.model.PriorityLabels;
import org.listify.service.TasksService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tasks")
public class TasksController {

    private final TasksService tasksService;
    private final UserService userService;

    public TasksController(TasksService tasksService, UserService userService) {
        this.tasksService = tasksService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<ViewTaskDTO>> getAllTasks(HttpServletRequest request) {

        Long userID = userService.getUserIDFromAuthentication(request);
        List<ViewTaskDTO> tasks = tasksService.getAllTasks(userID);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/{taskID}")
    public ResponseEntity<ViewTaskDTO> getTaskById(@PathVariable("taskID") Long taskID, HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);
        ViewTaskDTO task = tasksService.getTaskById(taskID, userID);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/{parentTaskID}/subtask")
    public ResponseEntity<List<ViewTaskDTO>> getAllSubtasksOfTask(@PathVariable("parentTaskID") Long parentTaskID, HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);
        List<ViewTaskDTO> subtasks = tasksService.getAllSubtasksOfTask(parentTaskID, userID);
        return ResponseEntity.ok(subtasks);
    }


    @GetMapping("/{taskID}/dependent")
    public ResponseEntity<ViewTaskDTO> getDependentTaskByTaskId(@PathVariable("taskID") Long taskID, HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);
        ViewTaskDTO dependentTask = tasksService.getDependentTaskById(taskID, userID);
        return ResponseEntity.ok(dependentTask);
    }


    @GetMapping("/labels")
    public ResponseEntity<List<PriorityLabels>> getAllLabels() {
        List<PriorityLabels> labels = tasksService.getAllLabels();
        return ResponseEntity.ok(labels);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<ViewTaskDTO> newTask(HttpServletRequest request,
                                     @RequestParam(name = "projectID") Long projectID,
                                     @RequestParam(name = "sectionID") Long sectionID,
                                     @RequestParam(name = "taskName") String taskName,
                                     @RequestParam(name = "taskDescription", required = false) String taskDescription,
                                     @RequestParam(name = "taskPriority") Byte taskPriority,
                                     @RequestParam(name = "dueDate", required = false) OffsetDateTime dueDate) {

        Long userID = userService.getUserIDFromAuthentication(request);
        Long newTaskID = tasksService.createTask(userID, projectID, sectionID, taskName, taskDescription, taskPriority, dueDate);
        ViewTaskDTO newTask = tasksService.getTaskById(newTaskID, userID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }


    @PostMapping("/{parentTaskID}/subtask")
    @Transactional
    public ResponseEntity<ViewTaskDTO> newSubTask(@PathVariable("parentTaskID") Long parentTaskID,
                                                  HttpServletRequest request,
                                        @RequestParam(name = "taskName") String taskName,
                                        @RequestParam(name = "taskDescription", required = false) String taskDescription,
                                        @RequestParam(name = "sectionID") Long sectionID,
                                        @RequestParam(name = "dueDate", required = false) OffsetDateTime dueDate) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        Long newTaskID = tasksService.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);
        ViewTaskDTO newTask = tasksService.getTaskById(newTaskID, teamLeaderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }


    @PutMapping("/{taskID}")
    @Transactional
    ResponseEntity<ViewTaskDTO> updateTask(@PathVariable("taskID") Long taskID,
                                           HttpServletRequest request,
                                           @Valid @RequestBody UpdateTaskDTO updatedTask) {
        Long userID = userService.getUserIDFromAuthentication(request);
        tasksService.updateTaskDetails(userID, taskID, updatedTask);
        ViewTaskDTO taskDTO = tasksService.getTaskById(taskID, userID);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }


    @PutMapping("/{taskID}/position")
    @Transactional
    ResponseEntity<ViewTaskDTO> updateTaskPosition(@PathVariable("taskID") Long taskID,
                                                   HttpServletRequest request,
                                                   @RequestBody UpdateTaskPositionDTO updatedPosition) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        tasksService.updateTaskPosition(teamLeaderID, taskID, updatedPosition);
        ViewTaskDTO updatedTask = tasksService.getTaskById(taskID, teamLeaderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
    }


    @DeleteMapping("/{taskID}")
    @Transactional
    ResponseEntity<?> deleteTask(@PathVariable("taskID") Long taskID, HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        tasksService.deleteTaskById(taskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
