package org.listify.repo;

import org.listify.model.Projects;
import org.listify.model.Sections;
import org.listify.model.Tasks;
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

    @Query(value = "SELECT tm.userID " +
            "FROM listify.Projects p " +
            "INNER JOIN listify.Teams t ON t.teamID = p.teamID " +
            "INNER JOIN listify.TeamMembers tm ON tm.teamID = t.teamID " +
            "WHERE p.projectID = :projectID AND tm.isTeamLeader = 1",
            nativeQuery = true)
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

    @Query(value = "SELECT " +
            "t.taskID, t.sectionID, t.parentTaskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dateCompleted, t.dueDate, t.createdAt, t.updatedAt " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "INNER JOIN listify.Tasks t ON v.taskID = t.taskID " +
            "WHERE v.userID = ?1 AND t.dueDate IS NOT NULL AND t.dateCompleted IS NULL AND v.projectID = ?2", nativeQuery = true)
    List<Tasks> findProjectsDueTasks(Long userID, Long projectID);
}
