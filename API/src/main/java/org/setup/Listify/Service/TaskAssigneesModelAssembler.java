package org.setup.Listify.Service;


import org.setup.Listify.Controller.TaskAssigneesController;
import org.setup.Listify.Model.TaskAssignees;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskAssigneesModelAssembler implements RepresentationModelAssembler<TaskAssignees, EntityModel<TaskAssignees>> {
    @Override
    public EntityModel<TaskAssignees> toModel(TaskAssignees assignedTask) {
        return EntityModel.of(assignedTask,
                linkTo(methodOn(TaskAssigneesController.class).getAssignedTaskById(assignedTask.getTaskAssigneeID())).withSelfRel(),
                linkTo(methodOn(TaskAssigneesController.class).getAllAssignedTasks()).withRel("taskAssignees"));
    }

    @Override
    public CollectionModel<EntityModel<TaskAssignees>> toCollectionModel(Iterable<? extends TaskAssignees> taskAssignees) {
        List<EntityModel<TaskAssignees>> assigneesModel = StreamSupport.stream(taskAssignees.spliterator(), false)
                .map(this::toModel)
                .toList();

        return CollectionModel.of(assigneesModel,
                linkTo(methodOn(TaskAssigneesController.class).getAllAssignedTasks()).withSelfRel());
    }
}
