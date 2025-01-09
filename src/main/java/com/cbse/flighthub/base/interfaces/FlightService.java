package com.cbse.flighthub.base.interfaces;

import com.cbse.flighthub.base.entity.Flight;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlightService {
    List<Flight> getFlightsByDateAndOriginAndDestination(String date, String origin, String destination);
    List<Flight> getFlightsByOriginAndDestination(String origin, String destination);

    Flight getFlightById(String flightId);
    Flight saveFlight(Flight flight);
}
