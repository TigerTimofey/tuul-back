package tuul.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id;
    private double stateOfCharge;
    private double latitude;
    private double longitude;
    private String status;

}
