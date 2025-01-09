package com.cbse.flighthub.booking;

import com.cbse.flighthub.base.dtos.BookingFormDTO;
import com.cbse.flighthub.base.entity.Booking;
import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.base.enums.BookingStatusEnum;
import com.cbse.flighthub.base.interfaces.BookingService;
import com.cbse.flighthub.base.interfaces.FlightService;
import com.cbse.flighthub.base.interfaces.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {
    @Autowired
    BookingService bookingService;

    @Autowired
    FlightService flightService;

    @Autowired
    UserService userService;

    @PostMapping("/book")
    public ResponseEntity<String> bookFlight(@RequestParam String flightId, @RequestBody BookingFormDTO dto, HttpServletRequest request) {
        if (flightId == null || dto.getPassengerContactNo() == null || dto.getPassengerICNo() == null ||
                dto.getPassengerEmail() == null || dto.getPassengerName() == null) {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        try {
            // Retrieve the userId from the cookie
            String userId = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userId".equals(cookie.getName())) {
                        userId = cookie.getValue();
                        break;
                    }
                }
            }
            if (userId == null) {
                return ResponseEntity.badRequest().body("User is not logged in.");
            }

            // Fetch the user by userId
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found.");
            }

            Flight flight = flightService.getFlightById(flightId);
            if (flight == null) {
                return ResponseEntity.status(404).body("Flight not found.");
            }

            if (flight.getAvailableSeats() <= 0) {
                return ResponseEntity.badRequest().body("No available seats left.");
            }

            Booking booking = new Booking();
            booking.setUser(user); // Set the user
            booking.setFlight(flight); // Set the flight
            booking.setPassengerEmail(dto.getPassengerEmail());
            booking.setPassengerContactNo(dto.getPassengerContactNo());
            booking.setPassengerName(dto.getPassengerName());
            booking.setPassengerICNo(dto.getPassengerICNo());
            booking.setBookingStatus(BookingStatusEnum.CONFIRMED);

            System.out.println(booking);
            bookingService.saveBooking(booking);

            // Reduce available seats
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            flightService.saveFlight(flight);

            return ResponseEntity.ok("Flight booked successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing booking.");
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestParam String bookingId) {
        if (bookingId == null) {
            return ResponseEntity.badRequest().body("Booking Id is null");
        }

        try {
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                booking.setBookingStatus(BookingStatusEnum.CANCELED);
                bookingService.saveBooking(booking);
                return ResponseEntity.ok("Booking canceled successfully.");
            } else {
                return ResponseEntity.badRequest().body("Booking not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error cancelling booking.");
        }
    }

    @PostMapping("/checkin")
    public ResponseEntity<String> checkInBooking(@RequestParam String bookingId) {
        if (bookingId == null) {
            return ResponseEntity.badRequest().body("Booking Id is null");
        }

        try {
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                booking.setBookingStatus(BookingStatusEnum.CHECKEDIN);
                bookingService.saveBooking(booking);
                return ResponseEntity.ok("Booking checked in successfully.");
            } else {
                return ResponseEntity.badRequest().body("Booking not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error checking in booking.");
        }
    }

}
