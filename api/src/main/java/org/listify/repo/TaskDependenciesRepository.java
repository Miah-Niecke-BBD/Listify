package org.listify.repo;

import org.listify.model.TaskDependencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface TaskDependenciesRepository extends JpaRepository<TaskDependencies, Long> {

    @Query(value = "SELECT TOP 1 * FROM listify.TaskDependencies ORDER BY taskDependencyID DESC", nativeQuery = true)
    TaskDependencies findTopOrderByTaskIDDesc();

    @Procedure("listify.uspAssignTaskDependency")
    void newTaskDependency(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("taskID") Long taskID,
            @Param("dependentTaskID") Long dependentTaskID);


    @Procedure("listify.uspDeleteTaskDependency")
    void deleteTaskDependency(
            @Param("taskID") Long taskID,
            @Param("taskDependencyID") Long taskDependencyID,
            @Param("teamLeaderID") Long teamLeaderID);


    @Query("SELECT CASE WHEN COUNT(td) > 0 THEN true ELSE false END " +
            "FROM TaskDependencies td " +
            "WHERE td.taskID = :taskID AND td.dependentTaskID = :dependentTaskID")
    boolean isTaskDependent(@Param("taskID") Long taskID, @Param("dependentTaskID") Long dependentTaskID);
}
