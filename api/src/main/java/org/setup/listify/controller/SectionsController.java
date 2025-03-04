package org.setup.listify.controller;

import org.setup.listify.model.Sections;
import org.setup.listify.model.Tasks;
import org.setup.listify.service.SectionsService;
import org.setup.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionsController {

    private final SectionsService sectionsService;
    private final UserService userService;

    public SectionsController(SectionsService sectionsService, UserService userService) {
        this.sectionsService = sectionsService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSections() {
        List<Sections> sections = sectionsService.getAllSections();
        if (sections.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no sections in this project");
        }
        return ResponseEntity.ok(sections);
    }

    @GetMapping("{sectionID}/tasks")
    public ResponseEntity<Object> getTaskBySectionId(@PathVariable("sectionID") Long sectionID) {
        List<Tasks> tasksList = sectionsService.getTaskBySectionId(sectionID);
        if (tasksList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no tasks in section: "+sectionID);
        }
        return ResponseEntity.ok(tasksList);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> newSection(Authentication authentication,
                                        @RequestParam(name = "projectID") Long projectID,
                                        @RequestParam(name = "sectionName") String sectionName,
                                        @RequestParam(name = "sectionPosition") Byte sectionPosition) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        Long newSectionID = sectionsService.createSection(teamLeaderID, projectID, sectionName, sectionPosition);
        Sections section = sectionsService.getSectionById(newSectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(section);
    }

    @PutMapping("/{sectionID}")
    @Transactional
    public ResponseEntity<Object> updateSection(@PathVariable("sectionID") Long sectionID,
                                           Authentication authentication,
                                           @RequestParam("newSectionName") String newSectionName) {

        Long userID = userService.getUserIDFromAuthentication(authentication);
        sectionsService.updateSection(sectionID, userID, newSectionName);
        Sections updatedSection = sectionsService.getSectionById(sectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSection);
    }

    @DeleteMapping("/{sectionID}")
    @Transactional
    public ResponseEntity<Object> deleteSectionById(@PathVariable("sectionID") Long sectionID,
                                               Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        sectionsService.deleteSectionById(teamLeaderID, sectionID);
        return ResponseEntity.noContent().build();
    }
}
