package es.codeurjc.mca.userms.repositories;

import es.codeurjc.mca.userms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNick(String nick);

    Optional<User> findByNick(String nick);

}
