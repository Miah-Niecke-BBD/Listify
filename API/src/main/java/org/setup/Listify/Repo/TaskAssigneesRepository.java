package org.setup.Listify.Repo;

import org.setup.Listify.Model.TaskAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, Long> {

    @Query("SELECT t FROM TaskAssignees t WHERE t.userID = :userID")
    List<TaskAssignees> findAssignedTasksByUserID(@Param("userID") long userID);

    @Procedure("listify.uspAssignUserToTask")
    void assignTaskToUser(
            @Param("userID") int userID,
            @Param("taskID") int taskID
    );

    @Procedure("listify.uspDeleteUserFromTask")
    void deleteUserFromTask(
            @Param("userID") int userID,
            @Param("taskID") int taskID,
            @Param("teamLeaderID") int teamLeaderID
    );

    @Query(value = "SELECT TOP 1 * FROM listify.TaskAssignees ORDER BY taskAssigneeID DESC", nativeQuery = true)
    TaskAssignees findTopOrderByTaskIDDesc();
}
