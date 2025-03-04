package org.setup.listify.controller;

import org.setup.listify.assembler.SectionsModelAssembler;
import org.setup.listify.assembler.TasksModelAssembler;
import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.Sections;
import org.setup.listify.model.Tasks;
import org.setup.listify.service.SectionsService;
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
@RequestMapping("/sections")
public class SectionsController {

    private final SectionsService sectionsService;
    private final UserService userService;
    private final SectionsModelAssembler assembler;
    private final TasksModelAssembler tasksAssembler;

    public SectionsController(SectionsService sectionsService, SectionsModelAssembler assembler, TasksModelAssembler tasksAssembler, UserService userService) {
        this.sectionsService = sectionsService;
        this.userService = userService;
        this.assembler = assembler;
        this.tasksAssembler = tasksAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Sections>> getAllSections() {
        List<Sections> projects = sectionsService.getAllSections();
        return assembler.toCollectionModel(projects);
    }

    @GetMapping("/{id}")
    public EntityModel<Sections> getSectionsById(@PathVariable("id") Long id) {
        Sections section = sectionsService.getSectionById(id);
        return assembler.toModel(section);
    }

    @GetMapping("{id}/tasks")
    public CollectionModel<EntityModel<Tasks>> getTaskBySectionId(@PathVariable("id") Long id) {
        List<Tasks> tasksList = sectionsService.getTaskBySectionId(id);
        return tasksAssembler.toCollectionModel(tasksList);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newSection(Authentication authentication,
                                        @RequestParam(name = "projectID", required = false) Integer projectID,
                                        @RequestParam(name = "sectionName", required = false) String sectionName,
                                        @RequestParam(name = "sectionPosition", required = false) Byte sectionPosition) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null || projectID == null || sectionPosition == null || sectionName == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newSectionID = sectionsService.createSection(teamLeaderID, projectID, sectionName, sectionPosition);
        Sections section = sectionsService.getSectionById(newSectionID);
        EntityModel<Sections> entityModel = assembler.toModel(section);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateSection(@PathVariable("id") Long id,
                                           Authentication authentication,
                                           @RequestParam("newSectionName") String newSectionName) {

        Long userIDLong = userService.getUserIDFromAuthentication(authentication);
        int userIDInt = userIDLong.intValue();
        Integer userID = Integer.valueOf(userIDInt);

        if (userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        sectionsService.updateSection(id, userID, newSectionName);

        Sections updatedSection = sectionsService.getSectionById(id);
        EntityModel<Sections> entityModel = assembler.toModel(updatedSection);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteSectionById(@PathVariable("id") Long id,
                                               Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        sectionsService.deleteSectionById(teamLeaderID, id.intValue());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Section with id: "+ id +" has been successfully deleted"));
    }
}
