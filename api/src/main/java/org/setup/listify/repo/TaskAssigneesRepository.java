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
}
