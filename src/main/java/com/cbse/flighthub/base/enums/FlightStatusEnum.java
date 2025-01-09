package com.cbse.flighthub.base.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FlightStatusEnum {
    DELAYED("DELAYED"),
    ONTIME("ONTIME"),
    CANCELED("CANCELED");

    private final String status;

    FlightStatusEnum(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
