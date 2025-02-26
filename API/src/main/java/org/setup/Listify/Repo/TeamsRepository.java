package org.setup.Listify.Repo;

import org.setup.Listify.Model.TeamProjects;
import org.setup.Listify.Model.Teams;
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

    @Query(value = "SELECT * FROM listify.vTeamProjects WHERE teamID=?1", nativeQuery = true)
    List<TeamProjects> findProjectsByTeamID(Long teamID);

    @Query(value = "SELECT TOP 1 * FROM listify.Teams ORDER BY teamID DESC", nativeQuery = true)
    Teams findTopByOrderByTeamIDDesc();
}
