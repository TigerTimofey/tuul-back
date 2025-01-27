package tuul.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id;
    private String vehicleCode;
    private String userId;
    private boolean paired;
    private String status;

    public Vehicle() {
        this.paired = false;
        this.status = "available";
    }

    public boolean isPaired() {
        return this.paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
