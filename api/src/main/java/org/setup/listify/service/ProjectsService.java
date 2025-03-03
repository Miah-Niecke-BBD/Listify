package org.setup.listify.service;

import org.setup.listify.dto.ProjectOverviewDTO;
import org.setup.listify.dto.ProjectSectionDTO;
import org.setup.listify.dto.SectionTaskDTO;
import org.setup.listify.exception.ListNotFoundException;
import org.setup.listify.exception.ProjectNotFoundException;
import org.setup.listify.model.Projects;
import org.setup.listify.model.Sections;
import org.setup.listify.repo.ProjectsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectsService {

    private final ProjectsRepository repository;

    public ProjectsService(ProjectsRepository repository) {
        this.repository = repository;
    }

    public List<Projects> getAllProjects() {
        List<Projects> projects = repository.findAll();
        if (projects.isEmpty()) {
            throw new ListNotFoundException("projects");
        }
        return projects;
    }

    public Projects getProjectById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }


    public List<Sections> getAllSectionsInProject(Long id) {
        List<Sections> sections = repository.findAllSectionsInProject(id);
        if (sections.isEmpty()) {
            throw new ListNotFoundException("sections");
        }
        return sections;
    }

    public Long createProject(int teamLeader, int teamID,
                              String projectName, String projectDescription) {
        repository.createProject(teamLeader, teamID, projectName, projectDescription);

        Projects newlyCreatedProject = repository.findTopOrderByProjectIDDesc();
        return newlyCreatedProject != null ? newlyCreatedProject.getProjectID() : null;
    }

    public void updateProject(Long id, Integer userID, String projectName, String projectDescription) {
        repository.updateProject(id.intValue(), userID, projectName, projectDescription);
    }

    public void deleteProjectById(int id, int teamLeaderID) {
        repository.deleteProject(id, teamLeaderID);
    }


    public ProjectOverviewDTO getCompleteProjectDetails( Long userID, Long projectID) {
        List<Object[]> projectDetails = repository.getProjectDetails(userID, projectID);

        if (projectDetails.isEmpty()) {
            throw new ProjectNotFoundException(projectID);
        }

        ProjectOverviewDTO projectDTO = new ProjectOverviewDTO();
        List<ProjectSectionDTO> sectionDTOs = new ArrayList<>();
        ProjectSectionDTO currentSection = null;

        Long currentProjectID = null;
        for (Object[] row : projectDetails) {
            currentProjectID = (Long) row[1];
            Long sectionID = (Long) row[4];
            String sectionName = (String) row[5];
            Byte sectionPosition = (Byte) row[6];

            if (currentSection == null || !currentSection.getSectionID().equals(sectionID)) {
                if (currentSection != null) {
                    sectionDTOs.add(currentSection);
                }
                currentSection = new ProjectSectionDTO(sectionID, sectionName, sectionPosition, new ArrayList<>());
            }

            Long taskID = (Long) row[7];
            Long parentTaskID = (Long) row[8];
            String taskName = (String) row[9];
            String taskDescription = (String) row[10];
            Long taskPriority = (Long) row[11];
            Byte taskPosition = (Byte) row[12];
            LocalDateTime dueDate = (LocalDateTime) row[13];
            Long assigneeUserID = (Long) row[14];

            SectionTaskDTO taskDTO = new SectionTaskDTO(taskID, parentTaskID, taskName, taskDescription,
                    taskPriority, taskPosition, dueDate, assigneeUserID);

            currentSection.getTasks().add(taskDTO);
        }

        sectionDTOs.add(currentSection);

        projectDTO.setProjectID(currentProjectID);
        projectDTO.setProjectName((String) projectDetails.get(0)[2]);
        projectDTO.setSections(sectionDTOs);

        return projectDTO;
    }
}
