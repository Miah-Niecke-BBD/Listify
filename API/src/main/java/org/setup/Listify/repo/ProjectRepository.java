package org.setup.Listify.repo;

import org.setup.Listify.model.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Projects, Long> {

    @Procedure(procedureName = "uspCreateProject")
    void createProject(Long teamID, String projectName, String projectDescription);

    @Procedure(procedureName = "uspAssignUserToProject")
    void assignUserToProject(Long userID, Long projectID, Long teamID);

    @Procedure(procedureName = "uspDeleteProject")
    void deleteProject(Long projectID);

    @Procedure(procedureName = "uspUpdateProject")
    void updateProject(Long projectID, String projectName, String projectDescription, String updatedAt);

}

