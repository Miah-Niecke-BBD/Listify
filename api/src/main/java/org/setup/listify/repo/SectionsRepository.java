package org.setup.listify.repo;

import org.setup.listify.model.Sections;
import org.setup.listify.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionsRepository extends JpaRepository<Sections, Long> {

    @Procedure("listify.uspAddSection")
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

    @Query("SELECT t FROM Tasks t WHERE t.sectionID = :id")
    List<Tasks> findTasksBySectionID(@Param("id") Long id);
}
