package org.setup.listify.repo;
import org.setup.listify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    Users findByGitHubID(String gitHubID);

    Users findByUserID(Long userID);

    boolean existsByGitHubID(String gitHubID);

    boolean existsByUserID(Long userID);

    @Procedure( "listify.uspCreateUser")
    void createUser(@Param("gitHubID") String githubID);

    @Procedure("listify.uspRemoveUser")
    void deleteUserByUserID(@Param("userID") Long userID);

}
