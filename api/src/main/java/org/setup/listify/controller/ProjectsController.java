package org.setup.listify.controller;

import org.setup.listify.assembler.ProjectsModelAssembler;
import org.setup.listify.assembler.SectionsModelAssembler;
import org.setup.listify.dto.ProjectOverviewDTO;
import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.Projects;
import org.setup.listify.model.Sections;
import org.setup.listify.service.ProjectsService;
import org.setup.listify.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectsService projectsService;
    private final UserService userService;
    private final ProjectsModelAssembler assembler;
    private final SectionsModelAssembler sectionsAssembler;

    public ProjectsController(ProjectsService projectsService, ProjectsModelAssembler assembler, SectionsModelAssembler sectionsAssembler, UserService userService) {
        this.projectsService = projectsService;
        this.userService = userService;
        this.assembler = assembler;
        this.sectionsAssembler = sectionsAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Projects>> getAllProjects() {
        List<Projects> projects = projectsService.getAllProjects();
        return assembler.toCollectionModel(projects);
    }

    @GetMapping("/{id}")
    public EntityModel<Projects> getProjectsById(@PathVariable("id") Long id) {
        Projects project = projectsService.getProjectById(id);
        return assembler.toModel(project);
    }

    @GetMapping("/{id}/sections")
    public CollectionModel<EntityModel<Sections>> getAllSectionsInProject(
            @PathVariable("id") Long id) {
        List<Sections> sections = projectsService.getAllSectionsInProject(id);
        return sectionsAssembler.toCollectionModel(sections);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newProject(Authentication authentication,
                                        @RequestParam(name = "teamID", required = false) Integer teamID,
                                        @RequestParam(name = "projectName", required = false) String projectName,
                                        @RequestParam(name = "projectDescription", required = false) String projectDescription) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null || teamID == null || projectName == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newProjectID = projectsService.createProject(teamLeaderID, teamID, projectName, projectDescription);

        Projects project = projectsService.getProjectById(newProjectID);
        EntityModel<Projects> entityModel = assembler.toModel(project);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateProject(@PathVariable("id") Long id,
                                          Authentication authentication,
                                           @RequestParam(name = "projectName") String projectName,
                                           @RequestParam(name = "projectDescription") String projectDescription) {
        Long userIDLong = userService.getUserIDFromAuthentication(authentication);
        int userIDInt = userIDLong.intValue();
        Integer userID = Integer.valueOf(userIDInt);

        if (userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        projectsService.updateProject(id, userID, projectName, projectDescription);

        Projects updatedProject = projectsService.getProjectById(id);
        EntityModel<Projects> entityModel = assembler.toModel(updatedProject);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProjectById(@PathVariable("id") Long id,
                                               Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        projectsService.deleteProjectById(id.intValue(), teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Project with id: "+ id +" has been successfully deleted"));
    }


    @GetMapping("/{projectID}/details")
    public ResponseEntity<ProjectOverviewDTO> getCompleteProjectDetails(@PathVariable("projectID") Long projectID,
                                                                        Authentication authentication) {
        // Extracting the userID from the authentication to pass to the service method
        Long userID = userService.getUserIDFromAuthentication(authentication);

        // Fetching the project details using the service method
        ProjectOverviewDTO projectOverview = projectsService.getCompleteProjectDetails(userID, projectID);

        if (projectOverview == null) {
            ProjectOverviewDTO errorResponse = null;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Return the project overview DTO as the response
        return ResponseEntity.ok(projectOverview);
    }

}
