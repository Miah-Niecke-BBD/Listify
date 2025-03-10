
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



@Service
public class ProjectAssigneesService {

    private final ProjectAssigneesRepository repository;
    private final ProjectsRepository projectRepository;
    private final UsersRepository usersRepository;

    public ProjectAssigneesService(ProjectAssigneesRepository repository, ProjectsRepository projectRepository, UsersRepository usersRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
    }

    private ProjectAssigneeDTO convertToDTO(ProjectAssignees projectAssignee) {
        Projects project = projectRepository.findById(projectAssignee.getProjectID())
                .orElseThrow(() -> new NotFoundException("Project with ID " + projectAssignee.getProjectID() + " not found"));

        String githubID = "user" + projectAssignee.getUserID() + "_github";

        return new ProjectAssigneeDTO(
                project.getProjectName(),
                githubID
        );
    }


    public ProjectAssigneeDTO getProjectAssigneeById(Long projectID) {
        ProjectAssignees assignee = repository.findById(projectID)
                .orElseThrow(() -> new NotFoundException("Project assignee with ID " + projectID + " not found"));
        return convertToDTO(assignee);
    }

    public Long assignUserToProject(Long teamLeaderID, Long userID, Long projectID) {
        if (!usersRepository.existsById(String.valueOf(userID))) {
            throw new NotFoundException("User with ID " + userID + " does not exist");
        }

        if (!repository.isTeamLeader(teamLeaderID, projectID)) {
            throw new ForbiddenException("Only a team leader can assign users to a project");
        }

        repository.assignUserToProject(teamLeaderID, userID, projectID);
        ProjectAssignees newlyAssignedProject = repository.findTopOrderByProjectAssigneeIDDesc();
        return newlyAssignedProject != null ? newlyAssignedProject.getProjectAssigneeID() : null;
    }

    public void deleteUserFromProject(Long userID, Long projectID, Long teamLeaderID) {
        if (!repository.isTeamLeader(teamLeaderID, projectID)) {
            throw new ForbiddenException("Only a team leader can remove users from a project");
        }

        if (!repository.isUserAssignedToProject(userID, projectID)) {
            throw new NotFoundException("User with ID " + userID + " is not assigned to project " + projectID);
        }

        if (repository.isTeamLeader(userID, projectID)) {
            throw new ForbiddenException("A team leader cannot be removed from a project");
        }

        repository.deleteUserFromProject(userID, projectID, teamLeaderID);
    }
}
