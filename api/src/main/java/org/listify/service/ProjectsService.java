package org.listify.service;

import org.listify.dto.ProjectOverviewDTO;
import org.listify.dto.ProjectSectionDTO;
import org.listify.dto.SectionTaskDTO;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.Projects;
import org.listify.repo.ProjectsRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final TeamsService teamsService;

    public ProjectsService(ProjectsRepository projectsRepository, TeamsService teamsService) {
        this.projectsRepository = projectsRepository;

        this.teamsService = teamsService;
    }


    public Projects getProjectById(Long projectID , Long userID) {

        isUserInTheProject(userID, projectID);

        return projectsRepository.findById(projectID)
                .orElseThrow(() -> new NotFoundException("projectID not found"));
    }


    public Long createProject(Long teamLeader, Long teamID,
                              String projectName, String projectDescription) {
        teamsService.findATeamByUserID(teamLeader, teamID);
        projectsRepository.createProject(teamLeader, teamID, projectName, projectDescription);
        Projects newlyCreatedProject = projectsRepository.findTopOrderByProjectIDDesc();
        return newlyCreatedProject != null ? newlyCreatedProject.getProjectID() : null;
    }

    public void updateProject(Long projectID, Long userID, String projectName, String projectDescription) {


        isUserInTheProject(userID, projectID);
        projectsRepository.updateProject(projectID, userID, projectName, projectDescription);

    }

    public void isUserInTheProject(Long userID, Long projectID) {

         Long project = projectsRepository.isUserAssignedToProject(userID,projectID);

        if (project == null) {
            throw new ForbiddenException("User " + userID + " is not added to the Project " + projectID);
        }

    }

    public void deleteProjectById(Long projectID, Long teamLeaderID) {

        isUserInTheProject(teamLeaderID, projectID);
        projectsRepository.deleteProject(projectID, teamLeaderID);

    }


    public ProjectOverviewDTO getProjectDetails(Integer userID, Integer projectID) {
        List<Object[]> projectDetails = projectsRepository.getProjectDetails(userID, projectID);

        isUserInTheProject(userID.longValue(),projectID.longValue());

        if (projectDetails.isEmpty()) {
            throw new NotFoundException("No project details found");
        }

        ProjectOverviewDTO projectDTO = new ProjectOverviewDTO();
        projectDTO.setProjectID(projectID);
        projectDTO.setProjectName((String) projectDetails.get(0)[2]);

        Map<Integer, ProjectSectionDTO> sectionMap = new HashMap<>();

        for (Object[] row : projectDetails) {
            Integer sectionID = (Integer) row[4];

            if (!sectionMap.containsKey(sectionID)) {
                ProjectSectionDTO newSection = new ProjectSectionDTO(
                        sectionID,
                        (String) row[5],
                        ((Byte) row[6]).intValue(),
                        new ArrayList<>()
                );
                sectionMap.put(sectionID, newSection);
            }

            SectionTaskDTO taskDTO = new SectionTaskDTO(
                    (Integer) row[7],
                    null,
                    (String) row[8],
                    (String) row[9],
                    ((Byte) row[10]).intValue(),
                    ((Byte) row[11]).intValue(),
                    row[12] != null ? ((java.sql.Timestamp) row[12]).toLocalDateTime() : null,
                    (Integer) row[13]
            );

            sectionMap.get(sectionID).getTasks().add(taskDTO);
        }
        projectDTO.setSections(new ArrayList<>(sectionMap.values()));
        return projectDTO;
    }

}
