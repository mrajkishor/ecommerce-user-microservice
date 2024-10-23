package com.ecommerce.user.service;  // Adjust the package name as needed

import com.ecommerce.user.exception.InvalidCredentialsException;
import com.ecommerce.user.exception.UserAlreadyExistsException;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // Test for successful user registration
    @Test
    public void shouldRegisterUserSuccessfully() {
        // Arrange
        User user = new User("john@example.com", "password123");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User registeredUser = userService.registerUser(user);

        // Assert
        assertEquals("john@example.com", registeredUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    // Test for user already exists scenario during registration
    @Test
    public void shouldThrowExceptionIfUserAlreadyExists() {
        // Arrange
        User user = new User("existing@example.com", "password123");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    // Test for successful login
    @Test
    public void shouldLoginUserSuccessfully() {
        // Arrange
        User user = new User("john@example.com", "password123");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        User loggedInUser = userService.loginUser(user);

        // Assert
        assertEquals("john@example.com", loggedInUser.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    // Test for invalid credentials during login
    @Test
    public void shouldThrowExceptionForInvalidCredentials() {
        // Arrange
        User user = new User("john@example.com", "password123");
        User wrongPasswordUser = new User("john@example.com", "wrongpassword");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(wrongPasswordUser));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.loginUser(user);
        });

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    // Test for non-existent user during login
    @Test
    public void shouldThrowExceptionIfUserNotFoundDuringLogin() {
        // Arrange
        User user = new User("nonexistent@example.com", "password123");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.loginUser(user);
        });

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
}
