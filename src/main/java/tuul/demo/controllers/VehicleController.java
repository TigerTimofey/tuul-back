package tuul.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tuul.demo.models.Vehicle;
import tuul.demo.service.VehicleService;
import lombok.Data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

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

            // Validate request
            if (request.getVehicleCode() == null || request.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Vehicle code and user ID are required"));
            }

            // Get existing or create new vehicle
            Vehicle existingVehicle = vehicleService.findVehicleByCode(request.getVehicleCode());
            if (existingVehicle == null) {
                Vehicle newVehicle = new Vehicle();
                newVehicle.setVehicleCode(request.getVehicleCode());
                existingVehicle = vehicleService.createVehicle(newVehicle);
                logger.info("Created new vehicle with code: {}", request.getVehicleCode());
            }

            logger.info("Attempting to pair vehicle {} with user {}",
                    existingVehicle.getVehicleCode(), request.getUserId());

            Vehicle pairedVehicle = vehicleService.pairVehicle(
                    existingVehicle.getVehicleCode(),
                    request.getUserId());

            return ResponseEntity.ok(pairedVehicle);
        } catch (Exception e) {
            logger.error("Pairing failed: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/power")
    public ResponseEntity<?> togglePower(
            @PathVariable String id,
            @RequestParam boolean on) {
        try {
            Vehicle vehicle = vehicleService.togglePower(id, on);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(
            @PathVariable String id,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        try {
            Vehicle vehicle = vehicleService.updateLocation(id, latitude, longitude);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllScooters() {
        try {
            // Retrieve all scooters
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            logger.error("Failed to retrieve vehicles: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

}

@Data
class PairRequest {
    private String vehicleCode;
    private String userId;
}

@Data
class PairResponse extends Vehicle {
    private String message;

    public PairResponse(Vehicle vehicle, String message) {
        BeanUtils.copyProperties(vehicle, this);
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
