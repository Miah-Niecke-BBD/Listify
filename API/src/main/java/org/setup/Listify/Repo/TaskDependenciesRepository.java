package org.setup.Listify.Repo;

import org.setup.Listify.Model.TaskDependencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface TaskDependenciesRepository extends JpaRepository<TaskDependencies, Long> {

    @Procedure("listify.uspAssignTaskDependency")
    void newTaskDependency(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("taskID") int taskID,
            @Param("dependentTaskID") int dependentTaskID);


    @Procedure("listify.uspDeleteTaskDependency")
    void deleteTaskDependencyByDependencyID(
            @Param("taskDependencyID") int taskDependencyID,
            @Param("taskID") int taskID,
            @Param("teamLeaderID") int teamLeaderID);
}
