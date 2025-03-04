package org.setup.listify.assembler;

import org.setup.listify.model.UserTeamProjects;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.setup.listify.controller.TeamsController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamProjectsAssembler implements RepresentationModelAssembler<UserTeamProjects, EntityModel<UserTeamProjects>> {

    @Override
    public EntityModel<UserTeamProjects> toModel(UserTeamProjects teamProject) {
        EntityModel<UserTeamProjects> projectEntityModel = EntityModel.of(teamProject);

        projectEntityModel.add(
                linkTo(methodOn(TeamsController.class).getProjectsByTeamIDAndUserID(teamProject.getTeamID(), null)).withSelfRel(),
                linkTo(methodOn(TeamsController.class).getTeamById(teamProject.getTeamID())).withRel("team"),
                linkTo(methodOn(TeamsController.class).getTeamMembersByTeamId(teamProject.getTeamID())).withRel("teamMembers")
        );

        return projectEntityModel;
    }

    public CollectionModel<EntityModel<UserTeamProjects>> toModel(List<UserTeamProjects> userTeamProjects, Long teamID) {
        List<EntityModel<UserTeamProjects>> projectsEntityModels = userTeamProjects.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(projectsEntityModels);
    }
}
