package org.listify.repo;

import org.listify.model.Projects;
import org.listify.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {

    @Procedure("listify.uspCreateProject")
    void createProject(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("teamID") Long teamID,
            @Param("projectName") String projectName,
            @Param("projectDescription") String projectDescription
    );

    @Query(value = "SELECT listify.fnGetTeamLeaderForProject(projectID)", nativeQuery = true)
    Long getTeamLeaderForProject(@Param("projectID") Long projectID);

    @Query(value = "SELECT userID FROM listify.ProjectAssignees pa WHERE pa.userID = ?1 AND pa.projectID = ?2", nativeQuery = true)
    Long isUserAssignedToProject(@Param("userID") Long userID, @Param("projectID") Long projectID);

    @Procedure("listify.uspUpdateProjectDetails")
    void updateProject(
            @Param("projectID") Long projectID,
            @Param("userID") Long userID,
            @Param("projectName") String projectName,
            @Param("projectDescription") String projectDescription
    );

    @Procedure("listify.uspDeleteProject")
    void deleteProject(
            @Param("projectID") Long projectID,
            @Param("teamLeaderID") Long teamLeaderID
    );

    @Query(value = "SELECT TOP 1 * FROM listify.Projects ORDER BY projectID DESC", nativeQuery = true)
    Projects findTopOrderByProjectIDDesc();

    @Query("SELECT s FROM Sections s WHERE s.projectID = :id")
    List<Sections> findAllSectionsInProject(@Param("id") Long id);

    @Query(value = "SELECT \n" +
            "    tm.teamID, \n" +
            "    p.projectID, p.projectName, p.projectDescription, \n" +
            "    s.sectionID, s.sectionName, CAST(s.sectionPosition AS TINYINT) AS sectionPosition, \n" +
            "    t.taskID, t.taskName, t.taskDescription, \n" +
            "    CAST(t.taskPriority AS TINYINT) AS taskPriority, \n" +
            "    t.taskPosition, t.dueDate, \n" +
            "    ta.userID AS assigneeUserID \n" +
            "FROM listify.TeamMembers tm\n" +
            "JOIN listify.Projects p ON tm.teamID = p.teamID \n" +
            "JOIN listify.Sections s ON p.projectID = s.projectID \n" +
            "JOIN listify.Tasks t ON s.sectionID = t.sectionID \n" +
            "LEFT JOIN listify.TaskAssignees ta ON t.taskID = ta.taskID \n" +
            "WHERE tm.userID = ?1 \n" +
            "AND p.projectID = ?2", nativeQuery = true)
    List<Object[]> getProjectDetails(@Param("userID") Integer userID, @Param("projectID") Integer projectID);

}
