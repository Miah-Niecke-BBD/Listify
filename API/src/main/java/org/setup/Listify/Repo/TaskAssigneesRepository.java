package org.setup.Listify.Repo;

import org.setup.Listify.Model.TaskAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, Long> {

    @Query("SELECT t FROM TaskAssignees t WHERE t.userID = :userID")
    List<TaskAssignees> findAssignedTasksByUserID(@Param("userID") long userID);
}
