package org.setup.Listify.assembler;

import org.setup.Listify.model.TeamProjects;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.setup.Listify.controller.TeamsController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamProjectsAssembler implements RepresentationModelAssembler<TeamProjects, EntityModel<TeamProjects>> {

    @Override
    public EntityModel<TeamProjects> toModel(TeamProjects teamProject) {
        EntityModel<TeamProjects> projectEntityModel = EntityModel.of(teamProject);

        projectEntityModel.add(
                linkTo(methodOn(TeamsController.class).getProjectsByTeamID(teamProject.getTeamID())).withSelfRel(),
                linkTo(methodOn(TeamsController.class).getTeamById(teamProject.getTeamID())).withRel("team"),
                linkTo(methodOn(TeamsController.class).getTeamMembersByTeamId(teamProject.getTeamID())).withRel("teamMembers")
        );

        return projectEntityModel;
    }

    public CollectionModel<EntityModel<TeamProjects>> toModel(List<TeamProjects> teamProjects, Long teamID) {
        List<EntityModel<TeamProjects>> projectsEntityModels = teamProjects.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(projectsEntityModels);
    }
}
