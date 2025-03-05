package org.listify.controller;

import org.listify.dto.ProjectAssigneeDTO;
import org.listify.service.ProjectAssigneesService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<ProjectAssigneeDTO> assignees = projectAssigneesService.getAllProjectAssignees(userID);
        return ResponseEntity.ok(assignees);
    }

    @GetMapping("/{assigneeID}")
    public ResponseEntity<Object> getProjectAssigneeById(@PathVariable("assigneeID") Long assigneeID) {
        ProjectAssigneeDTO assignee = projectAssigneesService.getProjectAssigneeById(assigneeID);
        return ResponseEntity.ok(assignee);
    }

    @GetMapping("/projects/{userID}")
    public ResponseEntity<Object> getProjectsAssignedToUser(@PathVariable("userID") Long userID) {
        List<ProjectAssigneeDTO> projects = projectAssigneesService.getProjectsAssignedToSpecificUser(userID);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> assignUserToProject(@RequestParam(name = "userID") Long userID,
                                                 @RequestParam(name = "projectID") Long projectID,
                                                 Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        Long newProjectAssigneeID = projectAssigneesService.assignUserToProject(teamLeaderID, userID, projectID);
        ProjectAssigneeDTO newProjectAssignee = projectAssigneesService.getProjectAssigneeById(newProjectAssigneeID);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProjectAssignee);
    }

    @DeleteMapping("/{userID}")
    @Transactional
    public ResponseEntity<?> deleteUserFromProject(@PathVariable("userID") Long userID,
                                                   @RequestParam(name = "projectID") Long projectID,
                                                   Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        projectAssigneesService.deleteUserFromProject(userID, projectID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}