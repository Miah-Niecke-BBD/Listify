package org.listify.repo;

import org.listify.model.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {

    @Procedure("listify.uspAssignUserToTeam")
    void assignMemberToTeam(@Param("teamLeaderID") Long teamLeaderID, @Param("userID") Long userID, @Param("teamID") Long TeamID);

    @Procedure("listify.uspDeleteUserFromTeam")
    void deleteMemberFromTeam(@Param("userID") Long userID, @Param("teamID") Long teamID, @Param("teamLeaderID") Long teamLeaderID);

    @Procedure("listify.uspUpdateTeamLeader")
    void updateTeamLeader(@Param("teamLeaderID") Long teamLeaderID, @Param("teamID") Long teamID, @Param("newTeamLeaderID") Long newTeamLeaderID);

    List<TeamMembers> findByTeamID(Long teamID);

    boolean existsByTeamIDAndUserIDAndIsTeamLeaderTrue(Long teamID, Long userID);

    boolean existsByTeamIDAndUserID(Long teamID, Long userID);
}
