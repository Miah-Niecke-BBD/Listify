package org.setup.Listify.service;

import org.setup.Listify.model.Sections;
import org.setup.Listify.repo.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {


    private final SectionRepository sectionRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }


    public List<Sections> getAllSections() {
        return sectionRepository.findAll();
    }

    public Sections getSectionById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section with ID " + id + " not found"));
    }

    public List<Sections> getSectionsByProjectId(Long projectID) {
        return sectionRepository.findByProjectID(projectID);
    }

    public List<Sections> getSectionsByName(String sectionName) {
        return sectionRepository.findBySectionName(sectionName);
    }

    public void updateSectionDetails(Long sectionID, String sectionName, int sectionPosition, Date updatedAt) {
        sectionRepository.updateSectionDetails(sectionID, sectionName, sectionPosition, updatedAt);
    }

    public void deleteSection(Long sectionID) {
        sectionRepository.deleteSection(sectionID);
    }

    public void createSection(Long projectID, String sectionName, int sectionPosition) {
        sectionRepository.createSection(projectID, sectionName, sectionPosition);
    }
}
