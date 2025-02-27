package org.setup.Listify.controller;


import org.setup.Listify.assembler.ProjectAssigneesAssembler;
import org.setup.Listify.exception.ErrorResponse;
import org.setup.Listify.model.ProjectAssignees;
import org.setup.Listify.service.ProjectAssigneesService;
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
@RequestMapping("/projectAssignees")
public class ProjectAssigneesController {

    private final ProjectAssigneesService projectAssigneesService;
    private final ProjectAssigneesAssembler assembler;

    public ProjectAssigneesController(ProjectAssigneesService projectAssigneesService, ProjectAssigneesAssembler assembler) {
        this.projectAssigneesService = projectAssigneesService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<ProjectAssignees>> getAllProjectAssignees() {
        List<ProjectAssignees> assignees = projectAssigneesService.getAllProjectAssignees();
        return assembler.toCollectionModel(assignees);
    }

    @GetMapping("/{id}")
    public EntityModel<ProjectAssignees> getProjectAssigneeById(@PathVariable Long id) {
        ProjectAssignees assignee = projectAssigneesService.getProjectAssigneeById(id);
        return assembler.toModel(assignee);
    }

    @GetMapping("/user/{userID}")
    public CollectionModel<EntityModel<ProjectAssignees>> getProjectsAssignedToUser(@PathVariable Long userID) {
        List<ProjectAssignees> projects = projectAssigneesService.getProjectsAssignedToSpecificUser(userID);
        return assembler.toCollectionModel(projects);
    }


    @PostMapping("/assign")
    @Transactional
    public ResponseEntity<?> assignUserToProject(@RequestParam int userID, @RequestParam int projectID) {
        Long newProjectAssigneeID = projectAssigneesService.assignUserToProject(userID, projectID);

        if (newProjectAssigneeID != null) {
            ProjectAssignees newProjectAssignee = projectAssigneesService.getProjectAssigneeById(newProjectAssigneeID);
            EntityModel<ProjectAssignees> entityModel = assembler.toModel(newProjectAssignee);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Failed to assign user to project.", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/unassign")
    @Transactional
    public ResponseEntity<?> deleteUserFromProject(@RequestParam int userID, @RequestParam int projectID, @RequestParam int teamLeaderID) {
        projectAssigneesService.deleteUserFromProject(userID, projectID, teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "User " + userID + " has been removed from project " + projectID));
    }
}
