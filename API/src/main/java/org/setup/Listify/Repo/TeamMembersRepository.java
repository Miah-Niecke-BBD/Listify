package org.setup.Listify.Repo;

import org.setup.Listify.Model.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {

    @Procedure("listify.uspAssignUserToTeam")
    void assignUserToTeam(@Param("teamLeaderID") Long teamLeaderID, @Param("userID") Long userID, @Param("teamID") Long TeamID);

    @Procedure("listify.uspDeleteUserFromTeam")
    void deleteUserFromTeam(@Param("userID") Long userID, @Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID);

    @Procedure("listify.uspUpdateTeamLeader")
    void updateTeamLeader(@Param("teamLeaderID") Long teamLeaderID, @Param("teamID") Long teamID, @Param("newTeamLeaderID") Long newTeamLeaderID);

}
