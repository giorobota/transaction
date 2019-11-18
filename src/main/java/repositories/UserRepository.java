package repositories;

import entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByFirstName(String firstName);
    List<User> findAllByLastName(String lastName);
}
