package org.setup.listify.repo;

import org.setup.listify.model.Projects;
import org.setup.listify.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {



    @Procedure("listify.uspCreateProject")
    void createProject(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("teamID") int teamID,
            @Param("projectName") String projectName,
            @Param("projectDescription") String projectDescription
    );

    @Procedure("listify.uspUpdateProjectDetails")
    void updateProject(
            @Param("projectID") int projectID,
            @Param("userID") int userID,
            @Param("projectName") String projectName,
            @Param("projectDescription") String projectDescription
    );

    @Procedure("listify.uspDeleteProject")
    void deleteProject(
            @Param("projectID") int projectID,
            @Param("teamLeaderID") int teamLeaderID
    );

    @Query(value = "SELECT TOP 1 * FROM listify.Projects ORDER BY projectID DESC", nativeQuery = true)
    Projects findTopOrderByProjectIDDesc();

    @Query("SELECT s FROM Sections s WHERE s.projectID = :id")
    List<Sections> findAllSectionsInProject(@Param("id") Long id);

        @Query(value = "SELECT " +
                "tm.teamID, " +
                "p.projectID, p.projectName, p.projectDescription, " +
                "s.sectionID, s.sectionName, s.sectionPosition, " +
                "t.taskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dueDate, " +
                "ta.userID AS assigneeUserID, " +
                "pl.priorityLabelName AS taskPriorityLabel " +
                "FROM listify.TeamMembers tm " +
                "JOIN listify.Projects p ON tm.teamID = p.teamID " +
                "JOIN listify.Sections s ON p.projectID = s.projectID " +
                "JOIN listify.Tasks t ON s.sectionID = t.sectionID " +
                "LEFT JOIN listify.TaskAssignees ta ON t.taskID = ta.taskID " +
                "LEFT JOIN listify.PriorityLabels pl ON t.taskPriority = pl.priorityLabelID " +
                "WHERE tm.userID = ?1 " +
                "AND p.projectID = ?2 " , nativeQuery = true)
    List<Object[]> getProjectDetails( Long userID, Long projectID);

}
