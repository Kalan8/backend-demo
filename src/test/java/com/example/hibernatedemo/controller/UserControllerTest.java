package com.example.hibernatedemo.controller;

import com.example.hibernatedemo.model.User;
import com.example.hibernatedemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 /**
 * Class tests for the {@link UserController} class.
 * <p>
 * Verifies the behavior of all REST endpoints exposed by the
 * {@code UserController}. It focuses on ensuring that HTTP requests are
 * correctly handled, responses have the expected status codes and body content,
 * and that the controller properly delegates to the {@link UserService} layer.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String USERS_ENDPOINT = "/api/users";

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user1 = new User("John", "Doe", "john.doe@example.com");
        user2 = new User("Jane", "Smith", "jane.smith@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].surname").value("Smith"));
    }

    @Test
    void getUserById_ShouldReturnUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.ofNullable(user1));

        mockMvc.perform(get(USERS_ENDPOINT+"/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void getUserById_ShouldReturnNotFoundException() throws Exception {

        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get(USERS_ENDPOINT+"/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User("Johnny", "Doe", "johnny.doe@example.com");
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put(USERS_ENDPOINT+"/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    void updateUser_NonExistent_ShouldReturnNotFound() throws Exception {
        when(userService.updateUser(eq(999L), any(User.class))).thenReturn(Optional.empty());

        String updatedUserJson = """
            {
              "name": "Johnny",
              "surname": "Doe",
              "email": "johnny@example.com"
            }
            """;

        mockMvc.perform(put(USERS_ENDPOINT+"/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteUser_ShouldCallUserServiceDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete(USERS_ENDPOINT+"/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
