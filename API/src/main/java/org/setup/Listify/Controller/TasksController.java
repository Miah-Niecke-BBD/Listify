package org.setup.Listify.Controller;


import org.setup.Listify.Model.Tasks;
import org.setup.Listify.Repo.TasksRepository;
import org.setup.Listify.Service.TasksModelAssembler;
import org.setup.Listify.exceptions.TaskNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TasksController {

    private final TasksRepository repository;
    private final TasksModelAssembler assembler;

    public TasksController(TasksRepository repository, TasksModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/tasks")
    public CollectionModel<EntityModel<Tasks>> all() {
        return assembler.toCollectionModel(repository.findAll());
    }

    @GetMapping("/tasks/{id}")
    public EntityModel<Tasks> one(@PathVariable Long id) {
        Tasks task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return assembler.toModel(task);
    }
}
