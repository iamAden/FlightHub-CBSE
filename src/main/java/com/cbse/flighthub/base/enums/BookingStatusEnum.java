package com.cbse.flighthub.base.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingStatusEnum {
    CHECKEDIN("CHECKEDIN"),
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED");

    private final String status;

    BookingStatusEnum(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}

