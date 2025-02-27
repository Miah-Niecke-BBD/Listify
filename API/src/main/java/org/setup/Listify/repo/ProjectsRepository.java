package org.setup.Listify.repo;

import org.setup.Listify.model.Projects;
import org.setup.Listify.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {

    @Procedure("listify.uspCreateProject")
    void createProject(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("teamID") int teamID,
            @Param("projectName") String projectName,
            @Param("projectDescription") String projectDescription
    );

    @Procedure("listify.uspUpdateProjectDetails")
    void updateProject(
            @Param("projectID") int projectID,
            @Param("userID") int userID,
            @Param("projectName") String projectName,
            @Param("projectDescription") String projectDescription
    );

    @Procedure("listify.uspDeleteProject")
    void deleteProject(
            @Param("projectID") int projectID,
            @Param("teamLeaderID") int teamLeaderID
    );

    @Query(value = "SELECT TOP 1 * FROM listify.Projects ORDER BY projectID DESC", nativeQuery = true)
    Projects findTopOrderByProjectIDDesc();

    @Query("SELECT s FROM Sections s WHERE s.projectID = :id")
    List<Sections> findAllSectionsInProject(@Param("id") Long id);

}
