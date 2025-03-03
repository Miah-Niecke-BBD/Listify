package org.setup.Listify.controller;

import org.setup.Listify.model.TeamMembers;
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
    public ResponseEntity<Object> getAllTeams() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<Teams> teams = teamService.getAllUserTeams(userID);

        return ResponseEntity.ok(teams);
    }

    @PostMapping
    public ResponseEntity<Object> addTeam(Authentication authentication, @RequestParam("teamName") String teamName) {
        Long userID = userService.getUserIDFromAuthentication(authentication);

        Long newTeamID = teamService.addTeam(userID, teamName);

        Teams createdTeam = teamService.findATeamByUserID(userID, newTeamID);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @GetMapping("/{teamID}")
    public ResponseEntity<Object> getTeamById(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);

        Teams team = teamService.findATeamByUserID(userID, teamID);
        return ResponseEntity.status(HttpStatus.OK).body(team);
    }

    @DeleteMapping("/{teamID}")
    public ResponseEntity<Object> deleteTeam(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);

        teamService.deleteTeam(teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body("Team " + teamID + " successfully deleted");
    }

    @PutMapping("/{teamID}")
    public ResponseEntity<Object> updateTeam(Authentication authentication, @PathVariable("teamID") Long teamID, @RequestParam("newTeamName") String newTeamName) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        Teams updatedTeam = teamService.updateTeamDetails(userID, teamID, newTeamName);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
    }

    @GetMapping("/{teamID}/members")
    public ResponseEntity<Object> getTeamMembersByTeamId(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<TeamMembers> teamMembers = teamService.getTeamMembersByTeamID(teamID, userID);

        return ResponseEntity.ok(teamMembers);
    }

    @PostMapping("/{teamID}/members")
    public ResponseEntity<Object> assignMemberToTeam(Authentication authentication, @RequestParam("githubID") String githubID, @PathVariable("teamID") Long teamID) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);

        teamService.assignMemberToTeam(teamLeaderID, githubID, teamID);
        List<TeamMembers> teamMembers = teamService.getTeamMembersByTeamID(teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
    }

    @DeleteMapping("/{teamID}/members")
    public ResponseEntity<Object> deleteMemberFromTeam(@RequestParam("githubID") String githubID, @PathVariable("teamID") Long teamID, Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);

        teamService.deleteMemberFromTeam(githubID, teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body("User " + githubID + " successfully remove from team " + teamID);
    }

    @PutMapping("/{teamID}/members")
    public ResponseEntity<Object> updateTeamLeader(Authentication authentication, @PathVariable("teamID") Long teamID, @RequestParam("newTeamLeaderGithubID")String newTeamLeaderGithubID) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);

        teamService.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderGithubID);

        return ResponseEntity.status(HttpStatus.OK).body("Team leader for team " + teamID + " successfully update to user " + newTeamLeaderGithubID);
    }

    @GetMapping("/{teamID}/projects")
    public ResponseEntity<Object> getProjectsByTeamIDAndUserID(@PathVariable("teamID") Long teamID, Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(teamService.getProjectsByTeamIDAndUserID(teamID, userID));
    }
}

