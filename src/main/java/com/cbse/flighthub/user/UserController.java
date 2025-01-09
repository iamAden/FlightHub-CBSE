package com.cbse.flighthub.user;

import com.cbse.flighthub.base.dtos.LoginDTO;
import com.cbse.flighthub.base.dtos.RegisterDTO;
import com.cbse.flighthub.base.entity.Booking;
import com.cbse.flighthub.base.interfaces.BookingService;
import com.cbse.flighthub.base.interfaces.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cbse.flighthub.base.entity.User;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto) {
        if (dto.getEmail() == null || dto.getName() == null || dto.getPassword() == null) {
            return new ResponseEntity<>("All fields (name, email, password) are required.", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(dto.getName());
        user.setAdmin(false);
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        try {
            userService.saveUser(user);
            return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while registering the user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) {
        if (dto.getEmail() == null || dto.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required."));
        }

        try {
            User user = userService.getUserByEmail(dto.getEmail());
            if (Objects.equals(user.getPassword(), dto.getPassword())) {
                // Return the userId in the response
                return ResponseEntity.ok(Map.of("message", "Login successful", "userId", user.getId()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Wrong password."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not registered."));
        }
    }


    @GetMapping("/bookinghistory")
    public List<Booking> getBookingHistory(HttpServletRequest request) {
        // Retrieve the userId from the cookie
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        // Log the cookie for debugging
        if (userId == null) {
            return Collections.emptyList();
        }

        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
            return bookings;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

}
