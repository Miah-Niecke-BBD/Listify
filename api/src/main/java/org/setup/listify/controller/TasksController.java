package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.Tasks;
import org.setup.listify.service.TasksService;
import org.setup.listify.service.UserService;
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

        if (tasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskID}")
    public ResponseEntity<?> getTaskById(@PathVariable("taskID") Long taskID) {
        Tasks task = tasksService.getTaskById(taskID);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find task with id: "+ taskID);
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/subtask/{parentTaskID}")
    public ResponseEntity<?> getAllSubtasksOfTask(@PathVariable("parentTaskID") Long parentTaskID) {
        List<Tasks> subtasks = tasksService.getAllSubtasksOfTask(parentTaskID);
        if (subtasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no subtasks for task: "+parentTaskID);
        }
        return ResponseEntity.ok(subtasks);
    }

    @GetMapping("dependent/{taskID}")
    public ResponseEntity<?> getDependentTaskByTaskId(@PathVariable("taskID") Long taskID) {
        Tasks dependentTask = tasksService.getDependentTaskById(taskID);
        if (dependentTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no dependencies for task: " + taskID);
        }
        return ResponseEntity.ok(dependentTask);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newTask(@RequestParam(name = "teamLeaderID", required = false) Integer teamLeaderID,
                                     @RequestParam(name = "projectID", required = false) Integer projectID,
                                     @RequestParam(name = "sectionID", required = false) Integer sectionID,
                                     @RequestParam(name = "taskName", required = false) String taskName,
                                     @RequestParam(name = "taskDescription", required = false) String taskDescription,
                                     @RequestParam(name = "taskPriority", required = false) Byte taskPriority,
                                     @RequestParam(name = "taskPosition", required = false) Byte taskPosition) {

        if (teamLeaderID == null || projectID == null || sectionID == null || taskName == null || taskPriority == null || taskPosition == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskID = tasksService.createTask(teamLeaderID, projectID, sectionID, taskName, taskDescription, taskPriority, taskPosition);
        Tasks newTask = tasksService.getTaskById(newTaskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PostMapping("/subtask/{parentTaskID}")
    @Transactional
    public ResponseEntity<?> newSubTask(@PathVariable("parentTaskID") Integer parentTaskID,
                                        Authentication authentication,
                                        @RequestParam(name = "taskName", required = false) String taskName,
                                        @RequestParam(name = "taskDescription", required = false) String taskDescription,
                                        @RequestParam(name = "sectionID", required = false) Integer sectionID,
                                        @RequestParam(name = "dueDate", required = false) LocalDateTime dueDate) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        if (sectionID == null || taskName == null || dueDate == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskID = tasksService.createSubTask(teamLeaderID, parentTaskID, taskName, taskDescription, sectionID, dueDate);

        Tasks newTask = tasksService.getTaskById(newTaskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{taskID}")
    @Transactional
    ResponseEntity<?> updateTask(@PathVariable("taskID") Long taskID,
                                 Authentication authentication,
                                 @RequestParam("newTaskName") String newTaskName,
                                 @RequestParam("newTaskDescription") String newTaskDescription,
                                 @RequestParam("newTaskPriority") Byte newTaskPriority,
                                 @RequestParam("newDueDate") LocalDateTime newDueDate) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        tasksService.updateTaskDetails(taskID.intValue(), teamLeaderID, newTaskName, newTaskDescription, newTaskPriority, newDueDate);
        Tasks updatedTask = tasksService.getTaskById(taskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
    }


    @PutMapping("/{taskID}/position")
    @Transactional
    ResponseEntity<?> updateTaskPosition(@PathVariable("taskID") Long taskID,
                                         Authentication authentication,
                                         @RequestParam(name = "newTaskPosition", required = false) Integer newTaskPosition,
                                         @RequestParam(name = "sectionID", required = false) Integer sectionID) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        if (sectionID == null || newTaskPosition == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        tasksService.updateTaskPosition(teamLeaderID, taskID.intValue(), newTaskPosition, sectionID);
        Tasks updatedTask = tasksService.getTaskById(taskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
    }


    @DeleteMapping("/{taskID}")
    @Transactional
    ResponseEntity<?> deleteTask(@PathVariable("taskID") Long taskID, Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        tasksService.deleteTaskById(taskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
