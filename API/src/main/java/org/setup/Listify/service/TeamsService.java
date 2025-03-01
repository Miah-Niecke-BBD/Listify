package org.setup.Listify.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.setup.Listify.exception.*;
import org.setup.Listify.model.TeamMembers;
import org.setup.Listify.dto.UserTeamProjects;
import org.setup.Listify.model.Teams;
import org.setup.Listify.repo.TeamMembersRepository;
import org.setup.Listify.repo.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Teams> teams = teamsRepository.findTeamsByUserID(userID);

        if (teams.isEmpty()) {
            throw new ListNotFoundException("teams");
        }
        return teams;
    }

    public List<TeamMembers> getAllTeamMembers() {
        List<TeamMembers> teamMembers = teamMembersRepository.findAll();

        if (teamMembers.isEmpty()) {
            throw new ListNotFoundException("team members");
        }

        return teamMembers;
    }

    public Teams getTeamById(Long teamID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);

        Teams team = teamsRepository.getTeamById(teamID, userID);

        if (team == null) {
            throw new TeamNotFoundException(teamID);
        }

        return team;
    }

    public TeamMembers getByTeamMembersId(Long userID) {
        return teamMembersRepository.findById(userID).orElseThrow(() -> new TeamMemberNotFoundException(userID));
    }

    public TeamMembers getTeamMemberByUserId(Long userID) {
        List<TeamMembers> teamMembersList = teamMembersRepository.findByUserID(userID);

        if (teamMembersList.isEmpty()) {
            throw new UserNotFoundException("User with id: " + userID + " not found");
        }
        return teamMembersList.getFirst();
    }

    public List<TeamMembers> getTeamMembersByTeamId(Long teamID) {
        getTeamById(teamID);
        List<TeamMembers> teamMembers = teamMembersRepository.findByTeamID(teamID);

        if (teamMembers.isEmpty()) {
            throw new ListNotFoundException("team members for team " + teamID);
        }

        return teamMembers;
    }

    public boolean isTeamLeader(Long teamID, Long userID) {
        boolean isTeamLeader = teamMembersRepository.existsByTeamIDAndUserIDAndIsTeamLeaderTrue(teamID, userID);
        if (!isTeamLeader) {
            throw new NotTeamLeaderException("User with ID " + userID + " is not the team leader for team ID " + teamID);
        }
        return isTeamLeader;
    }


    public List<UserTeamProjects> getProjectsByTeamIDAndUserID(Long teamID, Long userID) {
        getTeamById(teamID);
        List<UserTeamProjects> userTeamProjects = teamsRepository.findProjectsByTeamIDAndUserID(teamID, userID);

        if (userTeamProjects.isEmpty()) {
            throw new ListNotFoundException("team projects for team " + teamID);
        }

        return userTeamProjects;
    }

    public Long addTeam(Long userID, String teamName) {
        TeamMembers teamMember = getTeamMemberByUserId(userID);

        teamsRepository.createTeam(userID, teamName);

        Teams lastCreatedTeam = teamsRepository.findTopByOrderByTeamIDDesc();
        return lastCreatedTeam != null ? lastCreatedTeam.getTeamID() : null;
    }

    public void assignMemberToTeam(Long teamLeaderID, Long userID, Long teamID) {
        getTeamById(teamID);
        getTeamMemberByUserId(userID);
        isTeamLeader(teamID, teamLeaderID);
        boolean memberExists = teamMembersRepository.existsByTeamIDAndUserID(teamID, userID);

        if (memberExists) {
            throw new TeamMemberAlreadyExistsException("User with ID " + userID + " is already a member of team " + teamID);
        }

        teamMembersRepository.assignMemberToTeam(teamLeaderID, userID, teamID);
    }

    public void deleteTeam(Long teamID, Long teamLeaderID) {
        getTeamById(teamID);
        isTeamLeader(teamID, teamLeaderID);
        teamsRepository.deleteTeam(teamID, teamLeaderID);
    }

    public void deleteMemberFromTeam(Long userID, Long teamID, Long teamLeaderID) {
        getTeamById(teamID);
        isTeamLeader(teamID, teamLeaderID);
        boolean memberExists = teamMembersRepository.existsByTeamIDAndUserID(teamID, userID);
        if (userID == teamID) {
            throw new DeleteTeamLeaderException();
        }

        if (!memberExists) {
            throw new TeamMemberAlreadyExistsException("User with ID " + userID + " is not a member of team " + teamID);
        }
        teamMembersRepository.deleteMemberFromTeam(userID, teamID, teamLeaderID);
    }

    @Transactional
    public void updateTeamDetails(Long userID, Long teamID, String newTeamName) {
        getTeamById(teamID);
        isTeamLeader(teamID, userID);
        teamsRepository.updateTeamDetails(userID, teamID, newTeamName);
        entityManager.flush();
        entityManager.clear();
    }

    public void updateTeamLeader(Long teamLeaderID, Long teamID, Long newTeamLeaderID) {
        getTeamById(teamID);
        isTeamLeader(teamID, teamLeaderID);
        boolean memberExists = teamMembersRepository.existsByTeamIDAndUserID(teamID, newTeamLeaderID);

        if (!memberExists) {
            throw new TeamMemberAlreadyExistsException("User with ID " + newTeamLeaderID + " is not a member of team " + teamID);
        }
        teamMembersRepository.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderID);
    }
}
