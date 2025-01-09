package com.cbse.flighthub;

import com.cbse.flighthub.base.dtos.LoginDTO;
import com.cbse.flighthub.base.dtos.RegisterDTO;
import com.cbse.flighthub.base.entity.Booking;
import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.base.interfaces.BookingService;
import com.cbse.flighthub.base.interfaces.UserService;
import com.cbse.flighthub.user.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private BookingService bookingService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDTO dto = new RegisterDTO("John Doe", "john@example.com", "password123");
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<String> response = userController.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody());
    }

    @Test
    void testRegister_Failure_MissingFields() {
        RegisterDTO dto = new RegisterDTO(null, "john@example.com", "password123");

        ResponseEntity<String> response = userController.register(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("All fields (name, email, password) are required.", response.getBody());
    }

    @Test
    void testLogin_Success() {
        LoginDTO dto = new LoginDTO("john@example.com", "password123");
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setId("12345");

        when(userService.getUserByEmail(dto.getEmail())).thenReturn(user);

        ResponseEntity<Map<String, String>> response = userController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody().get("message"));
        assertEquals("12345", response.getBody().get("userId"));
    }

    @Test
    void testLogin_Failure_WrongPassword() {
        LoginDTO dto = new LoginDTO("john@example.com", "wrongpassword");
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword("password123");

        when(userService.getUserByEmail(dto.getEmail())).thenReturn(user);

        ResponseEntity<Map<String, String>> response = userController.login(dto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Wrong password.", response.getBody().get("error"));
    }

    @Test
    void testBookingHistory_Success() {
        Cookie[] cookies = {new Cookie("userId", "12345")};
        when(request.getCookies()).thenReturn(cookies);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        when(bookingService.getBookingsByUserId("12345")).thenReturn(bookings);

        List<Booking> response = userController.getBookingHistory(request);

        assertEquals(1, response.size());
        verify(bookingService, times(1)).getBookingsByUserId("12345");
    }

    @Test
    void testBookingHistory_NoUserId() {
        when(request.getCookies()).thenReturn(null);

        List<Booking> response = userController.getBookingHistory(request);

        assertEquals(0, response.size());
        verify(bookingService, never()).getBookingsByUserId(anyString());
    }
}

