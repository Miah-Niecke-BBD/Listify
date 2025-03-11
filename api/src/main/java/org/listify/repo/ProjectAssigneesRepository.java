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

    @Query("SELECT p FROM ProjectAssignees p WHERE p.projectID = :projectID")
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

    @Query("SELECT CASE WHEN COUNT(tm) > 0 THEN true ELSE false END " +
            "FROM TeamMembers tm " +
            "JOIN Projects p ON p.teamID = tm.teamID " +
            "WHERE p.projectID = :projectID AND tm.userID = :userID AND tm.isTeamLeader = true")
    boolean isTeamLeader(@Param("userID") Long userID, @Param("projectID") Long projectID);
}