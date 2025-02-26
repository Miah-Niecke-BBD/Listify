<<<<<<< Updated upstream
package org.setup.Listify.controller;

import org.setup.Listify.Model.Sections;
import org.setup.Listify.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public List<Sections> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{id}")
    public Sections getSectionById(@PathVariable Long id) {
        return sectionService.getSectionById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<Sections> getSectionsByProjectId(@PathVariable Long projectId) {
        return sectionService.getSectionsByProjectId(projectId);
    }

    @GetMapping("/search")
    public List<Sections> getSectionsByName(@RequestParam String name) {
        return sectionService.getSectionsByName(name);
    }

    @PutMapping("/{id}")
    public void updateSectionName(@PathVariable Long id, @RequestBody String sectionName) {
        sectionService.updateSectionDetails(id, sectionName, 0, null);
    }

    @DeleteMapping("/{id}")
    public void deleteSectionById(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }

    @PostMapping("/create")
    public void createSection(@RequestParam Long projectID, @RequestParam String sectionName, @RequestParam int sectionPosition) {
        sectionService.createSection(projectID, sectionName, sectionPosition);
    }
=======
package org.setup.Listify.controller;

import org.setup.Listify.Assembler.SectionModelAssembler;
import org.setup.Listify.Model.Sections;
import org.setup.Listify.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionModelAssembler SectionModelAssembler;

    @GetMapping
    public List<Sections> getAllSections() {
        List<Sections> sections = sectionService.getAllSections();
        return SectionModelAssembler.toModels(sections);
    }


    @GetMapping("/{id}")
    public Sections getSectionById(@PathVariable Long id) {
        return sectionService.getSectionById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<Sections> getSectionsByProjectId(@PathVariable Long projectId) {
        return sectionService.getSectionsByProjectId(projectId);
    }

    @GetMapping("/search")
    public List<Sections> getSectionsByName(@RequestParam String name) {
        return sectionService.getSectionsByName(name);
    }


    @PutMapping("/{id}")
    public void updateSectionName(@PathVariable Long id, @RequestBody String sectionName) {
        sectionService.updateSectionDetails(id, sectionName, 0, null);
    }

    @DeleteMapping("/{id}")
    public void deleteSectionById(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }

    @PostMapping("/create")
    public void createSection(@RequestParam Long projectID, @RequestParam String sectionName, @RequestParam int sectionPosition) {
        sectionService.createSection(projectID, sectionName, sectionPosition);
    }



>>>>>>> Stashed changes
}