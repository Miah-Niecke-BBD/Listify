package org.setup.Listify.repo;

import org.setup.Listify.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    @Procedure("listify.uspCreateTask")
    void createTask(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("projectID") int projectID,
            @Param("sectionID") int sectionID,
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("taskPriority") byte taskPriority,
            @Param("taskPosition") byte taskPosition
    );

    @Procedure("listify.uspCreateSubTask")
    void createSubTask(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("parentTaskID") int parentTaskID,
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("sectionID") int sectionID,
            @Param("dueDate") LocalDateTime dueDate
    );

    @Procedure("listify.uspUpdateTaskDetails")
    void updateTaskDetails(
            @Param("taskID") int taskID,
            @Param("teamLeaderID") int teamLeaderID,
            @Param("newTaskName") String newTaskName,
            @Param("newTaskDescription") String newTaskDescription,
            @Param("newTaskPriority") Byte newTaskPriority,
            @Param("newDueDate") LocalDateTime newDueDate
    );

    @Procedure("listify.uspUpdateTaskPosition")
    void updateTaskPosition(
            @Param("teamLeaderID") int teamLeaderID,
            @Param("taskID") int taskID,
            @Param("newTaskPosition") int newTaskPosition,
            @Param("sectionID") int sectionID
    );


    @Procedure("listify.uspDeleteTask")
    void deleteTasksById(@Param("taskID") int taskID, @Param("teamLeaderID") int teamLeaderID);

    @Query(value = "SELECT TOP 1 * FROM listify.Tasks ORDER BY taskID DESC", nativeQuery = true)
    Tasks findTopOrderByTaskIDDesc();

    @Query("SELECT t FROM Tasks t WHERE t.parentTaskID = :parentTaskID")
    List<Tasks> getAllSubtasksOfTask(Long parentTaskID);

    @Query("SELECT t FROM Tasks t WHERE t.taskID = (SELECT td.dependentTaskID FROM TaskDependencies td WHERE td.taskID = :taskID)")
    Tasks findDependentTaskByTaskID(@Param("taskID") Long taskID);
}
