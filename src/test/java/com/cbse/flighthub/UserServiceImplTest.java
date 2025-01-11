package com.cbse.flighthub;

import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.user.UserRepository;
import com.cbse.flighthub.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_ShouldSaveUser() {
        // Arrange
        User user = new User();
        user.setName("John Doe");
        when(userRepository.insert(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        verify(userRepository, times(1)).insert(user);
    }

    @Test
    void addPoints_ShouldIncreaseUserPoints() {
        // Arrange
        User user = new User();
        user.setPointsEarned(50);

        // Act
        userService.addPoints(user, 30);

        // Assert
        assertEquals(80, user.getPointsEarned());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        String userId = "12345";
        User user = new User();
        user.setId(userId);
        when(userRepository.getUserById(userId)).thenReturn(user);

        // Act
        User foundUser = userService.getUserById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).getUserById(userId);
    }

    @Test
    void getUserByEmail_ShouldReturnUser() {
        // Arrange
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.getUserByEmail(email)).thenReturn(user);

        // Act
        User foundUser = userService.getUserByEmail(email);

        // Assert
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).getUserByEmail(email);
    }
}
