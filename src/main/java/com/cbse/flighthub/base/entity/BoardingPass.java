package com.cbse.flighthub.base.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
@Document("boardingPasses")
public class BoardingPass {
    @Id
    private String id;
    private Timestamp createdAt;
    private Booking booking;
    private String seatNumber;
    private String gate;
    private String boardingTime;
    private String barcode;
}
