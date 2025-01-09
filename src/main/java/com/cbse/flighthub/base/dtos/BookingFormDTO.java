package com.cbse.flighthub.base.dtos;

import lombok.Data;

@Data
public class BookingFormDTO {
    private String passengerName;
    private String passengerEmail;
    private String passengerICNo;
    private String passengerContactNo;
}
