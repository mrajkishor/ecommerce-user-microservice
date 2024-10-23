package com.ecommerce.user.controller;  // Adjust the package as needed

import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import com.ecommerce.user.exception.InvalidCredentialsException;
import com.ecommerce.user.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // Test for successful user registration
    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        User user = new User("john@example.com", "password123");
        when(userService.registerUser(any(User.class))).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    // Test for user already exists scenario during registration
    @Test
    public void shouldReturnConflictIfUserAlreadyExists() throws Exception {
        // Arrange
        when(userService.registerUser(any(User.class))).thenThrow(new UserAlreadyExistsException());

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"existing@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isConflict());

        verify(userService, times(1)).registerUser(any(User.class));
    }

    // Test for successful login
    @Test
    public void shouldLoginUserSuccessfully() throws Exception {
        // Arrange
        User user = new User("john@example.com", "password123");
        when(userService.loginUser(any(User.class))).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).loginUser(any(User.class));
    }

    // Test for invalid credentials during login
    @Test
    public void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
        // Arrange
        when(userService.loginUser(any(User.class))).thenThrow(new InvalidCredentialsException());

        // Act & Assert
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).loginUser(any(User.class));
    }
}
