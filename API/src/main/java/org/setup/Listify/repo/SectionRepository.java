package org.setup.Listify.repo;

import org.setup.Listify.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Sections, Long> {
    List<Sections> findBySectionName(String sectionName);

    List<Sections> findByProjectID(Long projectId);

    @Procedure(procedureName = "uspCreateSection")
    void createSection(Long projectID, String sectionName, int sectionPosition);

    @Procedure(procedureName = "uspDeleteSection")
    void deleteSection(Long sectionID);

    @Procedure(procedureName = "uspUpdateSectionDetails")
    void updateSectionDetails(Long sectionID, String sectionName, int sectionPosition, Date updatedAt);

    @Procedure(procedureName = "uspUpdateSectionPosition")
    void updateSectionPosition(Long sectionID, String sectionName, int sectionPosition, Date updatedAt);

}
