package com.exam.restaurant.reservation.restaurant_reservation.service;

import com.exam.restaurant.reservation.restaurant_reservation.repository.ReservationRepository;
import com.exam.restaurant.reservation.restaurant_reservation.repository.model.ReservationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public ReservationRequest createOrUpdateReservation(ReservationRequest reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationRequest> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<ReservationRequest> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Method to find reservations within a specific time range
    public List<ReservationRequest> findReservationsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return reservationRepository.findByReservationDateBetween(startTime, endTime);
    }
}
