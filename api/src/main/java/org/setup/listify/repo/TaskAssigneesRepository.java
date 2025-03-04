package org.setup.listify.repo;

import org.setup.listify.model.TaskAssignees;
import org.setup.listify.dto.UserAssignedTasksDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, Long> {

    @Query("SELECT new org.setup.listify.dto.UserAssignedTasksDTO(u.userID, u.gitHubID, t.taskID, t.taskName, " +
            "t.taskDescription, t.taskPriority, t.dueDate, t.dateCompleted, t.createdAt, t.updatedAt," +
            "s.sectionID, s.sectionName, p.projectID, p.projectName) " +
            "FROM Users u " +
            "JOIN TaskAssignees ta ON u.userID = ta.userID " +
            "JOIN Tasks t ON ta.taskID = t.taskID " +
            "JOIN Sections s ON t.sectionID = s.sectionID " +
            "JOIN Projects p ON s.projectID = p.projectID WHERE u.userID = :userID")
    List<UserAssignedTasksDTO> findAssignedTasksByUserID(@Param("userID") Long userID);


    @Procedure("listify.uspAssignUserToTask")
    void assignTaskToUser(
            @Param("userID") Long userID,
            @Param("taskID") Long taskID
    );

    @Procedure("listify.uspDeleteUserFromTask")
    void deleteUserFromTask(
            @Param("userID") Long userID,
            @Param("taskID") Long taskID,
            @Param("teamLeaderID") Long teamLeaderID
    );

    @Query(value = "SELECT TOP 1 * FROM listify.TaskAssignees ORDER BY taskAssigneeID DESC", nativeQuery = true)
    TaskAssignees findTopOrderByTaskIDDesc();

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Tasks t " +
            "JOIN Sections s ON t.sectionID = s.sectionID " +
            "JOIN Projects p ON s.projectID = p.projectID " +
            "JOIN ProjectAssignees pa1 ON p.projectID = pa1.projectID " +
            "JOIN ProjectAssignees pa2 ON p.projectID = pa2.projectID " +
            "WHERE t.taskID = :taskID " +
            "AND pa1.userID = :loggedInUserID " +
            "AND pa2.userID = :userID")
    boolean findTaskAndUsersInProject(@Param("taskID") Long taskID,
                                       @Param("loggedInUserID") Long loggedInUserID,
                                       @Param("userID") Long userID);


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ProjectAssignees pa1 " +
            "JOIN ProjectAssignees pa2 ON pa1.projectID = pa2.projectID " +
            "JOIN Projects p ON pa1.projectID = p.projectID " +
            "WHERE pa1.userID = :userID1 " +
            "AND pa2.userID = :userID2")
    boolean findUsersInSameProject(@Param("userID1") Long userID1,
                                   @Param("userID2") Long userID2);
}
