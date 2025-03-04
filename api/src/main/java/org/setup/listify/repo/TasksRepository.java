package org.setup.listify.repo;

import org.setup.listify.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    @Procedure("listify.uspCreateTask")
    void createTask(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("projectID") Long projectID,
            @Param("sectionID") Long sectionID,
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("taskPriority") byte taskPriority,
            @Param("taskPosition") byte taskPosition
    );

    @Procedure("listify.uspCreateSubTask")
    void createSubTask(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("parentTaskID") Long parentTaskID,
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("sectionID") Long sectionID,
            @Param("dueDate") LocalDateTime dueDate
    );

    @Procedure("listify.uspUpdateTaskDetails")
    void updateTaskDetails(
            @Param("taskID") Long taskID,
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("newTaskName") String newTaskName,
            @Param("newTaskDescription") String newTaskDescription,
            @Param("newTaskPriority") Byte newTaskPriority,
            @Param("newDueDate") LocalDateTime newDueDate
    );

    @Procedure("listify.uspUpdateTaskPosition")
    void updateTaskPosition(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("taskID") Long taskID,
            @Param("newTaskPosition") Long newTaskPosition,
            @Param("sectionID") Long sectionID
    );


    @Procedure("listify.uspDeleteTask")
    void deleteTasksById(@Param("taskID") Long taskID, @Param("teamLeaderID") Long teamLeaderID);

    @Query(value = "SELECT TOP 1 * FROM listify.Tasks ORDER BY taskID DESC", nativeQuery = true)
    Tasks findTopOrderByTaskIDDesc();

    @Query("SELECT t FROM Tasks t WHERE t.parentTaskID = :parentTaskID")
    List<Tasks> getAllSubtasksOfTask(Long parentTaskID);

    @Query("SELECT t FROM Tasks t WHERE t.taskID = (SELECT td.dependentTaskID FROM TaskDependencies td WHERE td.taskID = :taskID)")
    Tasks findDependentTaskByTaskID(@Param("taskID") Long taskID);

    @Query(value = "SELECT " +
            "t.taskID, t.sectionID, t.parentTaskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dateCompleted, t.dueDate, t.createdAt, t.updatedAt " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "INNER JOIN listify.Tasks t ON v.taskID = t.taskID " +
            "WHERE v.userID = ?1 ", nativeQuery = true)
    List<Tasks> findTasksByUserID(Long userID);

    @Query(value = "SELECT " +
            "t.taskID, t.sectionID, t.parentTaskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dateCompleted, t.dueDate, t.createdAt, t.updatedAt " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "INNER JOIN listify.Tasks t ON v.taskID = t.taskID " +
            "WHERE v.userID = ?1 AND t.taskID = ?2", nativeQuery = true)
    Tasks getTaskById(Long userID, Long taskID);
}
