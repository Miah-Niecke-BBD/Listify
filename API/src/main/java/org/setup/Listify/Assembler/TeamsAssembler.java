package org.setup.Listify.Assembler;

import org.setup.Listify.Controller.TeamsController;
import org.setup.Listify.Model.TeamMembers;
import org.setup.Listify.Model.TeamProjects;
import org.setup.Listify.Model.Teams;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamsAssembler {

    public EntityModel<Teams> toModel(Teams team) {
        EntityModel<Teams> teamEntityModel = EntityModel.of(team);

        teamEntityModel.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getTeamById(team.getTeamID())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getAllTeams()).withRel("allTeams"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getTeamMembersByTeamId(team.getTeamID())).withRel("teamMembers"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getProjectsByTeamID(team.getTeamID())).withRel("projects")
        );

        return teamEntityModel;
    }

    public EntityModel<TeamMembers> toModel(TeamMembers teamMembers) {
        EntityModel<TeamMembers> teamMembersEntityModel = EntityModel.of(teamMembers);

        teamMembersEntityModel.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getTeamMembersByTeamId(teamMembers.getTeamID())).withRel("membersInTeam"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getAllTeamMembers()).withRel("allMembers"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getTeamById(teamMembers.getTeamID())).withRel("team")

        );

        return teamMembersEntityModel;
    }

    public CollectionModel<EntityModel<TeamProjects>> toModel(List<TeamProjects> teamProjects, Long teamID) {
        List<EntityModel<TeamProjects>> projectsEntityModels = teamProjects.stream()
                .map(project -> EntityModel.of(project,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getProjectsByTeamID(teamID)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getTeamById(teamID)).withRel("team"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TeamsController.class).getTeamMembersByTeamId(project.getTeamID())).withRel("teamMembers")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(projectsEntityModels);
    }
}