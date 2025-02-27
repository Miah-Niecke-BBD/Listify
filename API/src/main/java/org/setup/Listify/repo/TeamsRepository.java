package org.setup.Listify.repo;

import org.setup.Listify.model.Tasks;
import org.setup.Listify.model.UserTeamProjects;
import org.setup.Listify.model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamsRepository extends JpaRepository<Teams, Long> {
    @Procedure("listify.uspCreateTeam")
    void createTeam(@Param("userID") Long userID, @Param("teamName") String teamName);

    @Procedure("listify.uspDeleteTeam")
    void deleteTeam(@Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID);

    @Procedure("listify.uspUpdateTeamDetails")
    void updateTeamDetails(@Param("userID") Long userID, @Param("teamID") Long teamID, @Param("newTeamName") String newTeamName);

    @Query(value = "SELECT * FROM listify.vUserTeamProjects WHERE teamID = ?1 AND userID = ?2", nativeQuery = true)
    List<UserTeamProjects> findProjectsByTeamIDAndUserID(Long teamID, Long userID);

    @Query(value = "SELECT TOP 1 * FROM listify.Teams ORDER BY teamID DESC", nativeQuery = true)
    Teams findTopByOrderByTeamIDDesc();

    @Query(value = "SELECT teamID, teamName, teamCreatedAt AS createdAt, teamUpdatedAt AS updatedAt FROM listify.vUserTeams WHERE userID = ?1", nativeQuery = true)
    List<Teams> findTeamsByUserID(Long userID);

    @Query(value = "SELECT teamID, teamName, teamCreatedAt AS createdAt, teamUpdatedAt AS updatedAt FROM listify.vUserTeams WHERE teamID = ?1 AND userID = ?2", nativeQuery = true)
    Teams getTeamById(Long teamID, Long userID);

    @Query(value = "SELECT " +
            "t.taskID, t.sectionID, t.parentTaskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dateCompleted, t.dueDate, t.createdAt, t.updatedAt " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "INNER JOIN listify.Tasks t ON v.taskID = t.taskID " +
            "WHERE v.teamID = ?1 AND v.userID = ?2 AND t.dueDate IS NOT NULL AND (?3 IS NULL OR v.projectID = ?3)", nativeQuery = true)
    List<Tasks> findTeamsDueTasks(Long teamId, Long userID, Long projectID);

}
