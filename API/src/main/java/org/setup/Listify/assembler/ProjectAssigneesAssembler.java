package org.setup.listify.assembler;

import org.setup.listify.controller.ProjectAssigneesController;
import org.setup.listify.model.ProjectAssignees;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectAssigneesAssembler implements RepresentationModelAssembler<ProjectAssignees, EntityModel<ProjectAssignees>> {

    @Override
    public EntityModel<ProjectAssignees> toModel(ProjectAssignees projectAssignee) {
        return EntityModel.of(projectAssignee,
                linkTo(methodOn(ProjectAssigneesController.class).getProjectAssigneeById(projectAssignee.getProjectAssigneeID())).withSelfRel(),
                linkTo(methodOn(ProjectAssigneesController.class).getAllProjectAssignees()).withRel("projectAssignees"));
    }

    @Override
    public CollectionModel<EntityModel<ProjectAssignees>> toCollectionModel(Iterable<? extends ProjectAssignees> projectAssignees) {
        List<EntityModel<ProjectAssignees>> assigneesModel = StreamSupport.stream(projectAssignees.spliterator(), false)
                .map(this::toModel)
                .toList();

        return CollectionModel.of(assigneesModel,
                linkTo(methodOn(ProjectAssigneesController.class).getAllProjectAssignees()).withSelfRel());
    }
}
