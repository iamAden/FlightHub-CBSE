package com.cbse.flighthub;

import com.cbse.flighthub.base.dtos.BookingFormDTO;
import com.cbse.flighthub.base.entity.Booking;
import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.base.enums.BookingStatusEnum;
import com.cbse.flighthub.base.interfaces.BookingService;
import com.cbse.flighthub.base.interfaces.FlightService;
import com.cbse.flighthub.base.interfaces.UserService;
import com.cbse.flighthub.booking.BookingController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private FlightService flightService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookFlight_ShouldReturnSuccess() {
        // Arrange
        String flightId = "FL123";
        BookingFormDTO dto = new BookingFormDTO();
        dto.setPassengerName("John Doe");
        dto.setPassengerEmail("john.doe@example.com");
        dto.setPassengerContactNo("123456789");
        dto.setPassengerICNo("IC123456");

        Cookie[] cookies = {new Cookie("userId", "U123")};
        when(request.getCookies()).thenReturn(cookies);

        User mockUser = new User();
        mockUser.setId("U123");
        when(userService.getUserById("U123")).thenReturn(mockUser);

        Flight mockFlight = new Flight();
        mockFlight.setId(flightId);
        mockFlight.setAvailableSeats(10);
        when(flightService.getFlightById(flightId)).thenReturn(mockFlight);

        when(bookingService.saveBooking(any(Booking.class))).thenReturn(new Booking());
        when(flightService.saveFlight(any(Flight.class))).thenReturn(mockFlight);

        // Act
        ResponseEntity<String> response = bookingController.bookFlight(flightId, dto, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Flight booked successfully.", response.getBody());
        verify(flightService, times(1)).saveFlight(any(Flight.class));
    }

    @Test
    void bookFlight_ShouldReturnUnauthorizedIfUserNotLoggedIn() {
        // Arrange
        String flightId = "FL123";
        BookingFormDTO dto = new BookingFormDTO();
        dto.setPassengerName("John Doe");
        dto.setPassengerEmail("john.doe@example.com");
        dto.setPassengerICNo("041212312323");
        dto.setPassengerContactNo("01922912039");

        when(request.getCookies()).thenReturn(null);

        // Act
        ResponseEntity<String> response = bookingController.bookFlight(flightId, dto, request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User is not logged in.", response.getBody());
        verifyNoInteractions(userService, flightService, bookingService);
    }

    @Test
    void bookFlight_ShouldReturnInvalidRequestIfNoFlightId() {
        // Arrange
        String flightId = null;
        BookingFormDTO dto = new BookingFormDTO();
        dto.setPassengerName("John Doe");
        dto.setPassengerEmail("john.doe@example.com");
        dto.setPassengerICNo("041212312323");
        dto.setPassengerContactNo("01922912039");

        Cookie[] cookies = {new Cookie("userId", "U123")};
        when(request.getCookies()).thenReturn(cookies);

        User mockUser = new User();
        mockUser.setId("U123");
        when(userService.getUserById("U123")).thenReturn(mockUser);

        when(flightService.getFlightById(flightId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = bookingController.bookFlight(flightId, dto, request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request", response.getBody());
        verifyNoInteractions(bookingService);
    }

    @Test
    void bookFlight_ShouldReturnInvalidRequestIfIncompleteBookingFormDTO() {
        // Arrange
        String flightId = null;
        BookingFormDTO dto = new BookingFormDTO();
        dto.setPassengerName("John Doe");
        dto.setPassengerEmail("john.doe@example.com");
        dto.setPassengerICNo(null);
        dto.setPassengerContactNo(null);

        Cookie[] cookies = {new Cookie("userId", "U123")};
        when(request.getCookies()).thenReturn(cookies);

        User mockUser = new User();
        mockUser.setId("U123");
        when(userService.getUserById("U123")).thenReturn(mockUser);

        when(flightService.getFlightById(flightId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = bookingController.bookFlight(flightId, dto, request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request", response.getBody());
        verifyNoInteractions(bookingService);
    }


    @Test
    void bookFlight_ShouldReturnNotFoundIfFlightDoesNotExist() {
        // Arrange
        String flightId = "FL123";
        BookingFormDTO dto = new BookingFormDTO();
        dto.setPassengerName("John Doe");
        dto.setPassengerEmail("john.doe@example.com");
        dto.setPassengerICNo("041212312323");
        dto.setPassengerContactNo("01922912039");

        Cookie[] cookies = {new Cookie("userId", "U123")};
        when(request.getCookies()).thenReturn(cookies);

        User mockUser = new User();
        mockUser.setId("U123");
        when(userService.getUserById("U123")).thenReturn(mockUser);

        when(flightService.getFlightById(flightId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = bookingController.bookFlight(flightId, dto, request);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Flight not found.", response.getBody());
        verifyNoInteractions(bookingService);
    }

    @Test
    void cancelBooking_ShouldReturnSuccess() {
        // Arrange
        String bookingId = "BK123";

        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setBookingStatus(BookingStatusEnum.CONFIRMED);

        when(bookingService.getBookingById(bookingId)).thenReturn(mockBooking);
        when(bookingService.saveBooking(any(Booking.class))).thenReturn(mockBooking);

        // Act
        ResponseEntity<String> response = bookingController.cancelBooking(bookingId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Booking canceled successfully.", response.getBody());
        verify(bookingService, times(1)).saveBooking(any(Booking.class));
    }

    @Test
    void cancelBooking_ShouldReturnNotFoundIfBookingDoesNotExist() {
        // Arrange
        String bookingId = "BK123";

        when(bookingService.getBookingById(bookingId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = bookingController.cancelBooking(bookingId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Booking not found.", response.getBody());
    }

    @Test
    void checkInBooking_ShouldReturnSuccess() {
        // Arrange
        String bookingId = "BK123";

        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setBookingStatus(BookingStatusEnum.CONFIRMED);

        when(bookingService.getBookingById(bookingId)).thenReturn(mockBooking);
        when(bookingService.saveBooking(any(Booking.class))).thenReturn(mockBooking);

        // Act
        ResponseEntity<String> response = bookingController.checkInBooking(bookingId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Booking checked in successfully.", response.getBody());
        verify(bookingService, times(1)).saveBooking(any(Booking.class));
    }

    @Test
    void checkInBooking_ShouldReturnNotFoundIfBookingDoesNotExist() {
        // Arrange
        String bookingId = "BK123";

        when(bookingService.getBookingById(bookingId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = bookingController.checkInBooking(bookingId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Booking not found.", response.getBody());
    }
}
