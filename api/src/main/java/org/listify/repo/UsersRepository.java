package org.listify.repo;
import org.listify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    Optional<Users> findByGitHubID(String gitHubID);

    Optional<Users> findByUserID(Long userID);

    boolean existsByGitHubID(String gitHubID);

    boolean existsByUserID(Long userID);

    @Procedure( "listify.uspCreateUser")
    void createUser(@Param("gitHubID") String githubID);

    @Procedure("listify.uspRemoveUser")
    void deleteUserByUserID(@Param("userID") Long userID);

}
