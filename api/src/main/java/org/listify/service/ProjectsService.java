package org.listify.service;

import org.listify.dto.*;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.Projects;
import org.listify.model.Sections;
import org.listify.model.Tasks;
import org.listify.repo.ProjectsRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final TeamsService teamsService;
    private final UserService userService;

    public ProjectsService(ProjectsRepository projectsRepository, TeamsService teamsService, UserService userService) {
        this.projectsRepository = projectsRepository;
        this.teamsService = teamsService;
        this.userService = userService;
    }


    public Projects getProjectById(Long projectID, Long userID) {

        isUserInTheProject(userID, projectID);

        return projectsRepository.findById(projectID)
                .orElseThrow(() -> new NotFoundException("projectID not found"));
    }


    public Long createProject(Long teamLeader, Long teamID,
                              String projectName, String projectDescription) {
        teamsService.findATeamByUserID(teamLeader, teamID);
        if (projectName.length() > 100) {
            throw new IllegalArgumentException("Project name cannot exceed 100 characters.");
        }

        if ((projectDescription != null) && projectDescription.length() > 500) {
            throw new IllegalArgumentException("Project description cannot exceed 500 characters");
        }


        projectsRepository.createProject(teamLeader, teamID, projectName, projectDescription);
        Projects newlyCreatedProject = projectsRepository.findTopOrderByProjectIDDesc();
        return newlyCreatedProject != null ? newlyCreatedProject.getProjectID() : null;
    }

    public void updateProject(Long projectID, Long userID, String projectName, String projectDescription) {
        if ((projectName != null) && projectName.length() > 100) {
            throw new IllegalArgumentException("Project name cannot exceed 100 characters.");
        }

        if ((projectDescription != null) && projectDescription.length() > 500) {
            throw new IllegalArgumentException("Project description cannot exceed 500 characters");
        }

        isUserInTheProject(userID, projectID);

        if (!projectsRepository.getTeamLeaderForProject(projectID).equals(userID)) {
            throw new ForbiddenException("User " + userID + " is not a team leader, only team leaders are allowed to delete projects");
        }

        projectsRepository.updateProject(projectID, userID, projectName, projectDescription);

    }

    public void isUserInTheProject(Long userID, Long projectID) {
        Long project = projectsRepository.isUserAssignedToProject(userID, projectID);
        if (project == null) {
            throw new ForbiddenException("User " + userID + " is not added to the Project " + projectID);
        }

    }

    public void deleteProjectById(Long projectID, Long teamLeaderID) {
        isUserInTheProject(teamLeaderID, projectID);
        if (!projectsRepository.getTeamLeaderForProject(projectID).equals(teamLeaderID)) {
            throw new ForbiddenException("User " + teamLeaderID + " is not a team leader, only team leaders are allowed to delete projects");
        }

        if (getProjectById(projectID, teamLeaderID).getCreatedAt().equals(userService.getUserByUserID(teamLeaderID).getCreatedAt())) {
            throw new ForbiddenException("Can't delete initial personal projects");
        }
        projectsRepository.deleteProject(projectID, teamLeaderID);
    }

    public List<ProjectSectionsDTO> getAllSectionsByProjectID(Long userID, Long projectID) {
        isUserInTheProject(userID, projectID);

        List<Sections> sections = projectsRepository.findAllSectionsInProject(projectID);

        return sections.stream().map((section) ->
                new ProjectSectionsDTO(section.getSectionID(), section.getSectionName(),
                        section.getSectionPosition(), section.getCreatedAt(), section.getUpdatesAt())).toList();
    }

    public List<DueTasksDTO> getAllProjectDueTasks(Long userID, Long projectID) {
        isUserInTheProject(userID, projectID);

        List<Tasks> tasks = projectsRepository.findProjectsDueTasks(userID, projectID);

        return tasks.stream().map((task) ->
                new DueTasksDTO(task.getTaskID(), task.getTaskName(), task.getDueDate())
        ).collect(Collectors.toList());
    }
}
