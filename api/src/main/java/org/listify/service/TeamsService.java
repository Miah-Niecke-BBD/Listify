package org.listify.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.listify.dto.DueTasksDTO;
import org.listify.dto.TeamInfoDTO;
import org.listify.dto.TeamMemberInfoDTO;
import org.listify.dto.TeamProjectsDTO;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.exception.UnauthorizedException;
import org.listify.model.*;
import org.listify.repo.TeamMembersRepository;
import org.listify.repo.TeamsRepository;
import org.listify.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    UsersRepository usersRepository;

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

    public List<TeamMemberInfoDTO> getTeamMembersByTeamID(Long teamID, Long userID) {
        findATeamByUserID(userID, teamID);
        List<TeamMembers> teamMembers = teamMembersRepository.findByTeamID(teamID);

        if (teamMembers.isEmpty()) {
            throw new NotFoundException("No members for this team was found " + teamID);
        }

        List<TeamMemberInfoDTO> teamMembersInfo = teamMembers.stream().map(member -> {

            Users user = usersRepository.findByUserID(member.getUserID());

            return new TeamMemberInfoDTO(member.getUserID(), user.getGitHubID(), member.isTeamLeader());
        }).collect(Collectors.toList());

        return teamMembersInfo;
    }

    public boolean isTeamLeader(Long teamID, Long userID) {
        return teamMembersRepository.existsByTeamIDAndUserIDAndIsTeamLeaderTrue(teamID, userID);
    }

    @Transactional
    public Long addTeam(Long userID, String teamName) {
        if (userID == null) {
            throw new UnauthorizedException("User is not authenticated. Please log in");
        }
        if (teamName.length() > 100) {
            throw new IllegalArgumentException("Team name cannot exceed 100 characters.");
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
            throw new ForbiddenException("User can't assign members to their personal team");
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
            throw new ForbiddenException("User can't delete their personal team");
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
    public TeamInfoDTO updateTeamDetails(Long userID, Long teamID, String newTeamName) {
        findATeamByUserID(userID, teamID);
        boolean isTeamLeader = isTeamLeader(teamID, userID);

        if (newTeamName.length() > 100) {
            throw new IllegalArgumentException("Team name cannot exceed 100 characters.");
        }

        if (!isTeamLeader) {
            throw new ForbiddenException("Only team leaders can update teams details");
        }

        teamsRepository.updateTeamDetails(userID, teamID, newTeamName);
        entityManager.flush();
        entityManager.clear();

        Teams updatedTeam = findATeamByUserID(userID, teamID);

        return new TeamInfoDTO(updatedTeam.getTeamName(), updatedTeam.getCreatedAt(), updatedTeam.getUpdatedAt());
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

    public List<TeamProjectsDTO> findProjectsByTeamID(Long userID, Long teamID) {
        findATeamByUserID(userID, teamID);
        List<Projects> projects = teamsRepository.findTeamProjects(userID, teamID);

        List<TeamProjectsDTO> teamProjectsDTOs = projects.stream().map((project) ->
                new TeamProjectsDTO(project.getProjectID(), project.getProjectName())
        ).collect(Collectors.toList());

        return teamProjectsDTOs;
    }

    public List<DueTasksDTO> findTeamsDueTasks(Long userID, Long teamID) {
        findATeamByUserID(userID, teamID);
        List<Tasks> tasks = teamsRepository.findTeamsDueTasks(userID, teamID);

        List<DueTasksDTO> dueTasksDTOs = tasks.stream().map((task) ->
                new DueTasksDTO(task.getTaskID(), task.getTaskName(), task.getDueDate())
        ).collect(Collectors.toList());

        return dueTasksDTOs;
    }

}
