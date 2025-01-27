package tuul.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import tuul.demo.models.Reservation;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
