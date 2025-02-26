package org.setup.Listify.Controller;

import org.setup.Listify.Assembler.SectionsModelAssembler;
import org.setup.Listify.Exception.ErrorResponse;
import org.setup.Listify.Model.Sections;
import org.setup.Listify.Service.SectionsService;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/sections")
public class SectionsController {

    private final SectionsService sectionsService;
    private final SectionsModelAssembler assembler;

    public SectionsController(SectionsService sectionsService, SectionsModelAssembler assembler) {
        this.sectionsService = sectionsService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Sections>> getAllSections() {
        List<Sections> projects = sectionsService.getAllSections();
        return assembler.toCollectionModel(projects);
    }

    @GetMapping("/{id}")
    public EntityModel<Sections> getSectionsById(Long id) {
        Sections section = sectionsService.getSectionById(id);
        return assembler.toModel(section);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newSection(@RequestParam(required = false) Integer teamLeaderID,
                                        @RequestParam(required = false) Integer projectID,
                                        @RequestParam(required = false) String sectionName,
                                        @RequestParam(required = false) Byte sectionPosition) {
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
    public ResponseEntity<?> updateSection(@PathVariable Long id,
                                           @RequestParam Integer userID,
                                           @RequestParam String newSectionName) {
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
    public ResponseEntity<?> deleteSectionById(@PathVariable Long id,
                                               @RequestParam(required = false) Integer teamLeaderID) {
        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        sectionsService.deleteSectionById(teamLeaderID, id.intValue());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Section with id: "+ id +" has been successfully deleted"));
    }
}
