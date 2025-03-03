package org.setup.listify.service;

import org.setup.listify.exception.ListNotFoundException;
import org.setup.listify.exception.SectionNotFoundException;
import org.setup.listify.model.Sections;
import org.setup.listify.model.Tasks;
import org.setup.listify.repo.SectionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionsService {

    private final SectionsRepository repository;

    public SectionsService(SectionsRepository repository) {
        this.repository = repository;
    }

    public List<Sections> getAllSections() {
        List<Sections> sections = repository.findAll();
        if (sections.isEmpty()) {
            throw new ListNotFoundException("sections");
        }
        return sections;
    }

    public Sections getSectionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SectionNotFoundException(id));
    }

    public List<Tasks> getTaskBySectionId(Long sectionId) {
        List<Tasks> tasksInSection = repository.findTasksBySectionID(sectionId);
        if (tasksInSection.isEmpty()) {
            throw new SectionNotFoundException(sectionId);
        }
        return tasksInSection;
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
