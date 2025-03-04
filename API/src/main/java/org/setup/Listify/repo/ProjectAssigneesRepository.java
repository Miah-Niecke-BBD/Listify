package org.setup.Listify.repo;

import org.setup.Listify.model.ProjectAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAssigneesRepository extends JpaRepository<ProjectAssignees, Long> {

    @Query("SELECT p FROM ProjectAssignees p WHERE p.userID = :userID")
    List<ProjectAssignees> findProjectsAssignedToUser(@Param("userID") Long userID);


    @Procedure("listify.uspAssignUserToProject")
    void assignUserToProject(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("userID") int userID,
            @Param("projectID") int projectID);

    @Procedure("listify.uspRemoveUserFromProjects")
    void deleteUserFromProject(
            @Param("userID") int userID,
            @Param("projectID") int projectID,
            @Param("teamLeaderID") int teamLeaderID);


    @Query("SELECT p FROM ProjectAssignees p ORDER BY p.projectAssigneeID DESC LIMIT 1")
    ProjectAssignees findTopOrderByProjectAssigneeIDDesc();
}
