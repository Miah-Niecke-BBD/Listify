package org.setup.listify.service;

import org.setup.listify.exception.BadRequestException;
import org.setup.listify.exception.ForbiddenException;
import org.setup.listify.model.Sections;
import org.setup.listify.repo.SectionsRepository;
import org.springframework.stereotype.Service;

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
        if (sectionName.length() > 100) {
            throw new BadRequestException("Section name has a maximum of 100 characters");
        }
        repository.createSection(teamLeaderID, projectID, sectionName, sectionPosition);

        Sections newlyCreatedSection = repository.findTopOrderBySectionIDDesc();
        return newlyCreatedSection != null ? newlyCreatedSection.getSectionID() : null;
    }

    public void updateSection(Long sectionID, Long userID, String newSectionName) {
        if (newSectionName.length() > 100) {
            throw new BadRequestException("Section name has a maximum of 100 characters");
        }

        if (!repository.isSectionAndUserInSameProject(sectionID, userID)) {
            throw new ForbiddenException("Only sections within your project can be updated.");
        }
        repository.updateSection(sectionID, userID, newSectionName);
    }

    public void deleteSectionById(Long teamLeaderID, Long sectionID) {
        Integer userAccessToTask = repository.userHasAccessToSection(teamLeaderID, sectionID);
        if (userAccessToTask == null || userAccessToTask == 0) {
            throw new ForbiddenException("User does not have access to this section");
        }
        repository.deleteSectionById(teamLeaderID, sectionID);
    }

    public void updateSectionPosition(Long sectionID, Long loggedInUserID, Integer newSectionPosition) {
        if (!repository.isSectionAndUserInSameProject(sectionID, loggedInUserID)) {
            throw new ForbiddenException("Only sections within your project can be updated.");
        }
        repository.updateSectionPosition(loggedInUserID, sectionID, newSectionPosition);
    }
}
