package tuul.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tuul.demo.models.Vehicle;
import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    @Query("{ 'vehicleCode' : ?0 }")
    Optional<Vehicle> findByVehicleCode(String code);
}
