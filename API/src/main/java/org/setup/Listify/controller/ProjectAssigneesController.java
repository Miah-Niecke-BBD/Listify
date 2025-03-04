package org.setup.listify.controller;


import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.ProjectAssignees;
import org.setup.listify.service.ProjectAssigneesService;
import org.setup.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project/assignees")
public class ProjectAssigneesController {

    private final ProjectAssigneesService projectAssigneesService;
    private final UserService userService;

    public ProjectAssigneesController(ProjectAssigneesService projectAssigneesService, UserService userService) {
        this.projectAssigneesService = projectAssigneesService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProjectAssignees() {
        List<ProjectAssignees> assignees = projectAssigneesService.getAllProjectAssignees();
        return ResponseEntity.ok(assignees);
    }

    @GetMapping("/{assigneeID}")
    public ResponseEntity<Object> getProjectAssigneeById(@PathVariable("assigneeID") Long assigneeID) {
        ProjectAssignees assignee = projectAssigneesService.getProjectAssigneeById(assigneeID);
        return ResponseEntity.ok(assignee);
    }

    @GetMapping("/projects/{userID}")
    public ResponseEntity<Object> getProjectsAssignedToUser(@PathVariable("userID") Long userID) {
        List<ProjectAssignees> projects = projectAssigneesService.getProjectsAssignedToSpecificUser(userID);
        return ResponseEntity.ok(projects);
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<?> assignUserToProject(@RequestParam(name = "teamLeaderID") int teamLeaderID,
                                                 @RequestParam(name = "userID") int userID,
                                                 @RequestParam(name = "projectID") int projectID) {

        if (userID == 0 || projectID == 0) {
            ErrorResponse errorResponse = new ErrorResponse(" User ID and Project ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long newProjectAssigneeID = projectAssigneesService.assignUserToProject(teamLeaderID, userID, projectID);

        ProjectAssignees newProjectAssignee = projectAssigneesService.getProjectAssigneeById(newProjectAssigneeID);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProjectAssignee);

    }

    @DeleteMapping("/{userID}")
    @Transactional
    public ResponseEntity<?> deleteUserFromProject(@PathVariable("userID") Integer userID,
                                                   @RequestParam(name = "projectID") Integer projectID,
                                                   Authentication authentication) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        if (teamLeaderID == 0 || userID == null || projectID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID, Team Leader ID, and Project ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        projectAssigneesService.deleteUserFromProject(userID, projectID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "User " + userID + " has been removed from project " + projectID));
    }
}
