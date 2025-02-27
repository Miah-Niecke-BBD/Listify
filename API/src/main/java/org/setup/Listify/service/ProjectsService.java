package org.setup.Listify.service;

import org.setup.Listify.exception.ListNotFoundException;
import org.setup.Listify.exception.ProjectNotFoundException;
import org.setup.Listify.model.Projects;
import org.setup.Listify.model.Sections;
import org.setup.Listify.repo.ProjectsRepository;
import org.springframework.stereotype.Service;

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
}
