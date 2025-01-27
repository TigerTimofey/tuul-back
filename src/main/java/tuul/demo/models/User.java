package tuul.demo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String activeVehicleId;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private String firebaseUid;
}
