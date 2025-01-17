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
import java.util.Collections;
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
        RegisterDTO dto = new RegisterDTO("john@example.com", "John Doe", "password123");
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
        RegisterDTO dto = new RegisterDTO(null, "john", "password123");

        ResponseEntity<String> response = userController.register(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("All fields (name, email, password) are required.", response.getBody());
    }

    @Test
    void testRegister_Failure_ExistingEmail() {
        RegisterDTO dto = new RegisterDTO("john@example.com", "John Doe", "password123");
        User user = new User();
        user.setEmail("john@example.com");

        when(userService.getUserByEmail("john@example.com")).thenReturn(user);
        ResponseEntity<String> response = userController.register(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is already registered.", response.getBody());
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
    void testLogin_EmailNotRegistered() {
        LoginDTO loginDTO = new LoginDTO("john@gmail.com", "password123");
        when(userService.getUserByEmail(loginDTO.getEmail())).thenThrow(new RuntimeException("Email not found"));

        ResponseEntity<Map<String, String>> response = userController.login(loginDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Email not registered.", response.getBody().get("error"));
        verify(userService, times(1)).getUserByEmail(loginDTO.getEmail());
    }

    @Test
    void testBookingHistory_NoCookies() {
        when(request.getCookies()).thenReturn(null);
        List<Booking> response = userController.getBookingHistory(request);
        assertEquals(Collections.emptyList(), response);
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

    @Test
    void testShowProfile_NoCookies() {
        when(request.getCookies()).thenReturn(null);

        ResponseEntity<?> response = userController.showProfile(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No user found", response.getBody());
    }

    @Test
    void testShowProfile_UserNotFound() {
        Cookie[] cookies = {new Cookie("userId", "123")};
        when(request.getCookies()).thenReturn(cookies);
        when(userService.getUserById("123")).thenReturn(null);

        ResponseEntity<?> response = userController.showProfile(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No user found", response.getBody());
    }

    @Test
    void testShowProfile_UserFound() {
        Cookie[] cookies = {new Cookie("userId", "123")};
        User user = new User();
        user.setEmail("john@gmail.com");
        user.setName("John");
        user.setPointsEarned(900);

        when(request.getCookies()).thenReturn(cookies);
        when(userService.getUserById("123")).thenReturn(user);

        ResponseEntity<?> response = userController.showProfile(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}

