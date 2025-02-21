package org.setup.Listify.Repo;

import org.setup.Listify.Model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

}
