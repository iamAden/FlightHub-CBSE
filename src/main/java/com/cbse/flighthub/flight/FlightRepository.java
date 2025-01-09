package com.cbse.flighthub.flight;

import com.cbse.flighthub.base.entity.Flight;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends MongoRepository<Flight,String> {
    @Query("{date:'?0', origin:'?1', destination:'?2'}")
    List<Flight> getFlightsByDateAndOriginAndDestination(String date, String origin, String destination);

    @Query("{origin:'?0', destination:'?1'}")
    List<Flight> getFlightsByOriginAndDestination(String origin, String destination);

    @Query("{id:'?0'}")
    Flight getFlightById(String flightId);

}
