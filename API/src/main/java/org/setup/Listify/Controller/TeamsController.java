package org.setup.Listify.Controller;

import org.setup.Listify.Exception.ErrorResponse;
import org.setup.Listify.Exception.TeamMemberNotFoundException;
import org.setup.Listify.Exception.TeamNotFoundException;
import org.setup.Listify.Model.TeamMembers;
import org.setup.Listify.Model.TeamProjects;
import org.setup.Listify.Model.Teams;
import org.setup.Listify.Service.TeamsService;
import org.setup.Listify.Assembler.TeamsAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamsController {

    @Autowired
    private TeamsService teamService;

    @Autowired
    private TeamsAssembler teamsAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Teams>> getAllTeams() {
        List<EntityModel<Teams>> teams = teamService.getAllTeams().stream()
                .map(team -> teamsAssembler.toModel(team))
                .collect(Collectors.toList());

        return CollectionModel.of(teams);
    }

    @PostMapping
    public ResponseEntity<Object> addTeam(@RequestParam Long userID, @RequestParam String teamName) {
        if (teamName == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team name and User ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        try {
            Long newTeamID = teamService.addTeam(userID, teamName);
            Teams createdTeam = teamService.getTeamById(newTeamID)
                    .orElseThrow(() -> new TeamNotFoundException(newTeamID));

            EntityModel<Teams> entityModel = teamsAssembler.toModel(createdTeam);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(entityModel);

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error assigning member to team: " + e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }

    }

    @GetMapping("/{teamID}")
    public EntityModel<Teams> getTeamById(@PathVariable Long teamID) {
        Teams team = teamService.getTeamById(teamID)
                .orElseThrow(() -> new TeamNotFoundException(teamID));
        return teamsAssembler.toModel(team);
    }

    @DeleteMapping("/{teamID}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long teamID, @RequestParam Long teamLeaderID) {
        if (teamID == null || teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            teamService.getTeamById(teamID)
                    .orElseThrow(() -> new TeamNotFoundException(teamID));

            teamService.deleteTeam(teamID, teamLeaderID);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error deleting team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{teamID}")
    public ResponseEntity<Object> updateTeam(@PathVariable Long teamID, @RequestParam Long teamLeaderID, @RequestParam String newTeamName) {
        if (teamID == null || teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            teamService.updateTeamDetails(teamID, teamLeaderID, newTeamName);

            Teams updatedTeam = teamService.getTeamById(teamID)
                    .orElseThrow(() -> new TeamNotFoundException(teamID));

            EntityModel<Teams> entityModel = teamsAssembler.toModel(updatedTeam);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(entityModel);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error updating team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/members")

    public CollectionModel<EntityModel<TeamMembers>> getAllTeamMembers() {
        List<TeamMembers> teamMembers = teamService.getAllTeamMembers();
        List<EntityModel<TeamMembers>> teamMemberModels = teamMembers.stream()
                .map(teamMember -> teamsAssembler.toModel(teamMember))
                .collect(Collectors.toList());

        return CollectionModel.of(teamMemberModels);
    }

    @GetMapping("/{teamID}/members")
    public List<EntityModel<TeamMembers>> getTeamMembersByTeamId(@PathVariable Long teamID) {
        List<TeamMembers> teamMembersList = teamService.getTeamMembersByTeamId(teamID);

        if (teamMembersList.isEmpty()) {
            throw new TeamNotFoundException(teamID);
        }

        return teamMembersList.stream()
                .map(teamsAssembler::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{teamID}/projects")
    public CollectionModel<EntityModel<TeamProjects>> getProjectsByTeamID(@PathVariable Long teamID) {
        List<TeamProjects> teamProjectsList = teamService.getProjectsByTeamID(teamID);

        if (teamProjectsList.isEmpty()) {
            throw new TeamNotFoundException(teamID);
        }

        return teamsAssembler.toModel(teamProjectsList, teamID);
    }

    @PostMapping("/{teamID}/assignMember")
    public ResponseEntity<Object> assignMemberToTeam(@PathVariable Long teamID, @RequestParam Long teamLeaderID, @RequestParam Long userID) {

        if (teamID == null || teamLeaderID == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID, Team Leader ID, and User ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            teamService.getTeamById(teamID)
                    .orElseThrow(() -> new TeamNotFoundException(teamID));

            TeamMembers assignedMember = teamService.getByTeamMembersId(userID)
                    .orElseThrow(() -> new TeamMemberNotFoundException(userID));

            teamService.assignMemberToTeam(teamLeaderID, userID, teamID);

            EntityModel<TeamMembers> teamMemberModel = teamsAssembler.toModel(assignedMember);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(teamMemberModel);

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error assigning member to team: " + e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @DeleteMapping("/{teamID}/teamMember")
        public ResponseEntity<Object> deleteMemberFromTeam(@RequestParam Long userID, @PathVariable Long teamID, @RequestParam Long teamLeaderID) {

        if (userID == null || teamID == null || teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID, Team ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            teamService.getTeamById(teamID)
                    .orElseThrow(() -> new TeamNotFoundException(teamID));

           teamService.getByTeamMembersId(userID)
                    .orElseThrow(() -> new TeamMemberNotFoundException(userID));

           teamService.deleteMemberFromTeam(userID, teamID, teamLeaderID);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error deleting member from team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{teamID}/leader")
    public ResponseEntity<Object> updateTeamLeader(@RequestParam("teamLeaderID") Long teamLeaderID, @PathVariable("teamID") Long teamID, @RequestParam("newTeamLeaderID") Long newTeamLeaderID) {
        if ( teamID == null || teamLeaderID == null || newTeamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID, Team Leader ID and new Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            teamService.getTeamById(teamID)
                    .orElseThrow(() -> new TeamNotFoundException(teamID));

            teamService.getByTeamMembersId(newTeamLeaderID)
                    .orElseThrow(() -> new TeamMemberNotFoundException(newTeamLeaderID));

           teamService.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderID);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error updating team leader from team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

