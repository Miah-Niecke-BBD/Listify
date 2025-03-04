package org.setup.listify.controller;

import org.setup.listify.dto.ProjectOverviewDTO;
import org.setup.listify.model.Projects;
import org.setup.listify.service.ProjectsService;
import org.setup.listify.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectsService projectsService;
    private final UserService userService;

    public ProjectsController(ProjectsService projectsService, UserService userService) {
        this.projectsService = projectsService;
        this.userService = userService;
    }

    @GetMapping("/{projectID}")
    public ResponseEntity<Object> getProjectsById(@PathVariable("projectID") Long projectID,Authentication authentication) {
        Long userID= userService.getUserIDFromAuthentication(authentication);
        Projects project = projectsService.getProjectById(projectID , userID);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<Object> newProject(Authentication authentication,
                                        @RequestParam(name = "teamID") Long teamID,
                                        @RequestParam(name = "projectName") String projectName,
                                        @RequestParam(name = "projectDescription") String projectDescription) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        Long newProjectID = projectsService.createProject(teamLeaderIDLong, teamID, projectName, projectDescription);
        Projects project = projectsService.getProjectById(newProjectID ,teamLeaderIDLong);
        return ResponseEntity.ok(project);
    }


    @PostMapping("/hello")
    public ResponseEntity<Object> testing() {

        return ResponseEntity.ok("hello world");
    }

    @PutMapping("/{projectID}")
    @Transactional
    public ResponseEntity<Object> updateProject(@PathVariable("projectID") Long projectID,
                                          Authentication authentication,
                                           @RequestParam(name = "projectName",required = false) String projectName,
                                           @RequestParam(name = "projectDescription",required = false) String projectDescription) {

        Long userID= userService.getUserIDFromAuthentication(authentication);
        projectsService.updateProject(projectID, userID, projectName, projectDescription);
        Projects updatedProject = projectsService.getProjectById(projectID,userID);

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectID}")
    @Transactional
    public ResponseEntity<Object> deleteProjectById(@PathVariable("projectID") Long projectID,
                                               Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);

        projectsService.deleteProjectById(projectID, teamLeaderID);
        return ResponseEntity.ok("Project with id: "+ projectID +" has been successfully deleted");
    }


    @GetMapping("/{projectID}/details")
    public ResponseEntity<Object> getProjectDetails(@PathVariable("projectID") Integer projectID,
                                                                        Authentication authentication) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        ProjectOverviewDTO projectOverview = projectsService.getProjectDetails(Math.toIntExact(userID), projectID);

        return ResponseEntity.ok(projectOverview);
    }

}
