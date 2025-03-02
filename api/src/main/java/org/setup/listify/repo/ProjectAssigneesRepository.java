package org.setup.listify.repo;

import org.setup.listify.model.ProjectAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAssigneesRepository extends JpaRepository<ProjectAssignees, Long> {

    @Query("SELECT p FROM ProjectAssignees p WHERE p.userID = :userID")
    List<ProjectAssignees> findProjectsAssignedToUser(@Param("userID") Long userID);

    @Query(value = "INSERT INTO ProjectAssignees (userID, projectID) VALUES (:userID, :projectID)", nativeQuery = true)
    void assignUserToProject(@Param("userID") int userID, @Param("projectID") int projectID);

    @Query(value = "DELETE FROM ProjectAssignees WHERE userID = :userID AND projectID = :projectID AND EXISTS (" +
            "SELECT 1 FROM Projects WHERE projectID = :projectID" +
            ")", nativeQuery = true)
    void deleteUserFromProject(@Param("userID") int userID, @Param("projectID") int projectID, @Param("teamLeaderID") int teamLeaderID);

    @Query("SELECT p FROM ProjectAssignees p ORDER BY p.projectAssigneeID DESC LIMIT 1")
    ProjectAssignees findTopOrderByProjectAssigneeIDDesc();
}
