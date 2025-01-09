package com.cbse.flighthub.base.interfaces;

import com.cbse.flighthub.base.entity.Booking;
import org.bson.types.ObjectId;

import java.util.List;

public interface BookingService {
    Booking saveBooking(Booking booking);
    Booking updateBooking(Booking booking);
    Booking cancelBooking(String bookingId);
    Booking getBookingById(String bookingId);
    Booking checkInFlight(String bookingId);
    List<Booking> getBookingsByUserId(String userId);
}
