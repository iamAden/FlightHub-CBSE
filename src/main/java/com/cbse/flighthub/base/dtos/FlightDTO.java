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

    public FlightDTO(String flightNumber, String departureTime, String arrivalTime, String origin, String destination, int availableSeats, int price, String company, String date) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.origin = origin;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.price = price;
        this.company = company;
        this.date = date;
    }
}
