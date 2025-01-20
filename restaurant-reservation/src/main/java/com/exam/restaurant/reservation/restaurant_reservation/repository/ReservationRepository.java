package com.exam.restaurant.reservation.restaurant_reservation.repository;

import com.exam.restaurant.reservation.restaurant_reservation.repository.model.ReservationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationRequest, Long> {
    // Find reservations between two LocalDateTime values
    List<ReservationRequest> findByReservationDateBetween(LocalDateTime startTime, LocalDateTime endTime);
}
