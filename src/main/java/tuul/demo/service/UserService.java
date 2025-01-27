package tuul.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuul.demo.models.RegistrationRequest;
import tuul.demo.models.User;
import tuul.demo.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User registerUser(RegistrationRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());

        User savedUser = userRepository.save(user);
        logger.info("Successfully registered user with email: {}", request.getEmail());

        return savedUser;
    }

    public User findOrCreateUser(String firebaseUid, String email) {
        logger.info("Finding or creating user with Firebase UID: {}", firebaseUid);

        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setFirebaseUid(firebaseUid);
                    newUser.setEmail(email);
                    newUser.setCreatedAt(LocalDateTime.now());
                    newUser.setLastLoginAt(LocalDateTime.now());

                    User savedUser = userRepository.save(newUser);
                    logger.info("Created new user with Firebase UID: {}", firebaseUid);
                    return savedUser;
                });
    }
}
