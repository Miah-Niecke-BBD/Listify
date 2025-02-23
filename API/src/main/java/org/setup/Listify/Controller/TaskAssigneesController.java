package org.setup.Listify.Controller;

import org.setup.Listify.Model.TaskAssignees;
import org.setup.Listify.Service.TaskAssigneesModelAssembler;
import org.setup.Listify.Service.TaskAssigneesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final TaskAssigneesModelAssembler assembler;

    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, TaskAssigneesModelAssembler assembler) {
        this.taskAssigneesService = taskAssigneesService;
        this.assembler = assembler;
    }

    @GetMapping("/tasks/assigned")
    public CollectionModel<EntityModel<TaskAssignees>> getAllAssignedTasks() {
        List<TaskAssignees> assignedTasks = taskAssigneesService.getAllAssignedTasks();
        return assembler.toCollectionModel(assignedTasks);
    }

    @GetMapping("/tasks/assigned/{id}")
    public EntityModel<TaskAssignees> getAssignedTaskById(@PathVariable Long id) {
        TaskAssignees assignedTask = taskAssigneesService.getAssignedTaskById(id);
        return assembler.toModel(assignedTask);
    }

    @GetMapping("/tasks/assigned/user/{id}")
    public CollectionModel<EntityModel<TaskAssignees>> getTasksAssignedToSpecificUser(
            @PathVariable Long id) {

        List<TaskAssignees> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(id);

        return assembler.toCollectionModel(tasksAssignedToUser);
    }


}
