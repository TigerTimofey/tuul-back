package tuul.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tuul.demo.models.Vehicle;
import tuul.demo.service.VehicleService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:5173")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/pair")
    public ResponseEntity<?> pairVehicle(@RequestBody PairRequest request) {
        try {
            logger.info("Received pairing request: {}", request);
            Vehicle vehicle = vehicleService.pairVehicle(request.getVehicleCode(), request.getUserId());
            return ResponseEntity.ok(new PairResponse(vehicle.getId(), "Scooter paired successfully!"));
        } catch (Exception e) {
            logger.error("Pairing failed: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Failed to pair: " + e.getMessage()));
        }
    }
}

@Data
class PairRequest {
    private String vehicleCode;
    private String userId;
}

@Data
class PairResponse {
    private String vehicleId;
    private String message;

    public PairResponse(String vehicleId, String message) {
        this.vehicleId = vehicleId;
        this.message = message;
    }
}

@Data
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
