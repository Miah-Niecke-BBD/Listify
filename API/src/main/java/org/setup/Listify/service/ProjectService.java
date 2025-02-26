package org.setup.Listify.service;

import org.setup.Listify.model.Projects;
import org.setup.Listify.repo.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Projects> getAllProjects() {
        try {
            return projectRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching projects", e);
        }
    }

    public Projects getProjectById(Long projectID) {
        try {
            return projectRepository.findById(projectID)
                    .orElseThrow(() -> new RuntimeException("Project with ID " + projectID + " not found"));
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching the project", e);
        }
    }

    public void createProject(Long teamID, String projectName, String projectDescription) {
        try {
            Projects newProject = new Projects();
            newProject.setTeamID(teamID);
            newProject.setProjectName(projectName);
            newProject.setProjectDescription(projectDescription);
            projectRepository.save(newProject);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while saving the new project", e);
        }
    }

    public void deleteProject(Long projectID) {
        try {
            if (!projectRepository.existsById(projectID)) {
                throw new RuntimeException("Project with ID " + projectID + " not found");
            }
            projectRepository.deleteById(projectID);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while deleting the project", e);
        }
    }

    public void updateProject(Long projectID, String projectName, String projectDescription) {
        try {
            Optional<Projects> existingProject = projectRepository.findById(projectID);
            if (existingProject.isEmpty()) {
                throw new RuntimeException("Project with ID " + projectID + " not found");
            }

            Projects projectToUpdate = existingProject.get();
            projectToUpdate.setProjectName(projectName);
            projectToUpdate.setProjectDescription(projectDescription);

            projectRepository.save(projectToUpdate);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while updating the project", e);
        }
    }
}