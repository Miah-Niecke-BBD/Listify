package org.setup.Listify.Repo;

import org.setup.Listify.Model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

    @Query("SELECT t FROM Tasks t WHERE t.sectionID = :id")
    List<Tasks> findTasksBySectionID(@Param("id") Long id);
}
