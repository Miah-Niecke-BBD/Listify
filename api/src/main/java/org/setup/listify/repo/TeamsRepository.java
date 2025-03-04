package org.setup.listify.repo;

import org.setup.listify.model.Projects;
import org.setup.listify.model.Tasks;
import org.setup.listify.model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamsRepository extends JpaRepository<Teams, Long> {
    Teams findByTeamID(Long teamId);

    @Procedure("listify.uspCreateTeam")
    void createTeam(@Param("userID") Long userID, @Param("teamName") String teamName);

    @Procedure("listify.uspDeleteTeam")
    void deleteTeam(@Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID);

    @Procedure("listify.uspUpdateTeamDetails")
    void updateTeamDetails(@Param("userID") Long userID, @Param("teamID") Long teamID, @Param("newTeamName") String newTeamName);

    @Query(value = "SELECT TOP 1 * FROM listify.Teams ORDER BY teamID DESC", nativeQuery = true)
    Teams findTopByOrderByTeamIDDesc();

    @Query(value = "SELECT teamID, teamName, teamCreatedAt AS createdAt, teamUpdatedAt AS updatedAt FROM listify.vUserTeams WHERE userID = ?1", nativeQuery = true)
    List<Teams> findTeamsByUserID(Long userID);

    @Query(value = "SELECT teamID, teamName, teamCreatedAt AS createdAt, teamUpdatedAt AS updatedAt FROM listify.vUserTeams WHERE userID = ?1 AND teamID = ?2", nativeQuery = true)
    Teams findATeamByUserID(Long userID, Long teamID);

    @Query(value = "SELECT " +
            "t.taskID, t.sectionID, t.parentTaskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dateCompleted, t.dueDate, t.createdAt, t.updatedAt " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "INNER JOIN listify.Tasks t ON v.taskID = t.taskID " +
            "WHERE v.userID = ?1 AND t.dueDate IS NOT NULL AND (?2 IS NULL OR v.teamID = ?2) AND (?3 IS NULL OR v.projectID = ?3)", nativeQuery = true)
    List<Tasks> findTeamsDueTasks(Long userID, Long teamID, Long projectID);

    @Query(value = "SELECT" +
            " projectID, teamID, projectName, projectDescription, projectCreatedAt AS createdAt, projectUpdatedAt AS updatedAt" +
            " FROM listify.vUserTeamProjects" +
            " WHERE userID = ?1 AND teamID = ?2", nativeQuery = true)
    List<Projects> findTeamProjects(Long userID, Long teamID);
}
