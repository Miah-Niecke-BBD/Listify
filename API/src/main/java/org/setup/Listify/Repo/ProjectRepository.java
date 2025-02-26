package org.setup.Listify.Repository;

import org.setup.Listify.Model.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Projects, Long> {

    @Procedure(procedureName = "uspCreateProject")
    void createProject(Long teamLeaderID,Long teamID, String projectName, String projectDescription, );

    @Procedure(procedureName = "uspAssignUserToProject")
    void assignUserToProject(Long Long teamLeaderID, Long userID, Long projectID, Long teamID);

    @Procedure(procedureName = "uspDeleteProject")
    void deleteProject(Long projectID, Long teamLeaderID);

    @Procedure(procedureName = "uspUpdateProjectDetails")
    void updateProject(Long projectID, Long userID, String newProjectDescription, String newProjectName);

}
