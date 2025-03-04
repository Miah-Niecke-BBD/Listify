package org.setup.listify.repo;

import org.setup.listify.model.TaskDependencies;
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
}
