package org.setup.listify.controller;

import org.setup.listify.assembler.TeamMembersAssembler;
import org.setup.listify.assembler.TeamProjectsAssembler;
import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.TeamMembers;
import org.setup.listify.model.UserTeamProjects;
import org.setup.listify.model.Teams;
import org.setup.listify.service.TeamsService;
import org.setup.listify.assembler.TeamsAssembler;
import org.setup.listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamsController {

    @Autowired
    private TeamsService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamsAssembler teamsAssembler;

    @Autowired
    private TeamMembersAssembler teamMembersAssembler;

    @Autowired
    private TeamProjectsAssembler teamProjectsAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Teams>> getAllTeams() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<EntityModel<Teams>> teams = teamService.getAllUserTeams(userID).stream().map(teamsAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(teams);
    }

    @PostMapping
    public ResponseEntity<Object> addTeam(Authentication authentication, @RequestParam("teamName") String teamName) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        if (teamName == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team name and User ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long newTeamID = teamService.addTeam(userID, teamName);
        Teams createdTeam = teamService.getTeamById(newTeamID);
        EntityModel<Teams> entityModel = teamsAssembler.toModel(createdTeam);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @GetMapping("/{teamID}")
    public EntityModel<Teams> getTeamById(@PathVariable("teamID") Long teamID) {
        Teams team = teamService.getTeamById(teamID);
        return teamsAssembler.toModel(team);
    }

    @DeleteMapping("/{teamID}")
    public ResponseEntity<Object> deleteTeam(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        if (teamID == null || teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        teamService.getTeamById(teamID);
        teamService.deleteTeam(teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body("Team " + teamID + " successfully deleted");
    }

    @PutMapping("/{teamID}")
    public ResponseEntity<Object> updateTeam(Authentication authentication, @PathVariable("teamID") Long teamID, @RequestParam("newTeamName") String newTeamName) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        if (teamID == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        teamService.updateTeamDetails(userID, teamID, newTeamName);

        Teams updatedTeam = teamService.getTeamById(teamID);

        EntityModel<Teams> entityModel = teamsAssembler.toModel(updatedTeam);

        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    //To be deleted
    @GetMapping("/members")
    public CollectionModel<EntityModel<TeamMembers>> getAllTeamMembers() {
        List<TeamMembers> teamMembers = teamService.getAllTeamMembers();
        List<EntityModel<TeamMembers>> teamMemberModels = teamMembers.stream().map(teamMembersAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(teamMemberModels);
    }

    @GetMapping("/{teamID}/members")
    public List<EntityModel<TeamMembers>> getTeamMembersByTeamId(@PathVariable("teamID") Long teamID) {
        List<TeamMembers> teamMembersList = teamService.getTeamMembersByTeamId(teamID);

        return teamMembersList.stream().map(teamMembersAssembler::toModel).collect(Collectors.toList());
    }

    @GetMapping("/{teamID}/projects")
    public CollectionModel<EntityModel<UserTeamProjects>> getProjectsByTeamIDAndUserID(@PathVariable("teamID") Long teamID, Authentication authentication) {

        Long userID = userService.getUserIDFromAuthentication(authentication);
        System.out.println(userID);
        List<UserTeamProjects> userTeamProjectsList = teamService.getProjectsByTeamIDAndUserID(teamID, userID);

        return teamProjectsAssembler.toModel(userTeamProjectsList, teamID);
    }

    @PostMapping("/{teamID}/assignMember")
    public ResponseEntity<Object> assignMemberToTeam(@PathVariable("teamID") Long teamID, Authentication authentication, @RequestParam("userID") Long userID) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        if (teamID == null || teamLeaderID == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID, Team Leader ID, and User ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        teamService.getTeamById(teamID);
        List<TeamMembers> teamMembers = teamService.getTeamMembersByTeamId(teamID);

        teamService.assignMemberToTeam(teamLeaderID, userID, teamID);

        List<EntityModel<TeamMembers>> teamMemberModels = teamMembers.stream().map(teamMembersAssembler::toModel).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(teamMemberModels);
    }

    @DeleteMapping("/{teamID}/teamMember")
    public ResponseEntity<Object> deleteMemberFromTeam(@RequestParam("userID") Long userID, @PathVariable("teamID") Long teamID, Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);

        if (userID == null || teamID == null || teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID, Team ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        teamService.getTeamById(teamID);
        teamService.getByTeamMembersId(userID);
        teamService.deleteMemberFromTeam(userID, teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body("User " + userID + " successfully remove from team " + teamID);
    }

    @PutMapping("/{teamID}/leader")
    public ResponseEntity<Object> updateTeamLeader(Authentication authentication, @PathVariable("teamID") Long teamID, @RequestParam("newTeamLeaderID") Long newTeamLeaderID) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        if (teamID == null || teamLeaderID == null || newTeamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID, Team Leader ID and new Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        teamService.getTeamById(teamID);
        teamService.getByTeamMembersId(newTeamLeaderID);
        teamService.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body("Team leader for team " + teamID + " successfully update to user " + newTeamLeaderID);
    }
}

