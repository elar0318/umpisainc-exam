package com.exam.restaurant.reservation.restaurant_reservation.service;

import com.exam.restaurant.reservation.restaurant_reservation.repository.model.ReservationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    ReservationRequest createOrUpdateReservation(ReservationRequest reservation);

    List<ReservationRequest> getAllReservations();

    Optional<ReservationRequest> getReservationById(Long id);

    void deleteReservation(Long id);

    List<ReservationRequest> findReservationsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}
