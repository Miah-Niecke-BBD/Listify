package org.setup.Listify.Repo;

import org.setup.Listify.Model.TaskDependencies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDependenciesRepository extends JpaRepository<TaskDependencies, Long> {

}
