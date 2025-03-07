package org.listify.service;

import jakarta.transaction.Transactional;
import org.listify.dto.ProjectAssigneeDTO;
import org.listify.exception.AssignedProjectNotFoundException;
import org.listify.exception.ListNotFoundException;
import org.listify.exception.NotFoundException;
import org.listify.model.ProjectAssignees;
import org.listify.model.Projects;
import org.listify.repo.ProjectAssigneesRepository;
import org.listify.repo.ProjectsRepository;
import org.springframework.dao.DataAccessException;
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
                .orElseThrow(() -> new AssignedProjectNotFoundException(projectAssignee.getProjectID()));


        String githubID = "user" + projectAssignee.getUserID() + "_github";

        return new ProjectAssigneeDTO(
                project.getProjectName(),
                githubID
        );
    }

    public List<ProjectAssigneeDTO> getAllProjectAssignees(Long userID) {
        List<ProjectAssignees> projectAssigneesList = repository.findAll();
        if (projectAssigneesList.isEmpty()) {
            throw new ListNotFoundException("No projects assigned");
        }
        return projectAssigneesList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectAssigneeDTO getProjectAssigneeById(Long projectAssigneeID) {
        ProjectAssignees assignee = repository.findById(projectAssigneeID)
                .orElseThrow(() -> new AssignedProjectNotFoundException(projectAssigneeID));
        return convertToDTO(assignee);
    }

    public List<ProjectAssigneeDTO> getProjectsAssignedToSpecificUser(Long userID) {
        List<ProjectAssignees> projectsAssignedToUser = repository.findProjectsAssignedToUser(userID);
        if (projectsAssignedToUser.isEmpty()) {
            throw new NotFoundException("projects assigned to user not found");
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
