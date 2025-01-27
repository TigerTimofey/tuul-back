package tuul.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import tuul.demo.models.Vehicle;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
}
