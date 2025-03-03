package org.setup.listify.assembler;


import org.setup.listify.controller.TaskDependenciesController;
import org.setup.listify.model.TaskDependencies;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskDependenciesModelAssembler implements RepresentationModelAssembler<TaskDependencies, EntityModel<TaskDependencies>> {

    @Override
    public EntityModel<TaskDependencies> toModel(TaskDependencies taskDependency) {
        return EntityModel.of(taskDependency,
                linkTo(methodOn(TaskDependenciesController.class).getTaskDependenciesById(taskDependency.getTaskID())).withSelfRel(),
                linkTo(methodOn(TaskDependenciesController.class).getAllTaskDependencies()).withRel("taskDependencies"));
    }

    @Override
    public CollectionModel<EntityModel<TaskDependencies>> toCollectionModel(Iterable<? extends TaskDependencies> taskDependencies) {
        List<EntityModel<TaskDependencies>> dependencyModel = StreamSupport.stream(taskDependencies.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(dependencyModel,
                linkTo(methodOn(TaskDependenciesController.class).getAllTaskDependencies()).withSelfRel());
    }
}
