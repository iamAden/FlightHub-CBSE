package com.cbse.flighthub.flight;

import com.cbse.flighthub.base.entity.Flight;
import com.cbse.flighthub.base.interfaces.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    FlightRepository flightRepository;

    @Override
    public List<Flight> getFlightsByDateAndOriginAndDestination(String date, String origin, String destination) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");

        try {
            // Parse the input date and reformat it
            Date parsedDate = inputFormat.parse(date);
            String formattedDate = outputFormat.format(parsedDate);
            return flightRepository.getFlightsByDateAndOriginAndDestination(formattedDate, origin, destination);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Flight> getFlightsByOriginAndDestination(String origin, String destination) {
        return flightRepository.getFlightsByOriginAndDestination(origin, destination);
    }

    @Override
    public Flight getFlightById(String flightId) {
        return flightRepository.getFlightById(flightId);
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

}
