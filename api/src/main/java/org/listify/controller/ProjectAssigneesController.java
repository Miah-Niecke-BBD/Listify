package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.ProjectAssigneeDTO;
import org.listify.service.ProjectAssigneesService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/project/assignees")
public class ProjectAssigneesController {

    private final ProjectAssigneesService projectAssigneesService;
    private final UserService userService;

    public ProjectAssigneesController(ProjectAssigneesService projectAssigneesService, UserService userService) {
        this.projectAssigneesService = projectAssigneesService;
        this.userService = userService;
    }

    @GetMapping("/{projectID}")
    public ResponseEntity<List<ProjectAssigneeDTO>> getProjectsAssignedUsers(@PathVariable("projectID") Long projectID,
                                                                             HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        List<ProjectAssigneeDTO> assignedUsers = projectAssigneesService.getAllProjectsAssignees(projectID);
        return ResponseEntity.ok(assignedUsers);
    }

    @PostMapping("/{projectID}")
    @Transactional
    public ResponseEntity<ProjectAssigneeDTO> assignUserToProject(@PathVariable("projectID") Long projectID,
                                                                  @RequestParam(name = "githubID")String githubID,
                                                                  HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        projectAssigneesService.assignUserToProject(teamLeaderID, githubID, projectID);
        ProjectAssigneeDTO newProjectAssignee = new ProjectAssigneeDTO( githubID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProjectAssignee);
    }

    @DeleteMapping("/{projectID}")
    @Transactional
    public ResponseEntity<?> deleteUserFromProject(@PathVariable("projectID") Long projectID,
                                                   @RequestParam(name = "githubID") String githubID,
                                                   HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        projectAssigneesService.deleteUserFromProject(githubID, projectID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
