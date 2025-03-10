package org.listify.repo;

import org.listify.dto.SimpleTaskDTO;
import org.listify.dto.SimpleUserDTO;
import org.listify.dto.ViewTaskDTO;
import org.listify.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    @Procedure("listify.uspCreateTask")
    void createTask(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("projectID") Long projectID,
            @Param("sectionID") Long sectionID,
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("taskPriority") Byte taskPriority,
            @Param("dueDate") OffsetDateTime dueDate
    );

    @Procedure("listify.uspCreateSubTask")
    void createSubTask(
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("parentTaskID") Long parentTaskID,
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("sectionID") Long sectionID,
            @Param("dueDate") OffsetDateTime dueDate
    );

    @Procedure("listify.uspUpdateTaskDetails")
    void updateTaskDetails(
            @Param("taskID") Long taskID,
            @Param("teamLeaderID") Long teamLeaderID,
            @Param("newTaskName") String newTaskName,
            @Param("newTaskDescription") String newTaskDescription,
            @Param("newTaskPriority") Long newTaskPriority,
            @Param("newDueDate") OffsetDateTime newDueDate,
            @Param("dateCompleted") OffsetDateTime dateCompleted
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

    @Query("SELECT new org.listify.dto.SimpleTaskDTO(t.taskID, t.taskName) FROM Tasks t "+
            "WHERE t.taskID = (SELECT td.dependentTaskID FROM TaskDependencies td WHERE td.taskID = :taskID)")
    SimpleTaskDTO getDependantTaskByTaskID(@Param("taskID") Long taskID);

    @Query(value = "SELECT " +
            "t.taskID, t.sectionID, t.parentTaskID, t.taskName, t.taskDescription, t.taskPriority, t.taskPosition, t.dateCompleted, t.dueDate, t.createdAt, t.updatedAt " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "INNER JOIN listify.Tasks t ON v.taskID = t.taskID " +
            "WHERE v.userID = ?1 ", nativeQuery = true)
    List<Tasks> findTasksByUserID(Long userID);


    @Query("SELECT new org.listify.dto.ViewTaskDTO(" +
            "t.taskID, t.taskName, t.taskDescription, pl.priorityLabelName, t.createdAt, " +
            "t.updatedAt, t.dueDate, t.dateCompleted, null, null) " +
            "FROM Tasks t " +
            "JOIN PriorityLabels pl ON t.taskPriority = pl.priorityLabelID " +
            "WHERE t.taskID = :taskID")
    ViewTaskDTO getTaskInformation(@Param("taskID") Long taskID);


    @Query("SELECT new org.listify.dto.SimpleUserDTO(u.userID, u.gitHubID) " +
            "FROM Users u " +
            "JOIN TaskAssignees ta ON u.userID = ta.userID " +
            "WHERE ta.taskID = :taskID")
    List<SimpleUserDTO> getUsersAssignedToTask(@Param("taskID") Long taskID);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM listify.vUserTeamProjectsTasks v " +
            "WHERE v.userID = :userID AND v.taskID = :taskID", nativeQuery = true)
    Integer userHasAccessToTask(Long userID, Long taskID);


    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM listify.Projects p " +
            "JOIN listify.TeamMembers tm ON p.teamID = tm.teamID " +
            "WHERE tm.userID = :userID " +
            "AND p.projectID = :projectID " +
            "AND tm.isTeamLeader = 1", nativeQuery = true)
    Integer userIsTeamLeader(Long userID, Long projectID);


    @Query("SELECT pl.priorityLabelName " +
            "FROM PriorityLabels pl " +
            "JOIN Tasks t ON t.taskPriority = pl.priorityLabelID " +
            "WHERE t.taskID = :taskID")
    String getPriorityLabelNameByTaskID(@Param("taskID") Long taskID);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END "+
                    "FROM listify.Tasks t "+
                    "WHERE t.sectionID = :sectionID AND t.taskID = :taskID", nativeQuery = true)
    Integer findTaskInSection(Long sectionID, Long taskID);
}
