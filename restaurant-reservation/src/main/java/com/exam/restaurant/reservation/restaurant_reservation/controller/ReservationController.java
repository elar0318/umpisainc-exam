package com.exam.restaurant.reservation.restaurant_reservation.controller;

import com.exam.restaurant.reservation.restaurant_reservation.repository.model.ReservationRequest;
import com.exam.restaurant.reservation.restaurant_reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/umpisa/api/reservations")
public class ReservationController {

    private static final String SENDING_SMS = "Sending SMS...";

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationRequest> createReservation(@RequestBody ReservationRequest reservation) {
        ReservationRequest reserve = reservationService.createOrUpdateReservation(reservation);
        log.info(sendingNotif());
        log.info("Thank you {}, your reservation for {} guests has been created", reserve.getCustomerName(), reserve.getNumberOfGuests());
        return new ResponseEntity<>(reserve, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReservationRequest>> getAllReservations() {
        List<ReservationRequest> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationRequest> updateReservation(@PathVariable Long id, @RequestBody ReservationRequest reservation) {
        Optional<ReservationRequest> updateReservation = reservationService.getReservationById(id);
        if (updateReservation.isPresent()) {
            reservation.setId(id);
            reservation.setCustomerName(updateReservation.get().getCustomerName());
            reservation.setEmail(updateReservation.get().getEmail());
            reservation.setPhoneNumber(updateReservation.get().getPhoneNumber());
            ReservationRequest updatedReservation = reservationService.createOrUpdateReservation(reservation);
            log.info(sendingNotif());
            log.info("Thank you {}, your reservation for {} guests has been updated", reservation.getCustomerName(), reservation.getNumberOfGuests());
            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } else {
            log.info(sendingNotif());
            log.info("Sorry the reservation {} was not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Optional<ReservationRequest> existingReservation = reservationService.getReservationById(id);
        if (existingReservation.isPresent()) {
            reservationService.deleteReservation(id);
            log.info(sendingNotif());
            log.info("Your reservation {} has been cancelled", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info(sendingNotif());
            log.info("Sorry the reservation {} was not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private String sendingNotif() {
        return SENDING_SMS;
    }

}
