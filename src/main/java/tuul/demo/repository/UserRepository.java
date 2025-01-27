package tuul.demo.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import tuul.demo.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFirebaseUid(String firebaseUid);

    Optional<User> findByEmailOrFirebaseUid(String email, String firebaseUid);
}
