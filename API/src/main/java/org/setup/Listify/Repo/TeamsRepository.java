package org.setup.Listify.Repo;

import org.setup.Listify.Model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamsRepository extends JpaRepository<Teams, Long> {
    Teams findByTeamID(Long teamID);

    @Procedure("listify.uspCreateTeam")
    void createTeam(@Param("userID") Long userID, @Param("teamName") String teamName);

    @Procedure("listify.upsDeleteTeam")
    void deleteTeam(@Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID);

    @Procedure("listify.uspUpdateTeamDetails")
    void updateTeamDetails(@Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID, @Param("newTeamName") String newTeamName);

}
