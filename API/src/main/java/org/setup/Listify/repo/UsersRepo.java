package org.setup.Listify.repo;
import org.setup.Listify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByGitHubID(String gitHubID);

    boolean existsByGitHubID(String gitHubID);

    boolean existsByUserID(Integer userID);

    @Procedure(procedureName = "Listify.uspCreateUser")
    void createUser(String githubID);

    @Procedure(procedureName = "Listify.uspRemoveUser")
    void deleteUserByUserID(Integer userID);

}
