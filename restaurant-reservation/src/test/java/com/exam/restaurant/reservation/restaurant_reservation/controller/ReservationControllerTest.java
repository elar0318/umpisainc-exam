package com.exam.restaurant.reservation.restaurant_reservation.controller;

import com.exam.restaurant.reservation.restaurant_reservation.repository.model.ReservationRequest;
import com.exam.restaurant.reservation.restaurant_reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private ReservationRequest reservation;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

        // Set up a sample reservation for testing
        reservation = new ReservationRequest();
        reservation.setId(1L);
        reservation.setCustomerName("John Doe");
        reservation.setPhoneNumber("123-456-7890");
        reservation.setEmail("test@gmail.com");
        reservation.setNumberOfGuests(4);
        reservation.setReservationDate(LocalDateTime.parse("2025-01-25T18:00:00"));
    }

    // Test case for successfully creating a reservation
    @Test
    void createReservation_Success() throws Exception {
        when(reservationService.createOrUpdateReservation(any(ReservationRequest.class))).thenReturn(reservation);

        mockMvc.perform(MockMvcRequestBuilders.post("/umpisa/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\":\"John Doe\", \"phoneNumber\":\"123-456-7890\", \"email\":\"test@gmail.com\", \"numberOfGuests\":4, \"reservationDate\":\"2025-01-25T18:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(reservationService, times(1)).createOrUpdateReservation(any(ReservationRequest.class));
    }

    // Test case for updating an existing reservation
    @Test
    void updateReservation_Success() throws Exception {
        reservation.setNumberOfGuests(12);
        when(reservationService.getReservationById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationService.createOrUpdateReservation(any(ReservationRequest.class))).thenReturn(reservation);

        mockMvc.perform(MockMvcRequestBuilders.put("/umpisa/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"customerName\":\"John Doe\", \"phoneNumber\":\"123-456-7890\", \"email\":\"test@gmail.com\", \"numberOfGuests\":12, \"reservationDate\":\"2025-01-25T18:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests").value(12));

        verify(reservationService, times(1)).createOrUpdateReservation(any(ReservationRequest.class));
    }

    // Test case for deleting a reservation
    @Test
    void deleteReservation_Success() throws Exception {
        when(reservationService.getReservationById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationService.createOrUpdateReservation(any(ReservationRequest.class))).thenReturn(reservation);
        doNothing().when(reservationService).deleteReservation(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/umpisa/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteReservation(1L);
    }

}