package org.setup.listify.service;

import org.setup.listify.exception.ListNotFoundException;
import org.setup.listify.exception.AssignedProjectNotFoundException;
import org.setup.listify.model.ProjectAssignees;
import org.setup.listify.repo.ProjectAssigneesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectAssigneesService {

    private final ProjectAssigneesRepository repository;

    public ProjectAssigneesService(ProjectAssigneesRepository repository) {
        this.repository = repository;
    }

    public List<ProjectAssignees> getAllProjectAssignees() {
        List<ProjectAssignees> projectAssigneesList = repository.findAll();
        if (projectAssigneesList.isEmpty()) {
            throw new ListNotFoundException("Project Assignees");
        }
        return projectAssigneesList;
    }

    public ProjectAssignees getProjectAssigneeById(Long projectAssigneeID) {
        return repository.findById(projectAssigneeID)
                .orElseThrow(() -> new AssignedProjectNotFoundException(projectAssigneeID));
    }

    public List<ProjectAssignees> getProjectsAssignedToSpecificUser(Long userID) {
        List<ProjectAssignees> projectsAssignedToUser = repository.findProjectsAssignedToUser(userID);
        if (projectsAssignedToUser.isEmpty()) {
            throw new ListNotFoundException("projects assigned to user");
        }
        return projectsAssignedToUser;
    }

    public Long assignUserToProject(int teamLeaderID, int userID, int projectID) {
        repository.assignUserToProject(teamLeaderID, userID, projectID);

        ProjectAssignees newlyAssignedProject = repository.findTopOrderByProjectAssigneeIDDesc();
        return newlyAssignedProject != null ? newlyAssignedProject.getProjectAssigneeID() : null;
    }

    public void deleteUserFromProject(int userID, int projectID, int teamLeaderID) {
        repository.deleteUserFromProject(userID, projectID, teamLeaderID);
    }
}
