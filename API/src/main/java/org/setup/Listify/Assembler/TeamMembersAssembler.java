package org.setup.Listify.Assembler;

import org.setup.Listify.Model.TeamMembers;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.setup.Listify.Controller.TeamsController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamMembersAssembler implements RepresentationModelAssembler<TeamMembers, EntityModel<TeamMembers>> {

    @Override
    public EntityModel<TeamMembers> toModel(TeamMembers teamMembers) {
        EntityModel<TeamMembers> teamMembersEntityModel = EntityModel.of(teamMembers);

        teamMembersEntityModel.add(
                linkTo(methodOn(TeamsController.class).getTeamMembersByTeamId(teamMembers.getTeamID())).withSelfRel(),
                linkTo(methodOn(TeamsController.class).getAllTeamMembers()).withRel("allMembers"),
                linkTo(methodOn(TeamsController.class).getTeamById(teamMembers.getTeamID())).withRel("team")
        );

        return teamMembersEntityModel;
    }
}

