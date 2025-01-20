package com.exam.restaurant.reservation.restaurant_reservation.util;


import com.exam.restaurant.reservation.restaurant_reservation.repository.model.ReservationRequest;
import com.exam.restaurant.reservation.restaurant_reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationScheduleReminderUtil {

    private static final Logger log = LoggerFactory.getLogger(ReservationScheduleReminderUtil.class);

    private final ReservationService reservationService;

    public ReservationScheduleReminderUtil(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "0 * * * * *") // Runs every minute
    public void checkForUpcomingReservations() {

        LocalDateTime now = LocalDateTime.now();

        // Calculate the time 4 hours ahead
        LocalDateTime fourHoursLater = now.plusHours(4);

        // Find reservations happening in the next 4 hours
        List<ReservationRequest> upcomingReservations = reservationService.findReservationsByTimeRange(now, fourHoursLater);

        // Log reminders for those reservations
        for (ReservationRequest reservation : upcomingReservations) {
            log.info("Sending SMS...");
            log.info("Reminder: Reservation for {} is in 4 hours at {}",
                    reservation.getCustomerName(), reservation.getReservationDate());
        }
    }
}