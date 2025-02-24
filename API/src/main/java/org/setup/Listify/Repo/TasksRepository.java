package org.setup.Listify.Repo;

import org.setup.Listify.Model.Tasks;
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


    @Query("SELECT t FROM Tasks t WHERE t.sectionID = :id")
    List<Tasks> findTasksBySectionID(@Param("id") Long id);


    @Query("SELECT t FROM Tasks t WHERE t.sectionID = :sectionID AND t.taskName = :taskName AND t.taskPosition = :taskPosition")
    Tasks findBySectionIDAndTaskNameAndTaskPosition(int sectionID, String taskName, byte taskPosition);
}
