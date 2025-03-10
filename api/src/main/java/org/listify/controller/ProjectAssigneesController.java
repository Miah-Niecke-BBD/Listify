package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.ProjectAssigneeDTO;
import org.listify.service.ProjectAssigneesService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/{projectID}")
    public ResponseEntity<List<ProjectAssigneeDTO>> getProjectAssigneeById(@PathVariable("projectID") Long projectID, HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
       return ResponseEntity.status(HttpStatus.OK).body(projectAssigneesService.getAllProjectsAssignees(projectID));
    }


    @PostMapping
    @Transactional
    public ResponseEntity<ProjectAssigneeDTO> assignUserToProject(@RequestParam(name = "userID") Long userID,
                                                 @RequestParam(name = "projectID") Long projectID,
                                                                  HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        Long newProjectAssigneeID = projectAssigneesService.assignUserToProject(teamLeaderID, userID, projectID);
        ProjectAssigneeDTO newProjectAssignee = projectAssigneesService.getProjectAssigneeById(newProjectAssigneeID);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProjectAssignee);
    }

    @DeleteMapping("/{userID}")
    @Transactional
    public ResponseEntity<?> deleteUserFromProject(@PathVariable("userID") Long userID,
                                                   @RequestParam(name = "projectID") Long projectID,
                                                   HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        projectAssigneesService.deleteUserFromProject(userID, projectID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}