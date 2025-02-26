package org.setup.Listify.repo;

import org.setup.Listify.model.TeamProjects;
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
    void updateTeamDetails(@Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID, @Param("newTeamName") String newTeamName);

    @Query(value = "SELECT * FROM listify.vTeamProjects WHERE teamID=?1", nativeQuery = true)
    List<TeamProjects> findProjectsByTeamID(Long teamID);

    @Query(value = "SELECT * FROM Teams ORDER BY teamID DESC LIMIT 1", nativeQuery = true)
    Teams findTopByOrderByTeamIDDesc();
}
