package org.setup.listify.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.setup.listify.exception.*;
import org.setup.listify.model.Projects;
import org.setup.listify.model.TeamMembers;
import org.setup.listify.model.Teams;
import org.setup.listify.model.Users;
import org.setup.listify.repo.TeamMembersRepository;
import org.setup.listify.repo.TeamsRepository;
import org.setup.listify.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamsService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TeamsRepository teamsRepository;
    @Autowired
    private TeamMembersRepository teamMembersRepository;
    @Autowired
    UserService userService;

    public List<Teams> getAllUserTeams(Long userID) {
        if (userID == null) {
            throw new UnauthorizedException("User is not authenticated. Please log in.");
        }

        List<Teams> teams = teamsRepository.findTeamsByUserID(userID);

        if (teams.isEmpty()) {
            throw new NotFoundException("No teams for user " + userID + " were found");
        }
        return teams;
    }

    public void checkTeamExistence(Long teamID) {
        if (teamsRepository.findByTeamID(teamID) == null) {
            throw new NotFoundException("Team with id " + teamID + " was not found");
        }
    }


    public Teams findATeamByUserID(Long userID, Long teamID) {
        if (userID == null) {
            throw new UnauthorizedException("User is not authenticated. Please log in.");
        }

        checkTeamExistence(teamID);

        Teams team = teamsRepository.findATeamByUserID(userID, teamID);

        if (team == null) {
            throw new ForbiddenException("User " + userID + " is not added to the team " + teamID);
        }

        return team;
    }

    public List<TeamMembers> getTeamMembersByTeamID(Long teamID, Long userID) {
        findATeamByUserID(userID, teamID);
        List<TeamMembers> teamMembers = teamMembersRepository.findByTeamID(teamID);

        if (teamMembers.isEmpty()) {
            throw new NotFoundException("No members for this team was found " + teamID);
        }

        return teamMembers;
    }

    public boolean isTeamLeader(Long teamID, Long userID) {
        return teamMembersRepository.existsByTeamIDAndUserIDAndIsTeamLeaderTrue(teamID, userID);
    }

    public List<Projects> findTeamProjects(Long teamID, Long userID) {
        findATeamByUserID(userID, teamID);
        return teamsRepository.findTeamProjects(userID, teamID);
    }

    @Transactional
    public Long addTeam(Long userID, String teamName) {
        if (userID == null) {
            throw new UnauthorizedException("User is not authenticated. Please log in");
        }

        teamsRepository.createTeam(userID, teamName);

        Teams lastCreatedTeam = teamsRepository.findTopByOrderByTeamIDDesc();
        return lastCreatedTeam != null ? lastCreatedTeam.getTeamID() : null;
    }

    @Transactional
    public void assignMemberToTeam(Long teamLeaderID, String githubID, Long teamID) {
        Long userID = userService.getUserIDFromGithubID(githubID);

        Teams team = findATeamByUserID(teamLeaderID, teamID);

        if (team.getCreatedAt().equals(userService.getUserCreatedAt(teamLeaderID))) {
            throw new ForbiddenException("User can't assign members to personal team");
        }

        boolean isTeamLeader = isTeamLeader(teamID, teamLeaderID);
        if (!isTeamLeader) {
            throw new ForbiddenException("Only a team leader can add new members to a team");
        }

        boolean memberExists = teamMembersRepository.existsByTeamIDAndUserID(teamID, userID);
        if (memberExists) {
            throw new ForbiddenException("User with ID " + userID + " is already a member of team " + teamID);
        }

        teamMembersRepository.assignMemberToTeam(teamLeaderID, userID, teamID);
    }

    @Transactional
    public void deleteTeam(Long teamID, Long teamLeaderID) {
        Teams team = findATeamByUserID(teamLeaderID, teamID);
        boolean isTeamLeader = isTeamLeader(teamID, teamLeaderID);

        if (!isTeamLeader) {
            throw new ForbiddenException("Only the team leader can delete a team");
        }

        if (team.getCreatedAt().equals(userService.getUserCreatedAt(teamLeaderID))) {
            throw new ForbiddenException("User can't delete personal team");
        }

        teamsRepository.deleteTeam(teamID, teamLeaderID);
    }

    @Transactional
    public void deleteMemberFromTeam(String githubID, Long teamID, Long teamLeaderID) {
        Long userID = userService.getUserIDFromGithubID(githubID);

        boolean isTeamLeader = isTeamLeader(teamID, teamLeaderID);
        if (!isTeamLeader) {
            throw new ForbiddenException("Only team leaders can delete a member from a team");
        }

        if (userID.equals(teamLeaderID)) {
            throw new ForbiddenException("A team leader can't be deleted from the team, please first re-assign team leader");
        }

        boolean memberExists = teamMembersRepository.existsByTeamIDAndUserID(teamID, userID);
        if (!memberExists) {
            throw new ForbiddenException("User with ID " + userID + " is not a member of team " + teamID);
        }

        teamMembersRepository.deleteMemberFromTeam(userID, teamID, teamLeaderID);
    }

    @Transactional
    public Teams updateTeamDetails(Long userID, Long teamID, String newTeamName) {
        findATeamByUserID(userID, teamID);
        boolean isTeamLeader = isTeamLeader(teamID, userID);

        if (!isTeamLeader) {
            throw new ForbiddenException("Only team leaders can update teams details");
        }

        teamsRepository.updateTeamDetails(userID, teamID, newTeamName);
        entityManager.flush();
        entityManager.clear();

        return findATeamByUserID(userID, teamID);
    }

    public void updateTeamLeader(Long teamLeaderID, Long teamID, String newTeamLeaderGithubID) {
        Long newTeamLeaderID = userService.getUserIDFromGithubID(newTeamLeaderGithubID);

        findATeamByUserID(newTeamLeaderID, teamID);
        boolean isTeamLeader = isTeamLeader(teamID, teamLeaderID);

        if (!isTeamLeader) {
            throw new ForbiddenException("Only a team leader can reassign a team leader");
        }

        teamMembersRepository.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderID);
    }

}
