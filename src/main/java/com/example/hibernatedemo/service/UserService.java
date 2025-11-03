package com.example.hibernatedemo.service;

import com.example.hibernatedemo.model.User;
import com.example.hibernatedemo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for managing {@link User} entities.
 * <p>
 * This class acts as a bridge between the controller layer and the
 * {@link UserRepository}, providing high-level business logic for
 * creating, retrieving, updating, and deleting users.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserService} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository used for performing user persistence operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return a {@link List} containing all {@link User} objects
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the ID of the user to retrieve
     * @return an {@link Optional} containing the found {@link User}, or empty if no user exists with the given ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Creates a new user in the system.
     *
     * @param user the {@link User} object to create
     * @return the saved {@link User} instance with a generated ID
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user with new details.
     * <p>
     * If no user exists with the given ID, this method returns an empty {@link Optional}.
     * </p>
     *
     * @param id          the ID of the user to update
     * @param updatedUser the updated {@link User} data
     * @return the updated {@link User} instance, or empty {@link Optional} if not found
     */
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());
            return userRepository.save(user);
        });
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the ID of the user to delete
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
