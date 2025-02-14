package com.cbse.flighthub.base.dtos;

import lombok.Data;

@Data
public class FlightDTO {
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private String origin;
    private String destination;
    private int availableSeats;
    private int price;
    private String company;
    private String date;

    public FlightDTO(){}
}
