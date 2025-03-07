package org.listify.service;

import org.listify.dto.ProjectAssigneeDTO;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.ProjectAssignees;
import org.listify.model.Projects;
import org.listify.repo.ProjectAssigneesRepository;
import org.listify.repo.ProjectsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectAssigneesService {

    private final ProjectAssigneesRepository repository;
    private final ProjectsRepository projectRepository;

    public ProjectAssigneesService(ProjectAssigneesRepository repository, ProjectsRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    private ProjectAssigneeDTO convertToDTO(ProjectAssignees projectAssignee) {

        Projects project = projectRepository.findById(projectAssignee.getProjectID())
                .orElseThrow(() -> new NotFoundException("Project assignee with id " + projectAssignee.getProjectID() + " not found"));


        String githubID = "user" + projectAssignee.getUserID() + "_github";

        return new ProjectAssigneeDTO(
                project.getProjectName(),
                githubID
        );
    }

    public List<ProjectAssigneeDTO> getAllProjectAssignees(Long userID) {
        List<ProjectAssignees> projectAssigneesList = repository.findAll();
        if (projectAssigneesList.isEmpty()) {
            throw new ForbiddenException("No projects assigned");
        }
        return projectAssigneesList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectAssigneeDTO getProjectAssigneeById(Long projectAssigneeID) {
        ProjectAssignees assignee = repository.findById(projectAssigneeID)
                .orElseThrow(() -> new NotFoundException("Project assignee id " + projectAssigneeID + " not found"));
        return convertToDTO(assignee);
    }

    public List<ProjectAssigneeDTO> getProjectsAssignedToSpecificUser(Long userID) {
        List<ProjectAssignees> projectsAssignedToUser = repository.findProjectsAssignedToUser(userID);
        if (projectsAssignedToUser.isEmpty()) {
            throw new ForbiddenException("projects assigned to user not found");
        }
        return projectsAssignedToUser.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Long assignUserToProject(Long teamLeaderID, Long userID, Long projectID) {
        repository.assignUserToProject(teamLeaderID, userID, projectID);
        ProjectAssignees newlyAssignedProject = repository.findTopOrderByProjectAssigneeIDDesc();
        return newlyAssignedProject != null ? newlyAssignedProject.getProjectAssigneeID() : null;
    }


    public void deleteUserFromProject(Long userID, Long projectID, Long teamLeaderID) {
        repository.deleteUserFromProject(userID, projectID, teamLeaderID);
    }
}