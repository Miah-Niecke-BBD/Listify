package org.setup.listify.service;

import org.setup.listify.exception.ForbiddenException;
import org.setup.listify.exception.NotFoundException;
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

    public Sections getSectionById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public Long createSection(Long teamLeaderID, Long projectID,
                              String sectionName, Byte sectionPosition) {
        repository.createSection(teamLeaderID, projectID, sectionName, sectionPosition);

        Sections newlyCreatedSection = repository.findTopOrderBySectionIDDesc();
        return newlyCreatedSection != null ? newlyCreatedSection.getSectionID() : null;
    }

    public void updateSection(Long sectionID, Long userID, String newSectionName) {
        if (!repository.isSectionAndUserInSameProject(sectionID, userID)) {
            throw new ForbiddenException("Only sections within your project can be updated.");
        }
        repository.updateSection(sectionID, userID, newSectionName);
    }

    public void deleteSectionById(Long teamLeaderID, Long sectionID) {
        repository.deleteSectionById(teamLeaderID, sectionID);
    }

    public void updateSectionPosition(Long sectionID, Long loggedInUserID, Integer newSectionPosition) {
        if (!repository.isSectionAndUserInSameProject(sectionID, loggedInUserID)) {
            throw new ForbiddenException("Only sections within your project can be updated.");
        }
        repository.updateSectionPosition(loggedInUserID, sectionID, newSectionPosition);
    }
}
