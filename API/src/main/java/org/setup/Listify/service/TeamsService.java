package org.setup.Listify.service;

import org.setup.Listify.exception.ListNotFoundException;
import org.setup.Listify.exception.TeamMemberNotFoundException;
import org.setup.Listify.exception.TeamNotFoundException;
import org.setup.Listify.model.TeamMembers;
import org.setup.Listify.model.TeamProjects;
import org.setup.Listify.model.Teams;
import org.setup.Listify.repo.TeamMembersRepository;
import org.setup.Listify.repo.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamsService {
    @Autowired
    private TeamsRepository teamsRepository;
    @Autowired
    private TeamMembersRepository teamMembersRepository;

    public List<Teams> getAllTeams() {
        List<Teams> teams = teamsRepository.findAll();

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
        return teamsRepository.findById(teamID)
                .orElseThrow(() -> new TeamNotFoundException(teamID));
    }

    public TeamMembers getByTeamMembersId(Long userID) {
        return teamMembersRepository.findById(userID)
                .orElseThrow(() -> new TeamMemberNotFoundException(userID));
    }

    public List<TeamMembers> getTeamMembersByTeamId(Long teamID) {
        getTeamById(teamID);
        List<TeamMembers> teamMembers = teamMembersRepository.findByTeamID(teamID);

        if (teamMembers.isEmpty()) {
            throw new ListNotFoundException("team members for team " + teamID);
        }

        return teamMembers;
    }

    public List<TeamProjects> getProjectsByTeamID(Long teamID) {
        getTeamById(teamID);
        List<TeamProjects> teamProjects = teamsRepository.findProjectsByTeamID(teamID);

        if(teamProjects.isEmpty()) {
            throw new ListNotFoundException("team projects for team " + teamID);
        }

        return teamProjects;
    }

    public Long addTeam(Long userID, String teamName) {
        getByTeamMembersId(userID);
        teamsRepository.createTeam(userID, teamName);

        Teams lastCreatedTeam = teamsRepository.findTopByOrderByTeamIDDesc();
        return lastCreatedTeam != null ? lastCreatedTeam.getTeamID() : null;
    }

    public void assignMemberToTeam(Long teamLeaderID, Long userID, Long teamID) {
        getTeamById(teamID);
        teamMembersRepository.assignMemberToTeam(teamLeaderID, userID, teamID);
    }

    public void deleteTeam(Long teamID, Long teamLeaderID) {
        getTeamById(teamID);
        teamsRepository.deleteTeam(teamID, teamLeaderID);
    }

    public void deleteMemberFromTeam(Long userID, Long teamID, Long teamLeaderID) {
        getTeamById(teamID);
        teamMembersRepository.deleteMemberFromTeam(userID, teamID, teamLeaderID);
    }

    public void updateTeamDetails(Long teamID, Long teamLeaderID, String newTeamName) {
        getTeamById(teamID);
        teamsRepository.updateTeamDetails(teamID, teamLeaderID, newTeamName);
    }

    public void updateTeamLeader(Long teamLeaderID, Long teamID, Long newTeamLeaderID) {
        getTeamById(teamID);
        teamMembersRepository.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderID);
    }

}
