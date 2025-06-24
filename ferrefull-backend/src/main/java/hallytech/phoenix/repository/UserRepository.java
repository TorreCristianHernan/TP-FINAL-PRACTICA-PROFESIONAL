package hallytech.phoenix.repository;

import hallytech.phoenix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<UserInfo> findByName(String username);
    Optional<User> findByEmailAndState(String email, String state);
}
