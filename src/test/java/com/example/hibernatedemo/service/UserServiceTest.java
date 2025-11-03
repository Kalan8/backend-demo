package com.example.hibernatedemo.service;

import com.example.hibernatedemo.model.User;
import com.example.hibernatedemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Class tests for {@link UserService}.
 * <p>
 * Verifies CRUD operations and ensures the service correctly interacts
 * with the {@link UserRepository}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_ShouldReturnUserList() {
        List<User> mockUsers = Arrays.asList(
                new User("John", "Doe", "john@example.com"),
                new User("Jane", "Smith", "jane@example.com"));
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        User user = new User("John", "Doe", "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_ShouldReturnUser() {
        User user = new User("Jane", "Smith", "jane@example.com");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals(result, user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldReturnTheInitialUserUpdated() {
        User user1 = new User("John", "Doe", "john@example.com");
        User user2 = new User("Jane", "Smith", "jane@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        Optional<User> result = userService.updateUser(1L, user2);

        assertTrue(result.isPresent());
        assertEquals(user2.getName(), result.get().getName());
        assertEquals(user2.getSurname(), result.get().getSurname());
        assertEquals(user2.getEmail(), result.get().getEmail());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void deleteUser(){

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}