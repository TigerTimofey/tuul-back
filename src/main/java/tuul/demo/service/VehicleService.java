package tuul.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuul.demo.models.Vehicle;
import tuul.demo.models.User;
import tuul.demo.models.Reservation;
import tuul.demo.repository.VehicleRepository;
import tuul.demo.repository.UserRepository;
import tuul.demo.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

@Service
public class VehicleService {
    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Vehicle pairVehicle(String vehicleCode, String userId) {
        validatePairingRequest(vehicleCode, userId);

        Vehicle vehicle = findAndValidateVehicle(vehicleCode, userId);
        User user = findAndValidateUser(userId);

        return completePairing(vehicle, user);
    }

    private void validatePairingRequest(String vehicleCode, String userId) {
        if (vehicleCode == null || vehicleCode.trim().isEmpty()) {
            throw new RuntimeException("Vehicle code is required");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new RuntimeException("User ID is required");
        }
    }

    private Vehicle findAndValidateVehicle(String vehicleCode, String userId) {
        Vehicle vehicle = vehicleRepository.findByVehicleCode(vehicleCode)
                .orElseThrow(() -> new RuntimeException("Invalid vehicle code"));

        if (vehicle.isPaired()) {
            if (vehicle.getUserId() != null && vehicle.getUserId().equals(userId)) {
                return vehicle;
            }
            throw new RuntimeException("Vehicle is already paired with another user");
        }
        return vehicle;
    }

    private User findAndValidateUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Vehicle completePairing(Vehicle vehicle, User user) {
        vehicle.setUserId(user.getId());
        vehicle.setPaired(true);
        vehicle.setStatus("paired");

        user.setActiveVehicleId(vehicle.getId());
        userRepository.save(user);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        logger.info("Successfully paired vehicle: {} with user: {}",
                vehicle.getVehicleCode(), user.getId());

        return savedVehicle;
    }

    public Reservation startRide(String vehicleId, String userId,
            double startLatitude, double startLongitude) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!vehicle.getUserId().equals(userId)) {
            throw new RuntimeException("Vehicle not paired with this user");
        }

        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now());
        reservation.setStartLatitude(startLatitude);
        reservation.setStartLongitude(startLongitude);

        return reservationRepository.save(reservation);
    }
}
