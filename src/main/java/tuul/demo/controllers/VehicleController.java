package tuul.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tuul.demo.models.User;
import tuul.demo.models.Vehicle;
import tuul.demo.service.VehicleService;
import tuul.demo.service.UserService;
import tuul.demo.service.ReservationService;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import com.google.firebase.auth.*;
import tuul.demo.models.ErrorResponse;
import tuul.demo.models.PairRequest;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Operation(summary = "Pair a vehicle with a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle successfully paired"),
            @ApiResponse(responseCode = "403", description = "User ID mismatch"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/pair")
    public ResponseEntity<?> pairVehicle(@RequestBody PairRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        try {
            logger.info("Received pairing request: {}", request);

            String token = bearerToken.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            if (!decodedToken.getUid().equals(request.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User ID mismatch"));
            }

            User user = userService.findOrCreateUser(decodedToken.getUid(), decodedToken.getEmail());

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

    @Operation(summary = "Create a new vehicle")
    @PostMapping("/create")
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Toggle vehicle power state")
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

    @Operation(summary = "Update vehicle location")
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

    @Operation(summary = "Unpair a vehicle")
    @PostMapping("/{id}/unpair")
    public ResponseEntity<?> unpairVehicle(@PathVariable String id) {
        try {
            logger.info("Received unpair request for vehicle: {}", id);
            Vehicle vehicle = vehicleService.unpairVehicle(id);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            logger.error("Unpairing failed: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Toggle vehicle lock state")
    @PostMapping("/{id}/lock")
    public ResponseEntity<?> toggleLock(
            @PathVariable String id,
            @RequestParam boolean lock) {
        try {
            Vehicle vehicle = vehicleService.toggleLock(id, lock);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get all vehicles")
    @GetMapping("/all")
    public ResponseEntity<?> getAllScooters() {
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            logger.error("Failed to retrieve vehicles: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get vehicle by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getScooterById(@PathVariable String id) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);
            if (vehicle == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Vehicle not found"));
            }
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            logger.error("Failed to retrieve vehicle: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get current cost of a vehicle")
    @GetMapping("/{id}/current-cost")
    public ResponseEntity<?> getCurrentCost(@PathVariable String id) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);
            if (vehicle == null || !vehicle.isPaired()) {
                return ResponseEntity.ok(Map.of("cost", 0.0));
            }

            double currentCost = reservationService.calculateCurrentCostFromPair(vehicle.getReservationStartTime());
            return ResponseEntity.ok(Map.of("cost", currentCost));
        } catch (Exception e) {
            logger.error("Failed to calculate current cost: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Delete a vehicle")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String id) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);
            if (vehicle == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Vehicle not found"));
            }

            vehicleService.deleteVehicle(id);

            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete vehicle: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

}
