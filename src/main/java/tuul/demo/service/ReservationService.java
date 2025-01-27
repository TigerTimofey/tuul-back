package tuul.demo.service;

import java.time.Duration;
import org.springframework.stereotype.Service;

import tuul.demo.models.Reservation;

@Service
public class ReservationService {

    public double calculateCost(Reservation reservation) {
        long durationMinutes = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
        double cost = 1.0;

        if (durationMinutes <= 10) {
            cost += durationMinutes * 0.5;
        } else {
            cost += (10 * 0.5) + ((durationMinutes - 10) * 0.3);
        }

        return cost;
    }
}
