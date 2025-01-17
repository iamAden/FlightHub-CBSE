package com.cbse.flighthub.booking;

import com.cbse.flighthub.base.entity.Booking;
import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.base.enums.BookingStatusEnum;
import com.cbse.flighthub.base.interfaces.BookingService;
import com.cbse.flighthub.base.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserService userService;

    @Override
    public Booking saveBooking(Booking booking) {
        User user = booking.getUser();
        int point = booking.getFlight().getPrice() / 10;
        userService.addPoints(user, point);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(String bookingId) {
        Booking canceledBooking = bookingRepository.getBookingById(bookingId);
        canceledBooking.setBookingStatus(BookingStatusEnum.CANCELED);
        bookingRepository.save(canceledBooking);
        return canceledBooking;
    }

    @Override
    public Booking getBookingById(String bookingId) {
        return bookingRepository.getBookingById(bookingId);
    }

    @Override
    public Booking checkInFlight(String bookingId) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        booking.setBookingStatus(BookingStatusEnum.CHECKEDIN);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.getBookingsByUserId(userId);
    }

}
