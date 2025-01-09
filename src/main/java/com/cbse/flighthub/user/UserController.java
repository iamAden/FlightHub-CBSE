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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbse.flighthub.base.entity.User;

import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<String> login(@RequestBody LoginDTO dto, HttpServletResponse response) {
        if (dto.getEmail() == null || dto.getPassword() == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        try {
            User user = userService.getUserbyEmail(dto.getEmail());
            if (user != null && Objects.equals(user.getPassword(), dto.getPassword())) {
                // Set the cookie for the userId or JWT token (for session persistence)
                Cookie cookie = new Cookie("userId", user.getId()); // or use a JWT token instead of userId
                cookie.setHttpOnly(true); // Prevents JavaScript access to the cookie
                cookie.setSecure(true); // Ensure cookie is only sent over HTTPS
                cookie.setMaxAge(60 * 60); // Set cookie expiry (1 hour)
                cookie.setPath("/"); // Cookie available for all endpoints
                response.addCookie(cookie);

                return ResponseEntity.ok("User logged in successfully.");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password. Try again.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not registered.");
        }
    }

    @GetMapping("bookinghistory")
    public List<Booking> bookingHistory(HttpServletRequest request) {

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

        if (userId == null) {
            return null;
        }

        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
            return bookings;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
