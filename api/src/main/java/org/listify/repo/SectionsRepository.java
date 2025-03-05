package org.listify.repo;

import org.listify.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Sections s " +
            "JOIN Tasks t ON s.sectionID = t.sectionID " +
            "JOIN Projects p ON s.projectID = p.projectID " +
            "JOIN ProjectAssignees pa ON p.projectID = pa.projectID " +
            "WHERE s.sectionID = :sectionID " +
            "AND pa.userID = :userID")
    boolean isSectionAndUserInSameProject(@Param("sectionID") Long sectionID,
                                          @Param("userID") Long userID);
}
