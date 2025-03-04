package org.setup.listify.assembler;

import org.setup.listify.controller.TeamsController;
import org.setup.listify.model.Teams;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamsAssembler implements RepresentationModelAssembler<Teams, EntityModel<Teams>> {

    @Override
    public EntityModel<Teams> toModel(Teams team) {
        EntityModel<Teams> teamEntityModel = EntityModel.of(team);

        teamEntityModel.add(
                linkTo(methodOn(TeamsController.class).getTeamById(team.getTeamID())).withSelfRel(),
                linkTo(methodOn(TeamsController.class).getAllTeams()).withRel("allTeams"),
                linkTo(methodOn(TeamsController.class).getTeamMembersByTeamId(team.getTeamID())).withRel("membersInTeam"),
                linkTo(methodOn(TeamsController.class).getProjectsByTeamIDAndUserID(team.getTeamID(), null)).withRel("projects")
        );

        return teamEntityModel;
    }
}
