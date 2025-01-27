package tuul.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import tuul.demo.models.LoginRequest;
import tuul.demo.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import tuul.demo.repository.UserRepository;
import tuul.demo.models.RegistrationRequest;
import tuul.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            logger.info("Received registration request for email: {}", request.getEmail());
            User user = userService.registerUser(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent() && user.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            logger.info("Fetching all users");
            return ResponseEntity.ok(userRepository.findAll());
        } catch (Exception e) {
            logger.error("Failed to fetch users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Unable to fetch users"));
        }
    }

}

@Data
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
