package org.listify.repo;

import org.listify.model.ProjectAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAssigneesRepository extends JpaRepository<ProjectAssignees, Long> {

    @Query("SELECT t FROM ProjectAssignees t WHERE t.projectID = :projectID")
    List<ProjectAssignees> findProjectsAssignedUsers(@Param("projectID") Long projectID);

    @Procedure("listify.uspAssignUserToProject")
    void assignUserToProject(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("userID") Long userID,
            @Param("projectID") Long projectID);

    @Procedure("listify.uspRemoveUserFromProjects")
    void deleteUserFromProject(
            @Param("userID") Long userID,
            @Param("projectID") Long projectID,
            @Param("teamLeaderID") Long teamLeaderID);

    @Query("SELECT t FROM ProjectAssignees t ORDER BY t.projectAssigneeID DESC LIMIT 1")
    ProjectAssignees findTopOrderByProjectAssigneeIDDesc();

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN " +
            "CASE WHEN tm.isTeamLeader = true THEN true ELSE false END " +
            "ELSE false END " +
            "FROM ProjectAssignees t " +
            "JOIN Projects p ON t.projectID = p.projectID " +
            "JOIN TeamMembers tm ON tm.teamID = p.teamID AND tm.userID = t.userID " +
            "WHERE t.userID = :userID " +
            "AND t.projectID = :projectID")
    boolean isTeamLeader(@Param("userID") Long userID, @Param("projectID") Long projectID);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM ProjectAssignees t " +
            "WHERE t.userID = :userID AND t.projectID = :projectID")
    boolean isUserAssignedToProject(@Param("userID") Long userID, @Param("projectID") Long projectID);
}