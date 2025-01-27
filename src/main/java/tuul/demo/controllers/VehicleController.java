package tuul.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tuul.demo.models.User;
import tuul.demo.models.Vehicle;
import tuul.demo.service.VehicleService;
import tuul.demo.service.UserService;
import lombok.Data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import com.google.firebase.auth.*;
import tuul.demo.models.ErrorResponse;
import tuul.demo.models.PairRequest;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:5173")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService; // Add this

    @PostMapping("/pair")
    public ResponseEntity<?> pairVehicle(@RequestBody PairRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        try {
            logger.info("Received pairing request: {}", request);

            // Extract Firebase token
            String token = bearerToken.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            // Verify the user ID matches the token
            if (!decodedToken.getUid().equals(request.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User ID mismatch"));
            }

            // Create or get user
            User user = userService.findOrCreateUser(decodedToken.getUid(), decodedToken.getEmail());

            // Now proceed with vehicle pairing using the user's Firebase UID
            Vehicle pairedVehicle = vehicleService.pairVehicle(
                    request.getVehicleCode(),
                    user.getFirebaseUid());

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
