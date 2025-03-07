package org.listify.controller;

import org.listify.dto.SectionTaskDTO;
import org.listify.model.Sections;
import org.listify.service.SectionsService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/{sectionID}/tasks")
    public ResponseEntity<List<SectionTaskDTO>> getTasksBySectionID(@PathVariable("sectionID") Long sectionID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<SectionTaskDTO> taskDTOS = sectionsService.getTasksBySectionId(sectionID, userID);
        return ResponseEntity.ok(taskDTOS);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Sections> newSection(Authentication authentication,
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
    public ResponseEntity<Sections> updateSection(@PathVariable("sectionID") Long sectionID,
                                           Authentication authentication,
                                           @RequestParam("newSectionName") String newSectionName) {
        Long loggedInUserID = userService.getUserIDFromAuthentication(authentication);
        sectionsService.updateSection(sectionID, loggedInUserID, newSectionName);
        Sections updatedSection = sectionsService.getSectionById(sectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSection);
    }

    @PutMapping("/{sectionID}/position")
    @Transactional
    public ResponseEntity<Sections> updateSectionPosition(@PathVariable("sectionID") Long sectionID,
                                                        Authentication authentication,
                                                        @RequestParam("newSectionPosition") Integer newSectionPosition) {
        Long loggedInUserID = userService.getUserIDFromAuthentication(authentication);
        sectionsService.updateSectionPosition(sectionID, loggedInUserID, newSectionPosition);
        Sections updatedSection = sectionsService.getSectionById(sectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSection);
    }

    @DeleteMapping("/{sectionID}")
    @Transactional
    public ResponseEntity<?> deleteSectionById(@PathVariable("sectionID") Long sectionID,
                                               Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        sectionsService.deleteSectionById(teamLeaderID, sectionID);
        return ResponseEntity.noContent().build();
    }
}
