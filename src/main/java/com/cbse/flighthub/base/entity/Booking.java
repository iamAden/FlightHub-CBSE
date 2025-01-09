package com.cbse.flighthub.base.entity;

import com.cbse.flighthub.base.enums.BookingStatusEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Document("bookings")
public class Booking {
    @Id
    private String id;
    private Flight flight;
    private User user;
    private String passengerName;
    private String passengerEmail;
    private String passengerICNo;
    private String passengerContactNo;
    private BookingStatusEnum bookingStatus;
    public User getUser() {
        return this.user;
    }
    public Flight getFlight() {
        return this.flight;
    }
    public void setBookingStatus(BookingStatusEnum bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
