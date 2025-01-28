package tuul.demo.service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public double calculateCurrentCostFromPair(LocalDateTime pairTime) {
        if (pairTime == null) {
            return 0.0;
        }

        long durationSeconds = Duration.between(pairTime, LocalDateTime.now()).getSeconds();
        double minutes = durationSeconds / 60.0;
        double cost = 1.0;

        if (minutes <= 10) {
            cost += minutes * 0.5;
        } else {
            cost += (10 * 0.5) + ((minutes - 10) * 0.3);
        }

        return Math.round(cost * 100.0) / 100.0;
    }
}
