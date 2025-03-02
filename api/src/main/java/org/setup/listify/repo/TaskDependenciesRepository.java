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
            @Param("teamLeaderID") int teamLeaderID,
            @Param("taskID") int taskID,
            @Param("dependentTaskID") int dependentTaskID);


    @Procedure("listify.uspDeleteTaskDependency")
    void deleteTaskDependency(
            @Param("taskID") int taskID,
            @Param("taskDependencyID") int taskDependencyID,
            @Param("teamLeaderID") int teamLeaderID);
}
