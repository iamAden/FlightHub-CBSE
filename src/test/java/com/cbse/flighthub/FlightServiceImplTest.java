package com.cbse.flighthub;

import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.flight.FlightRepository;
import com.cbse.flighthub.flight.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFlightsByDateAndOriginAndDestination_ShouldReturnFlights() throws ParseException {
        // Arrange
        String date = "2025-01-15";
        String origin = "NYC";
        String destination = "LAX";
        Flight mockFlight = new Flight();
        mockFlight.setFlightNumber("FL123");
        List<Flight> mockFlights = Arrays.asList(mockFlight);

        when(flightRepository.getFlightsByDateAndOriginAndDestination(anyString(), eq(origin), eq(destination)))
                .thenReturn(mockFlights);

        // Act
        List<Flight> result = flightService.getFlightsByDateAndOriginAndDestination(date, origin, destination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FL123", result.get(0).getFlightNumber());
        verify(flightRepository, times(1))
                .getFlightsByDateAndOriginAndDestination(anyString(), eq(origin), eq(destination));
    }

    @Test
    void getFlightsByDateAndOriginAndDestination_ShouldHandleParseException() {
        // Arrange
        String invalidDate = "invalid-date";
        String origin = "NYC";
        String destination = "LAX";

        // Act
        List<Flight> result = flightService.getFlightsByDateAndOriginAndDestination(invalidDate, origin, destination);

        // Assert
        assertNull(result);
        verify(flightRepository, never()).getFlightsByDateAndOriginAndDestination(anyString(), anyString(), anyString());
    }

    @Test
    void getFlightsByOriginAndDestination_ShouldReturnFlights() {
        // Arrange
        String origin = "NYC";
        String destination = "LAX";
        Flight mockFlight = new Flight();
        mockFlight.setFlightNumber("FL123");
        List<Flight> mockFlights = Arrays.asList(mockFlight);

        when(flightRepository.getFlightsByOriginAndDestination(origin, destination)).thenReturn(mockFlights);

        // Act
        List<Flight> result = flightService.getFlightsByOriginAndDestination(origin, destination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FL123", result.get(0).getFlightNumber());
        verify(flightRepository, times(1)).getFlightsByOriginAndDestination(origin, destination);
    }

    @Test
    void getFlightById_ShouldReturnFlight() {
        // Arrange
        String flightId = "FL123";
        Flight mockFlight = new Flight();
        mockFlight.setFlightNumber(flightId);

        when(flightRepository.getFlightById(flightId)).thenReturn(mockFlight);

        // Act
        Flight result = flightService.getFlightById(flightId);

        // Assert
        assertNotNull(result);
        assertEquals(flightId, result.getFlightNumber());
        verify(flightRepository, times(1)).getFlightById(flightId);
    }

    @Test
    void saveFlight_ShouldReturnSavedFlight() {
        // Arrange
        Flight mockFlight = new Flight();
        mockFlight.setFlightNumber("FL123");

        when(flightRepository.save(mockFlight)).thenReturn(mockFlight);

        // Act
        Flight result = flightService.saveFlight(mockFlight);

        // Assert
        assertNotNull(result);
        assertEquals("FL123", result.getFlightNumber());
        verify(flightRepository, times(1)).save(mockFlight);
    }


}
