package org.listify.service;

import org.listify.dto.ProjectAssigneeDTO;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.ProjectAssignees;
import org.listify.model.Projects;
import org.listify.repo.ProjectAssigneesRepository;
import org.listify.repo.ProjectsRepository;
import org.listify.repo.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectAssigneesService {

    private final ProjectAssigneesRepository repository;
    private final ProjectsRepository projectRepository;
    private final UsersRepository usersRepository;
    private final UserService userService;

    public ProjectAssigneesService(ProjectAssigneesRepository repository,
                                   ProjectsRepository projectRepository,
                                   UsersRepository usersRepository,
                                   UserService userService
    ) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
        this.userService = userService;
    }

    private ProjectAssigneeDTO convertToDTO(ProjectAssignees projectAssignee) {
        Projects project = projectRepository.findById(projectAssignee.getProjectID())
                .orElseThrow(() -> new NotFoundException("Project with ID " + projectAssignee.getProjectID() + " not found"));

        String githubID = "user" + projectAssignee.getUserID() + "_github";

        return new ProjectAssigneeDTO(githubID);
    }

    public List<ProjectAssigneeDTO> getAllProjectsAssignees(Long projectID) {
        if (!projectRepository.existsById(projectID)) {
            throw new NotFoundException("Project with ID " + projectID + " not found");
        }

        List<ProjectAssignees> projectAssigneesList = repository.findProjectsAssignedUsers(projectID);
        if (projectAssigneesList.isEmpty()) {
            throw new NotFoundException("No users assigned to project with ID: " + projectID);
        }
        return projectAssigneesList.stream()
                .map(assignee -> {
                    String githubID = usersRepository.findById(assignee.getUserID().toString())
                            .map(user -> user.getGitHubID())
                            .orElse("Unknown");

                    return new ProjectAssigneeDTO( githubID);
                })
                .collect(Collectors.toList());
    }

    public Long assignUserToProject(Long teamLeaderID, String githubID, Long projectID) {
        Long userID = userService.getUserIDFromGithubID(githubID);

        if (!usersRepository.existsById(String.valueOf(userID))) {
            throw new NotFoundException("User with ID " + userID + " does not exist");
        }
        if (!projectRepository.existsById(projectID)) {
            throw new NotFoundException("Project with ID " + projectID + " not found");
        }
        if (!repository.isTeamLeader(teamLeaderID, projectID)) {
            throw new ForbiddenException("Only a team leader can assign users to a project");
        }
        repository.assignUserToProject(teamLeaderID, userID, projectID);
        return userID;
    }

    public void deleteUserFromProject(String githubID, Long projectID, Long teamLeaderID) {
        Long userID = userService.getUserIDFromGithubID(githubID);
        if (!projectRepository.existsById(projectID)) {
            throw new NotFoundException("Project with ID " + projectID + " not found");
        }
        if (!repository.isTeamLeader(teamLeaderID, projectID)) {
            throw new ForbiddenException("Only a team leader can remove users from a project");
        }
        if (repository.findProjectsAssignedUsers(projectID).stream()
                .noneMatch(pa -> pa.getUserID().equals(userID))) {
            throw new NotFoundException("User with ID " + userID + " is not assigned to project " + projectID);
        }
        if (userID.equals(teamLeaderID)) {
            throw new ForbiddenException("A team leader cannot remove themselves from a project");
        }
        repository.deleteUserFromProject(userID, projectID, teamLeaderID);
    }
}
