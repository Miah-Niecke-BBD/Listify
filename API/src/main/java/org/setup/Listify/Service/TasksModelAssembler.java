package org.setup.Listify.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.setup.Listify.Controller.TasksController;
import org.setup.Listify.Model.Tasks;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class TasksModelAssembler implements RepresentationModelAssembler<Tasks, EntityModel<Tasks>> {
    @Override
    public EntityModel<Tasks> toModel(Tasks task) {
        return EntityModel.of(task,
                linkTo(methodOn(TasksController.class).one(task.getTaskID())).withSelfRel(),
                linkTo(methodOn(TasksController.class).all()).withRel("tasks"));
    }

    @Override
    public CollectionModel<EntityModel<Tasks>> toCollectionModel(Iterable<? extends Tasks> tasks) {
        List<EntityModel<Tasks>> tasksModel = StreamSupport.stream(tasks.spliterator(), false)
                .map(this::toModel)
                .toList();

        return CollectionModel.of(tasksModel,
                linkTo(methodOn(TasksController.class).all()).withSelfRel());
    }
}
