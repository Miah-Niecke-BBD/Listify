package org.setup.Listify.service;

import org.setup.Listify.model.Sections;
import org.setup.Listify.model.Tasks;
import org.setup.Listify.repo.SectionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionsService {

    private final SectionsRepository repository;

    public SectionsService(SectionsRepository repository) {
        this.repository = repository;
    }

    public List<Sections> getAllSections() {
        return repository.findAll();
    }

    public Sections getSectionById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public List<Tasks> getTaskBySectionId(Long sectionId) {
        return repository.findTasksBySectionID(sectionId);
    }

    public Long createSection(Integer teamLeaderID, Integer projectID,
                              String sectionName, Byte sectionPosition) {
        repository.createSection(teamLeaderID, projectID, sectionName, sectionPosition);

        Sections newlyCreatedSection = repository.findTopOrderBySectionIDDesc();
        return newlyCreatedSection != null ? newlyCreatedSection.getSectionID() : null;
    }

    public void updateSection(Long id, Integer userID, String newSectionName) {
        repository.updateSection(id, userID, newSectionName);
    }

    public void deleteSectionById(Integer teamLeaderID, int sectionID) {
        repository.deleteSectionById(teamLeaderID, sectionID);
    }
}
