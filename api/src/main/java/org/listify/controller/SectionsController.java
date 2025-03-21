package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.SectionTaskDTO;
import org.listify.dto.UpdateSectionDTO;
import org.listify.dto.UpdateSectionPositionDTO;
import org.listify.model.Sections;
import org.listify.service.SectionsService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sections")
public class SectionsController {

    private final SectionsService sectionsService;
    private final UserService userService;

    public SectionsController(SectionsService sectionsService, UserService userService) {
        this.sectionsService = sectionsService;
        this.userService = userService;
    }


    @GetMapping("/{sectionID}/tasks")
    public ResponseEntity<List<SectionTaskDTO>> getTasksBySectionID(@PathVariable("sectionID") Long sectionID , HttpServletRequest request) {
        Long userID = userService.getUserIDFromAuthentication(request);
        List<SectionTaskDTO> taskDTOS = sectionsService.getTasksBySectionId(sectionID, userID);
        return ResponseEntity.ok(taskDTOS);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Sections> newSection(HttpServletRequest request,
                                        @RequestParam(name = "projectID") Long projectID,
                                        @RequestParam(name = "sectionName") String sectionName,
                                        @RequestParam(name = "sectionPosition") Byte sectionPosition) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        Long newSectionID = sectionsService.createSection(teamLeaderID, projectID, sectionName, sectionPosition);
        Sections section = sectionsService.getSectionById(newSectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(section);
    }


    @PutMapping("/{sectionID}")
    @Transactional
    public ResponseEntity<Sections> updateSection(@PathVariable("sectionID") Long sectionID,
                                                  HttpServletRequest request,
                                                  @RequestBody UpdateSectionDTO updateSectionDTO) {
        Long loggedInUserID = userService.getUserIDFromAuthentication(request);
        sectionsService.updateSection(sectionID, loggedInUserID, updateSectionDTO);
        Sections updatedSection = sectionsService.getSectionById(sectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSection);
    }


    @PutMapping("/{sectionID}/position")
    @Transactional
    public ResponseEntity<Sections> updateSectionPosition(@PathVariable("sectionID") Long sectionID,
                                                          HttpServletRequest request,
                                                          @RequestBody UpdateSectionPositionDTO updatedPosition) {
        Long loggedInUserID = userService.getUserIDFromAuthentication(request);
        sectionsService.updateSectionPosition(sectionID, loggedInUserID, updatedPosition);
        Sections updatedSection = sectionsService.getSectionById(sectionID);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSection);
    }


    @DeleteMapping("/{sectionID}")
    @Transactional
    public ResponseEntity<?> deleteSectionById(@PathVariable("sectionID") Long sectionID,
                                               HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        sectionsService.deleteSectionById(teamLeaderID, sectionID);
        return ResponseEntity.noContent().build();
    }
}
