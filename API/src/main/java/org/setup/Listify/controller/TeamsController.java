package org.setup.Listify.controller;

import org.setup.Listify.exception.ErrorResponse;
import org.setup.Listify.model.TeamMembers;
import org.setup.Listify.dto.UserTeamProjects;
import org.setup.Listify.model.Teams;
import org.setup.Listify.service.TeamsService;
import org.setup.Listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamsController {

    @Autowired
    private TeamsService teamService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Teams> getAllTeams() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        return teamService.getAllUserTeams(userID);
    }

    @PostMapping
    public ResponseEntity<Object> addTeam(Authentication authentication, @RequestParam("teamName") String teamName) {
        Long userID = userService.getUserIDFromAuthentication(authentication);

        if (teamName == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team name and user ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long newTeamID = teamService.addTeam(userID, teamName);
        Teams createdTeam = teamService.getTeamById(newTeamID);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @GetMapping("/{teamID}")
    public Teams getTeamById(@PathVariable("teamID") Long teamID) {
        return teamService.getTeamById(teamID);
    }

    @DeleteMapping("/{teamID}")
    public ResponseEntity<Object> deleteTeam(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        if (teamID == null || teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID and Team Leader ID are required to delete a team", HttpStatus.BAD_REQUEST.value());
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

        return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
    }

    //To be deleted
    @GetMapping("/members")
    public List<TeamMembers> getAllTeamMembers() {
       return teamService.getAllTeamMembers();
    }

    @GetMapping("/{teamID}/members")
    public List<TeamMembers> getTeamMembersByTeamId(@PathVariable("teamID") Long teamID) {
        return teamService.getTeamMembersByTeamId(teamID);
    }

    @GetMapping("/{teamID}/projects")
    public List<UserTeamProjects> getProjectsByTeamIDAndUserID(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
       return  teamService.getProjectsByTeamIDAndUserID(teamID, userID);
    }

    @PostMapping("/{teamID}/assignMember")
    public ResponseEntity<Object> assignMemberToTeam(@PathVariable("teamID") Long teamID, Authentication authentication, @RequestParam("userID") Long userID) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        if (teamID == null || teamLeaderID == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team ID, Team Leader ID, and User ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        teamService.getTeamById(teamID);
        teamService.assignMemberToTeam(teamLeaderID, userID, teamID);
        List<TeamMembers> teamMembers = teamService.getTeamMembersByTeamId(teamID);

        return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
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

