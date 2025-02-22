package org.setup.Listify.Controller;

import org.setup.Listify.Model.Tasks;
import org.setup.Listify.Service.TasksService;
import org.setup.Listify.Service.TasksModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

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
    ResponseEntity<?> newTask(@RequestBody Tasks newTask) throws URISyntaxException {
        Tasks savedTask = tasksService.saveTask(newTask);
        EntityModel<Tasks> entityModel = assembler.toModel(savedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/tasks/{id}")
    @Transactional
    ResponseEntity<?> updateTask(@RequestBody Tasks updatedTask, @PathVariable Long id) {
        Tasks savedTask = tasksService.updateTask(id, updatedTask);
        EntityModel<Tasks> entityModel = assembler.toModel(savedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
