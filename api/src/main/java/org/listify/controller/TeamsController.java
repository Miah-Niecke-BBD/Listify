package org.listify.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.DueTasksDTO;
import org.listify.dto.TeamInfoDTO;
import org.listify.dto.TeamMemberInfoDTO;
import org.listify.dto.TeamProjectsDTO;
import org.listify.model.Teams;
import org.listify.service.TeamsService;
import org.listify.service.UserService;
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
    public ResponseEntity<List<Teams>> getAllTeams(HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);
        List<Teams> teams = teamService.getAllUserTeams(userID);

        return ResponseEntity.ok(teams);
    }

    @PostMapping
    public ResponseEntity<Teams> addTeam(@RequestParam("teamName") String teamName,HttpServletRequest request) {

        Long userID = userService.getUserIDFromAuthentication(request);
        Long newTeamID = teamService.addTeam(userID, teamName);
        Teams createdTeam = teamService.findATeamByUserID(userID, newTeamID);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @GetMapping("/{teamID}")
    public ResponseEntity<TeamInfoDTO> getTeamById(@PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long userID = userService.getUserIDFromAuthentication(request);
        Teams team = teamService.findATeamByUserID(userID, teamID);

        return ResponseEntity.status(HttpStatus.OK).body(new TeamInfoDTO(team.getTeamName(), team.getCreatedAt(), team.getUpdatedAt()));
    }

    @DeleteMapping("/{teamID}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        teamService.deleteTeam(teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{teamID}")
    public ResponseEntity<TeamInfoDTO> updateTeam(@PathVariable("teamID") Long teamID, @RequestParam("newTeamName") String newTeamName,HttpServletRequest request) {

        Long userID = userService.getUserIDFromAuthentication(request);
        TeamInfoDTO updatedTeam = teamService.updateTeamDetails(userID, teamID, newTeamName);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
    }

    @GetMapping("/{teamID}/members")
    public ResponseEntity<List<TeamMemberInfoDTO>> getTeamMembersByTeamId(@PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long userID = userService.getUserIDFromAuthentication(request);
        List<TeamMemberInfoDTO> teamMembers = teamService.getTeamMembersByTeamID(teamID, userID);

        return ResponseEntity.ok(teamMembers);
    }

    @PostMapping("/{teamID}/members")
    public ResponseEntity<List<TeamMemberInfoDTO>> assignMemberToTeam( @RequestParam("githubID") String githubID, @PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        teamService.assignMemberToTeam(teamLeaderID, githubID, teamID);
        List<TeamMemberInfoDTO> teamMembers = teamService.getTeamMembersByTeamID(teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
    }

    @DeleteMapping("/{teamID}/members")
    public ResponseEntity<Void> deleteMemberFromTeam(@RequestParam("githubID") String githubID, @PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        teamService.deleteMemberFromTeam(githubID, teamID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{teamID}/members")
    public ResponseEntity<JSONObject> updateTeamLeader(@PathVariable("teamID") Long teamID, @RequestParam("newTeamLeaderGithubID") String newTeamLeaderGithubID,HttpServletRequest request) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);

        teamService.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderGithubID);
        JSONObject response = new JSONObject();
        response.put("message", "Team leader for team " + teamID + " successfully updated to user " + newTeamLeaderGithubID);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{teamID}/projects")
    public ResponseEntity<List<TeamProjectsDTO>> findProjectsByTeamID(@PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long loggedInUser = userService.getUserIDFromAuthentication(request);
        List<TeamProjectsDTO> projects = teamService.findProjectsByTeamID(loggedInUser, teamID);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{teamID}/dueTasks")
    public ResponseEntity<List<DueTasksDTO>> findTeamsDueTasks(@PathVariable("teamID") Long teamID,HttpServletRequest request) {

        Long loggedInUser = userService.getUserIDFromAuthentication(request);
        List<DueTasksDTO> tasks = teamService.findTeamsDueTasks(loggedInUser, teamID);
        return ResponseEntity.ok(tasks);
    }
}

