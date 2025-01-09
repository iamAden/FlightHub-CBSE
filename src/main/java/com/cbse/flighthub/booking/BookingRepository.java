package com.cbse.flighthub.booking;

import com.cbse.flighthub.base.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking,String> {

    Booking insert(Booking booking);

    @Query("{'_id': ?0}")
    Booking getBookingById(String bookingId);

    @Query("{'userId': ?0}")
    List<Booking> getBookingsByUserId(String userId);
}
