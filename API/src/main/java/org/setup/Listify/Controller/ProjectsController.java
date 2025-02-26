package org.setup.Listify.Controller;

import org.setup.Listify.Assembler.ProjectsModelAssembler;
import org.setup.Listify.Assembler.SectionsModelAssembler;
import org.setup.Listify.Exception.ErrorResponse;
import org.setup.Listify.Model.Projects;
import org.setup.Listify.Model.Sections;
import org.setup.Listify.Service.ProjectsService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectsService projectsService;
    private final ProjectsModelAssembler assembler;
    private final SectionsModelAssembler sectionsAssembler;

    public ProjectsController(ProjectsService projectsService, ProjectsModelAssembler assembler, SectionsModelAssembler sectionsAssembler) {
        this.projectsService = projectsService;
        this.assembler = assembler;
        this.sectionsAssembler = sectionsAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Projects>> getAllProjects() {
        List<Projects> projects = projectsService.getAllProjects();
        return assembler.toCollectionModel(projects);
    }

    @GetMapping("/{id}")
    public EntityModel<Projects> getProjectsById(@PathVariable Long id) {
        Projects project = projectsService.getProjectById(id);
        return assembler.toModel(project);
    }

    @GetMapping("/{id}/sections")
    public CollectionModel<EntityModel<Sections>> getAllSectionsInProject(
            @PathVariable Long id) {
        List<Sections> sections = projectsService.getAllSectionsInProject(id);
        return sectionsAssembler.toCollectionModel(sections);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newProject(@RequestParam(required = false) Integer teamLeaderID,
                                        @RequestParam(required = false) Integer teamID,
                                        @RequestParam(required = false) String projectName,
                                        @RequestParam(required = false) String projectDescription) {
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
    public ResponseEntity<?> updateProject(@PathVariable Long id,
                                           @RequestParam Integer userID,
                                           @RequestParam String projectName,
                                           @RequestParam String projectDescription) {
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
    public ResponseEntity<?> deleteProjectById(@PathVariable Long id,
                                               @RequestParam(required = false) Integer teamLeaderID) {
        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        projectsService.deleteProjectById(id.intValue(), teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Project with id: "+ id +" has been successfully deleted"));
    }

}
