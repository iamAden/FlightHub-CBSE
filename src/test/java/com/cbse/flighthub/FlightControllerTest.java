package com.cbse.flighthub;

import com.cbse.flighthub.flight.FlightController;
import com.cbse.flighthub.base.dtos.FlightDTO;
import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.enums.FlightStatusEnum;
import com.cbse.flighthub.base.interfaces.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFlights_WithDate_ShouldReturnFlights() {
        // Arrange
        String date = "2025-01-01";
        String origin = "NYC";
        String destination = "LAX";
        Flight flight = new Flight();
        when(flightService.getFlightsByDateAndOriginAndDestination(date, origin, destination))
                .thenReturn(List.of(flight));

        // Act
        List<Flight> flights = flightController.getFlights(date, origin, destination);

        // Assert
        assertNotNull(flights);
        assertEquals(1, flights.size());
        verify(flightService, times(1)).getFlightsByDateAndOriginAndDestination(date, origin, destination);
    }

    @Test
    void getFlights_WithoutDate_ShouldReturnFlights() {
        // Arrange
        String origin = "NYC";
        String destination = "LAX";
        Flight flight = new Flight();
        when(flightService.getFlightsByOriginAndDestination(origin, destination))
                .thenReturn(List.of(flight));

        // Act
        List<Flight> flights = flightController.getFlights(null, origin, destination);

        // Assert
        assertNotNull(flights);
        assertEquals(1, flights.size());
        verify(flightService, times(1)).getFlightsByOriginAndDestination(origin, destination);
    }

    @Test
    void addFlight_ShouldReturnSuccessMessage() {
        // Arrange
        FlightDTO dto = new FlightDTO();
        dto.setFlightNumber("FL123");
        dto.setDepartureTime("10:00 AM");
        dto.setArrivalTime("12:00 PM");
        dto.setOrigin("NYC");
        dto.setDestination("LAX");
        dto.setAvailableSeats(100);
        dto.setPrice(200);
        dto.setCompany("Airline");
        dto.setDate("2025-01-01");

        doAnswer(invocation -> null).when(flightService).saveFlight(any(Flight.class));

        // Act
        ResponseEntity<String> response = flightController.addFlight(dto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Flight saved successfully.", response.getBody());
        verify(flightService, times(1)).saveFlight(any(Flight.class));
    }

    @Test
    void cancelFlight_ShouldReturnSuccessMessage() {
        // Arrange
        String flightId = "12345";
        Flight flight = new Flight();
        when(flightService.getFlightById(flightId)).thenReturn(flight);

        doAnswer(invocation -> null).when(flightService).saveFlight(any(Flight.class));

        // Act
        ResponseEntity<String> response = flightController.cancelFlight(flightId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Flight status changed to CANCELED successfully.", response.getBody());
        assertEquals(FlightStatusEnum.CANCELED, flight.getFlightStatus());
        verify(flightService, times(1)).getFlightById(flightId);
        verify(flightService, times(1)).saveFlight(flight);
    }

    @Test
    void cancelFlight_ShouldReturnFlightNotFound() {
        // Arrange
        String flightId = "12345";
        when(flightService.getFlightById(flightId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = flightController.cancelFlight(flightId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Flight not found.", response.getBody());
        verify(flightService, times(1)).getFlightById(flightId);
    }

    @Test
    void delayFlight_ShouldReturnSuccessMessage() {
        // Arrange
        String flightId = "12345";
        Flight flight = new Flight();
        when(flightService.getFlightById(flightId)).thenReturn(flight);

        doAnswer(invocation -> null).when(flightService).saveFlight(any(Flight.class));

        // Act
        ResponseEntity<String> response = flightController.delayFlight(flightId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Flight status changed to DELAYED successfully.", response.getBody());
        assertEquals(FlightStatusEnum.DELAYED, flight.getFlightStatus());
        verify(flightService, times(1)).getFlightById(flightId);
        verify(flightService, times(1)).saveFlight(flight);
    }

    @Test
    void delayFlight_ShouldReturnFlightNotFound() {
        // Arrange
        String flightId = "12345";
        when(flightService.getFlightById(flightId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = flightController.delayFlight(flightId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Flight not found.", response.getBody());
        verify(flightService, times(1)).getFlightById(flightId);
    }
}
