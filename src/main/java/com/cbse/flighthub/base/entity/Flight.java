package com.cbse.flighthub.base.entity;

import com.cbse.flighthub.base.enums.FlightStatusEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Document("flights")
public class Flight {
    @Id
    private String id;
    @Field("flight_number")
    private String flightNumber;
    @Field("departure_time")
    private String departureTime;
    @Field("arrival_time")
    private String arrivalTime;
    private String origin;
    private String destination;
    @Field("available_seats")
    private int availableSeats;
    private int price;
    private String company;
    private String date;
    @Field("flight_status")
    private FlightStatusEnum flightStatus;
    private List<Booking> bookings;
}
