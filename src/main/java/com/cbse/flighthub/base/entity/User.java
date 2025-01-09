package com.cbse.flighthub.base.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Document("users")
public class User {
    @Id
    private String id;
    private String email;
    private String name;
    private String password;
    private boolean isAdmin;
    private List<Booking> bookings;
    private Membership membership;
    private int pointsEarned;
}
