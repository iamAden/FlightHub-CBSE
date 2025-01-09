package com.cbse.flighthub.flight;

import com.cbse.flighthub.base.dtos.FlightDTO;
import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.enums.FlightStatusEnum;
import com.cbse.flighthub.base.interfaces.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-flight")
    public ResponseEntity<String> addFlight(@RequestBody FlightDTO dto) {
        Flight newFlight = new Flight();
        newFlight.setFlightNumber(dto.getFlightNumber());
        newFlight.setDepartureTime(dto.getDepartureTime());
        newFlight.setArrivalTime(dto.getArrivalTime());
        newFlight.setOrigin(dto.getOrigin());
        newFlight.setDestination(dto.getDestination());
        newFlight.setAvailableSeats(dto.getAvailableSeats());
        newFlight.setPrice(dto.getPrice());
        newFlight.setFlightStatus(FlightStatusEnum.ONTIME);
        newFlight.setCompany(dto.getCompany());
        newFlight.setDate(dto.getDate());

        try {
            flightService.saveFlight(newFlight);
            return ResponseEntity.ok().body("Flight saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Failed saving flight.");
    }

    @PostMapping("/cancel-flight")
    public ResponseEntity<String> cancelFlight(@RequestParam String flightId) {
        Flight flight = flightService.getFlightById(flightId);

        if (flight == null) {
            return ResponseEntity.badRequest().body("Flight not found.");
        }

        try {
            flight.setFlightStatus(FlightStatusEnum.CANCELED);
            flightService.saveFlight(flight);
            return ResponseEntity.ok().body("Flight status changed to CANCELED successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Failed saving flight.");
    }

    @PostMapping("/delay-flight")
    public ResponseEntity<String> delayFlight(@RequestParam String flightId) {
        Flight flight = flightService.getFlightById(flightId);

        if (flight == null) {
            return ResponseEntity.badRequest().body("Flight not found.");
        }

        try {
            flight.setFlightStatus(FlightStatusEnum.DELAYED);
            flightService.saveFlight(flight);
            return ResponseEntity.ok().body("Flight status changed to DELAYED successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Failed saving flight.");
    }
}
