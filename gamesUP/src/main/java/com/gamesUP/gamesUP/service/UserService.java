package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.UserRequest;
import com.gamesUP.gamesUP.dto.UserResponse;
import com.gamesUP.gamesUP.exception.ConflictException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
/**
 * Provides business operations for user accounts.
 */
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates the service with repository and password encoder dependencies.
     *
     * @param userRepository repository for users
     * @param passwordEncoder password encoder used before storing credentials
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Lists every user account.
     *
     * @return users
     */
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Finds a user by identifier.
     *
     * @param id user identifier
     * @return requested user
     */
    public UserResponse findById(Long id) {
        return toResponse(findUser(id));
    }

    /**
     * Finds a user by email.
     *
     * @param email user email
     * @return requested user
     */
    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    /**
     * Creates a new account.
     *
     * @param request user payload
     * @return created user
     */
    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        applyRequest(user, request, true);
        user.setRole(Role.CLIENT);
        return toResponse(userRepository.save(user));
    }

    /**
     * Updates an existing account.
     *
     * @param id user identifier
     * @param request user payload
     * @return updated user
     */
    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = findUser(id);
        applyRequest(user, request, false);
        return toResponse(user);
    }

    /**
     * Deletes a user account.
     *
     * @param id user identifier
     */
    @Transactional
    public void delete(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    private void applyRequest(User user, UserRequest request, boolean requirePassword) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        } else if (requirePassword) {
            throw new IllegalArgumentException("Password is required");
        }
        user.setRole(request.role() == null ? Role.CLIENT : request.role());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }
}
