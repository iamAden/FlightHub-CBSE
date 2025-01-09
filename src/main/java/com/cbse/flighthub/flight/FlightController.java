package com.cbse.flighthub.flight;

import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.interfaces.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightController {
    @Autowired
    FlightService flightService;

    @GetMapping("/flight")
    public List<Flight> getFlights(
            @RequestParam(required = false) String date,
            @RequestParam String origin,
            @RequestParam String destination) {

        if (date != null) {
            return flightService.getFlightsByDateAndOriginAndDestination(date, origin, destination);
        } else {
            return flightService.getFlightsByOriginAndDestination(origin, destination);
        }
    }

    @GetMapping("/getOrigins")
    public List<String> getOrigins() {
        return flightService.getOrigins();
    }

    @GetMapping("/getDestinations")
    public List<String> getDestinations() {
        return flightService.getDestinations();
    }
}
