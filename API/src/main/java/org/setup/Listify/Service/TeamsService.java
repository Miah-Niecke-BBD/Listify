package org.setup.Listify.Service;

import org.setup.Listify.Model.TeamMembers;
import org.setup.Listify.Model.TeamProjects;
import org.setup.Listify.Model.Teams;
import org.setup.Listify.Repo.TeamMembersRepository;
import org.setup.Listify.Repo.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamsService {
    @Autowired
    private TeamsRepository teamsRepository;
    @Autowired
    private TeamMembersRepository teamMembersRepository;

    public List<Teams> getAllTeams() {
        return teamsRepository.findAll();
    }

    public List<TeamMembers> getAllTeamMembers() {
        return teamMembersRepository.findAll();
    }

    public Optional<Teams> getTeamById(Long teamID) {
        return teamsRepository.findById(teamID);
    }

    public Optional<TeamMembers> getByTeamMembersId(Long teamID) {
        return teamMembersRepository.findById(teamID);
    }

    public List<TeamMembers> getTeamMembersByTeamId(Long teamID) {
        return teamMembersRepository.findByTeamID(teamID);
    }

    public List<TeamProjects> getProjectsByTeamID(Long teamID) {
        return teamsRepository.findProjectsByTeamID(teamID);
    }

    public Long addTeam(Long userID, String teamName) {
        teamsRepository.createTeam(userID, teamName);

        Teams lastCreatedTeam = teamsRepository.findTopByOrderByTeamIDDesc();
        return lastCreatedTeam != null ? lastCreatedTeam.getTeamID() : null;
    }

    public void assignMemberToTeam(Long teamLeaderID, Long userID, Long teamID) {
        teamMembersRepository.assignMemberToTeam(teamLeaderID, userID, teamLeaderID);
    }

    public void deleteTeam(Long teamID, Long teamLeaderID) {
        teamsRepository.deleteTeam(teamID, teamLeaderID);
    }

    public void deleteMemberFromTeam(Long userID, Long teamID, Long teamLeaderID) {
        teamMembersRepository.deleteMemberFromTeam(userID, teamID, teamLeaderID);
    }

    public void updateTeamDetails(Long teamID, Long teamLeaderID, String newTeamName) {
        teamsRepository.updateTeamDetails(teamID, teamLeaderID, newTeamName);
    }

    public void updateTeamLeader(Long teamLeaderID, Long teamID, Long newTeamLeaderID) {
        teamMembersRepository.updateTeamLeader(teamLeaderID, teamID, newTeamLeaderID);
    }

}
