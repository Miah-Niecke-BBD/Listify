package org.setup.Listify.repo;

import org.setup.Listify.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface SectionsRepository extends JpaRepository<Sections, Long> {

    @Procedure("listify.uspCreateSection")
    void createSection(@Param("teamLeaderID") Integer teamLeaderID,
                       @Param("projectID") Integer projectID,
                       @Param("sectionName") String sectionName,
                       @Param("sectionPosition") Byte sectionPosition
    );

    @Query(value = "SELECT TOP 1 * FROM listify.Sections ORDER BY sectionID DESC", nativeQuery = true)
    Sections findTopOrderBySectionIDDesc();

    @Procedure("listify.uspUpdateSectionDetails")
    void updateSection(@Param("sectionID") Long sectionID,
                       @Param("userID") Integer userID,
                       @Param("newSectionName") String newSectionName
    );

    @Procedure("listify.uspDeleteSection")
    void deleteSectionById(@Param("teamLeaderID")Integer teamLeaderID,
                           @Param("sectionID") Integer sectionID
    );
}
