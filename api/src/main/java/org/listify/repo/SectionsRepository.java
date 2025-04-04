package org.listify.repo;

import org.listify.model.Sections;
import org.listify.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionsRepository extends JpaRepository<Sections, Long> {

    @Procedure("listify.uspAddSection")
    void createSection(@Param("teamLeaderID") Long teamLeaderID,
                       @Param("projectID") Long projectID,
                       @Param("sectionName") String sectionName,
                       @Param("sectionPosition") Byte sectionPosition
    );

    @Query(value = "SELECT TOP 1 * FROM listify.Sections ORDER BY sectionID DESC", nativeQuery = true)
    Sections findTopOrderBySectionIDDesc();

    @Procedure("listify.uspUpdateSectionDetails")
    void updateSection(@Param("sectionID") Long sectionID,
                       @Param("userID") Long userID,
                       @Param("newSectionName") String newSectionName
    );

    @Procedure("listify.uspDeleteSection")
    void deleteSectionById(@Param("teamLeaderID")Long teamLeaderID,
                           @Param("sectionID") Long sectionID
    );

    @Procedure("listify.uspUpdateSectionPosition")
    void updateSectionPosition(@Param("userID") Long loggedInUserID,
                               @Param("sectionID") Long sectionID,
                               @Param("newSectionPosition") Integer newSectionPosition);


    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM listify.ProjectAssignees pa " +
            "JOIN listify.Projects p ON pa.projectID = p.projectID " +
            "JOIN listify.Sections s ON p.projectID = s.projectID " +
            "WHERE pa.userID = :userID " +
            "AND s.sectionID = :sectionID", nativeQuery = true)
    Integer userHasAccessToSection(Long userID, Long sectionID);


    @Query("SELECT t FROM Tasks t WHERE t.sectionID = ?1")
    List<Tasks> findTasksBySectionID(Long sectionID);
}
