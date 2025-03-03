package org.setup.listify.assembler;
import org.setup.listify.controller.ProjectsController;
import org.setup.listify.model.Projects;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectsModelAssembler implements RepresentationModelAssembler<Projects, EntityModel<Projects>> {
    @Override
    public EntityModel<Projects> toModel(Projects project) {
        return EntityModel.of(project,
                linkTo(methodOn(ProjectsController.class).getProjectsById(project.getProjectID())).withSelfRel(),
                linkTo(methodOn(ProjectsController.class).getAllProjects()).withRel("projects"),
                linkTo(methodOn(ProjectsController.class).getAllSectionsInProject(project.getProjectID())).withRel("sectionsInProject"));
    }

    @Override
    public CollectionModel<EntityModel<Projects>> toCollectionModel(Iterable<? extends Projects> projects) {
        List<EntityModel<Projects>> projectsModel = StreamSupport.stream(projects.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(projectsModel,
                linkTo(methodOn(ProjectsController.class).getAllProjects()).withSelfRel());
    }
}
