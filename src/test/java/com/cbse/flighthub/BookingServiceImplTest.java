package com.cbse.flighthub;

import com.cbse.flighthub.base.entity.Booking;
import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.base.enums.BookingStatusEnum;
import com.cbse.flighthub.base.interfaces.UserService;
import com.cbse.flighthub.booking.BookingRepository;
import com.cbse.flighthub.booking.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBooking_ShouldAddPointsAndSaveBooking() {
        // Arrange
        User user = new User();
        user.setId("U123");
        Flight flight = new Flight();
        flight.setId("FL123");
        flight.setPrice(200);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking result = bookingServiceImpl.saveBooking(booking);

        // Assert
        assertNotNull(result);
        verify(userService, times(1)).addPoints(user, 20); // Price / 10 = 200 / 10 = 20
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void updateBooking_ShouldInsertBooking() {
        // Arrange
        Booking booking = new Booking();
        booking.setId("BK123");

        when(bookingRepository.insert(any(Booking.class))).thenReturn(booking);

        // Act
        Booking result = bookingServiceImpl.updateBooking(booking);

        // Assert
        assertNotNull(result);
        assertEquals("BK123", result.getId());
        verify(bookingRepository, times(1)).insert(booking);
    }

    @Test
    void cancelBooking_ShouldSetStatusToCanceledAndSave() {
        // Arrange
        String bookingId = "BK123";
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatusEnum.CONFIRMED);

        when(bookingRepository.getBookingById(bookingId)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking result = bookingServiceImpl.cancelBooking(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatusEnum.CANCELED, result.getBookingStatus());
        verify(bookingRepository, times(1)).getBookingById(bookingId);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void getBookingById_ShouldReturnBooking() {
        // Arrange
        String bookingId = "BK123";
        Booking booking = new Booking();
        booking.setId(bookingId);

        when(bookingRepository.getBookingById(bookingId)).thenReturn(booking);

        // Act
        Booking result = bookingServiceImpl.getBookingById(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        verify(bookingRepository, times(1)).getBookingById(bookingId);
    }

    @Test
    void checkInFlight_ShouldSetStatusToCheckedIn() {
        // Arrange
        String bookingId = "BK123";
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatusEnum.CONFIRMED);

        when(bookingRepository.getBookingById(bookingId)).thenReturn(booking);

        // Act
        Booking result = bookingServiceImpl.checkInFlight(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatusEnum.CHECKEDIN, result.getBookingStatus());
        verify(bookingRepository, times(1)).getBookingById(bookingId);
    }

    @Test
    void getBookingsByUserId_ShouldReturnListOfBookings() {
        // Arrange
        String userId = "U123";
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        bookings.add(booking1);
        bookings.add(booking2);

        when(bookingRepository.getBookingsByUserId(userId)).thenReturn(bookings);

        // Act
        List<Booking> result = bookingServiceImpl.getBookingsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).getBookingsByUserId(userId);
    }
}
