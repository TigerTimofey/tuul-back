package tuul.demo.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private double cost;

}
