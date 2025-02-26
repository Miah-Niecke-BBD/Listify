package org.setup.Listify.repo;
import org.setup.Listify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByGitHubID(String gitHubID);

    boolean existsByGitHubID(String gitHubID);

    boolean existsByUserID(Long userID);

    @Procedure( "Listify.uspCreateUser")
    void createUser(@Param("gitHubID") String githubID);

    @Procedure("Listify.uspRemoveUser")
    void deleteUserByUserID(@Param("userID") Long userID);

}
