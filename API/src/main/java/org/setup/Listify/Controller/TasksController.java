package org.setup.Listify.Controller;

import org.setup.Listify.Exception.ErrorResponse;
import org.setup.Listify.Model.Tasks;
import org.setup.Listify.Service.TasksService;
import org.setup.Listify.Assembler.TasksModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TasksService tasksService;
    private final TasksModelAssembler assembler;

    public TasksController(TasksService tasksService, TasksModelAssembler assembler) {
        this.tasksService = tasksService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Tasks>> getAllTasks() {
        List<Tasks> tasks = tasksService.getAllTasks();
        return assembler.toCollectionModel(tasks);
    }

    @GetMapping("/{id}")
    public EntityModel<Tasks> getTaskById(@PathVariable Long id) {
        Tasks task = tasksService.getTaskById(id);
        return assembler.toModel(task);
    }

    @GetMapping("/subtask/{parentTaskID}")
    public CollectionModel<EntityModel<Tasks>> getAllSubtasksOfTask(@PathVariable Long parentTaskID) {
        List<Tasks> subtasks = tasksService.getAllSubtasksOfTask(parentTaskID);
        return assembler.toCollectionModel(subtasks);
    }

    @GetMapping("dependent/{taskID}")
    public EntityModel<Tasks> getDependentTaskByTaskId(@PathVariable Long taskID) {
        Tasks dependentTask = tasksService.getDependentTaskById(taskID);
        return assembler.toModel(dependentTask);
    }

    @GetMapping("/section/{sectionId}")
    public CollectionModel<EntityModel<Tasks>> getTaskBySectionId(@PathVariable Long sectionId) {
        List<Tasks> tasksList = tasksService.getTaskBySectionId(sectionId);
        return assembler.toCollectionModel(tasksList);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newTask(@RequestParam(required = false) Integer teamLeaderID,
                                     @RequestParam(required = false) Integer projectID,
                                     @RequestParam(required = false) Integer sectionID,
                                     @RequestParam(required = false) String taskName,
                                     @RequestParam(required = false) String taskDescription,
                                     @RequestParam(required = false) Byte taskPriority,
                                     @RequestParam(required = false) Byte taskPosition) {
        if (teamLeaderID == null || projectID == null || sectionID == null || taskName == null || taskPriority == null || taskPosition == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskID = tasksService.createTask(teamLeaderID, projectID, sectionID, taskName, taskDescription, taskPriority, taskPosition);
        Tasks newTask = tasksService.getTaskById(newTaskID);
        EntityModel<Tasks> entityModel = assembler.toModel(newTask);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @PostMapping("/subtask/{parentTaskID}")
    @Transactional
    public ResponseEntity<?> newSubTask(@PathVariable Long parentTaskID,
                                        @RequestParam(required = false) Integer teamLeaderID,
                                        @RequestParam(required = false) String taskName,
                                        @RequestParam(required = false) String taskDescription,
                                        @RequestParam(required = false) Integer sectionID,
                                        @RequestParam(required = false) LocalDateTime dueDate) {
        if (teamLeaderID == null || sectionID == null || taskName == null || dueDate == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskID = tasksService.createSubTask(teamLeaderID, parentTaskID.intValue(), taskName, taskDescription, sectionID, dueDate);

        Tasks newTask = tasksService.getTaskById(newTaskID);
        EntityModel<Tasks> entityModel = assembler.toModel(newTask);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Transactional
    ResponseEntity<?> updateTask(@PathVariable Long id,
                                 @RequestParam Integer teamLeaderID,
                                 @RequestParam String newTaskName,
                                 @RequestParam String newTaskDescription,
                                 @RequestParam Byte newTaskPriority,
                                 @RequestParam LocalDateTime newDueDate) {
        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        tasksService.updateTaskDetails(id.intValue(), teamLeaderID, newTaskName, newTaskDescription, newTaskPriority, newDueDate);

        Tasks updatedTask = tasksService.getTaskById(id);
        EntityModel<Tasks> entityModel = assembler.toModel(updatedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping("/{id}/position")
    @Transactional
    ResponseEntity<?> updateTaskPosition(@PathVariable Long id,
                                         @RequestParam(required = false) Integer teamLeaderID,
                                         @RequestParam(required = false) Integer newTaskPosition,
                                         @RequestParam(required = false) Integer sectionID) {
        if (teamLeaderID == null || sectionID == null || newTaskPosition == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        tasksService.updateTaskPosition(teamLeaderID, id.intValue(), newTaskPosition, sectionID);

        Tasks updatedTask = tasksService.getTaskById(id);
        EntityModel<Tasks> entityModel = assembler.toModel(updatedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @DeleteMapping("/{id}")
    @Transactional
    ResponseEntity<?> deleteTask(@PathVariable Long id, @RequestParam(required = false) Integer teamLeaderID) {
        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        tasksService.deleteTaskById(id, teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Task with id: "+ id +" has been successfully deleted"));
    }
}
