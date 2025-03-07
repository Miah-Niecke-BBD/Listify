package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.DueTasksDTO;
import org.listify.dto.ProjectSectionsDTO;
import org.listify.dto.ProjectsInfoDTO;
import org.listify.model.Projects;
import org.listify.service.ProjectsService;
import org.listify.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<ProjectsInfoDTO> getProjectsById(@PathVariable("projectID") Long projectID, HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);
        Projects project = projectsService.getProjectById(projectID, userID);

        return ResponseEntity.ok(new ProjectsInfoDTO(project.getProjectName(), project.getProjectDescription(), project.getCreatedAt(), project.getUpdatedAt()));
    }

    @PostMapping
    public ResponseEntity<Projects> newProject(HttpServletRequest request,
                                               @RequestParam(name = "teamID") Long teamID,
                                               @RequestParam(name = "projectName") String projectName,
                                               @RequestParam(name = "projectDescription", required = false) String projectDescription) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(request);
        Long newProjectID = projectsService.createProject(teamLeaderIDLong, teamID, projectName, projectDescription);
        Projects project = projectsService.getProjectById(newProjectID, teamLeaderIDLong);
        return ResponseEntity.ok(project);
    }
    @PutMapping("/{projectID}")
    @Transactional
    public ResponseEntity<ProjectsInfoDTO> updateProject(@PathVariable("projectID") Long projectID,
                                                         HttpServletRequest request,
                                                         @RequestParam(name = "projectName", required = false) String projectName,
                                                         @RequestParam(name = "projectDescription", required = false) String projectDescription) {
        Long userID = userService.getUserIDFromAuthentication(request);
        projectsService.updateProject(projectID, userID, projectName, projectDescription);
        Projects updatedProject = projectsService.getProjectById(projectID, userID);

        return ResponseEntity.ok(new ProjectsInfoDTO(updatedProject.getProjectName(), updatedProject.getProjectDescription(), updatedProject.getCreatedAt(), updatedProject.getUpdatedAt()));
    }

    @DeleteMapping("/{projectID}")
    @Transactional
    public ResponseEntity<?> deleteProjectById(@PathVariable("projectID") Long projectID,
                                               HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);

        projectsService.deleteProjectById(projectID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectID}/sections")
    public ResponseEntity<List<ProjectSectionsDTO>> getProjectSections(@PathVariable("projectID") Long projectID, HttpServletRequest request) {

        Long userID = userService.getUserIDFromAuthentication(request);

        List<ProjectSectionsDTO> projectSectionsDTO = projectsService.getAllSectionsByProjectID(userID, projectID);

        return ResponseEntity.ok(projectSectionsDTO);
    }

    @GetMapping("/{projectID}/dueTasks")
    public ResponseEntity<List<DueTasksDTO>> getAllProjectDueTasks(@PathVariable("projectID") Long projectID, HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);

        List<DueTasksDTO> dueTasksDTOS = projectsService.getAllProjectDueTasks(userID, projectID);
        return ResponseEntity.ok(dueTasksDTOS);
    }

}
