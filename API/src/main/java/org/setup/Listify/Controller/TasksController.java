package org.setup.Listify.Controller;

import org.setup.Listify.Model.Tasks;
import org.setup.Listify.Service.TasksService;
import org.setup.Listify.Service.TasksModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class TasksController {

    private final TasksService tasksService;
    private final TasksModelAssembler assembler;

    public TasksController(TasksService tasksService, TasksModelAssembler assembler) {
        this.tasksService = tasksService;
        this.assembler = assembler;
    }

    @GetMapping("/tasks")
    public CollectionModel<EntityModel<Tasks>> getAllTasks() {
        List<Tasks> tasks = tasksService.getAllTasks();
        return assembler.toCollectionModel(tasks);
    }

    @GetMapping("/tasks/{id}")
    public EntityModel<Tasks> getTaskById(@PathVariable Long id) {
        Tasks task = tasksService.getTaskById(id);
        return assembler.toModel(task);
    }

    @GetMapping("/tasks/section/{sectionId}")
    public CollectionModel<EntityModel<Tasks>> getTaskBySectionId(@PathVariable Long sectionId) {
        List<Tasks> tasksList = tasksService.getTaskBySectionId(sectionId);
        return assembler.toCollectionModel(tasksList);
    }

    @PostMapping("/tasks")
    @Transactional
    public ResponseEntity<?> newTask(@RequestParam int teamLeaderID,
                                     @RequestParam int projectID,
                                     @RequestParam int sectionID,
                                     @RequestParam String taskName,
                                     @RequestParam(required = false) String taskDescription,
                                     @RequestParam byte taskPriority,
                                     @RequestParam byte taskPosition) {

        tasksService.createTask(teamLeaderID, projectID, sectionID, taskName, taskDescription, taskPriority, taskPosition);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Task created successfully"));
    }

    @PostMapping("/tasks/subtask/{parentTaskID}")
    @Transactional
    public ResponseEntity<?> newSubTask(@PathVariable Long parentTaskID,
                                        @RequestParam int teamLeaderID,
                                        @RequestParam String taskName,
                                        @RequestParam(required = false) String taskDescription,
                                        @RequestParam int sectionID,
                                        @RequestParam LocalDateTime dueDate) {

        tasksService.createSubTask(teamLeaderID, parentTaskID.intValue(), taskName, taskDescription, sectionID, dueDate);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Subtask created successfully", "parentTaskID", parentTaskID));
    }

    @PutMapping("/tasks/{id}")
    @Transactional
    ResponseEntity<?> updateTask(@PathVariable Long id,
                                 @RequestParam int teamLeaderID,
                                 @RequestParam String newTaskName,
                                 @RequestParam String newTaskDescription,
                                 @RequestParam byte newTaskPriority,
                                 @RequestParam LocalDateTime newDueDate) {

        tasksService.updateTaskDetails(id.intValue(), teamLeaderID, newTaskName, newTaskDescription, newTaskPriority, newDueDate);

        Tasks updatedTask = tasksService.getTaskById(id);
        EntityModel<Tasks> entityModel = assembler.toModel(updatedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping("/tasks/{id}/position")
    @Transactional
    ResponseEntity<?> updateTaskPosition(@PathVariable Long id,
                                         @RequestParam int teamLeaderID,
                                         @RequestParam int newTaskPosition,
                                         @RequestParam int sectionID) {
        tasksService.updateTaskPosition(teamLeaderID, id.intValue(), newTaskPosition, sectionID);

        Tasks updatedTask = tasksService.getTaskById(id);
        EntityModel<Tasks> entityModel = assembler.toModel(updatedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @DeleteMapping("/tasks/{id}")
    @Transactional
    ResponseEntity<?> deleteTask(@PathVariable Long id, @RequestParam int teamLeaderID) {
        tasksService.deleteTaskById(id, teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Task with id: "+ id +" has been successfully deleted"));
    }
}
