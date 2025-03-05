package org.setup.listify.repo;

import org.setup.listify.model.TaskAssignees;
import org.setup.listify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, Long> {

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
            "FROM Tasks t " +
            "JOIN Sections s ON t.sectionID = s.sectionID " +
            "JOIN Projects p ON s.projectID = p.projectID " +
            "JOIN ProjectAssignees pa ON p.projectID = pa.projectID " +
            "WHERE t.taskID = :taskID " +
            "AND pa.userID = :userID")
    boolean findUserAndTaskInProject(@Param("userID") Long userID, @Param("taskID") Long taskID);


    @Query("SELECT u " +
            "FROM Users u " +
            "JOIN TaskAssignees ta ON u.userID = ta.userID " +
            "WHERE ta.taskID = :taskID")
    List<Users> getUsersAssignedToTask(@Param("taskID") Long taskID);


    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Users u " +
            "JOIN TaskAssignees ta ON u.userID = ta.userID " +
            "WHERE ta.taskID = :taskID AND ta.userID = :userID")
    boolean isUserAssignedToTask(@Param("taskID") Long taskID, @Param("userID") Long userID);

}
