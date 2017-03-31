package gr.uoa.di.digibid.persist.repository;

import gr.uoa.di.digibid.persist.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user where user.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select user from User user where user.username = ?1")
    Optional<User> findByUsername(String username);
}
