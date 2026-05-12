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
 * Fournit les opérations métier liées aux comptes utilisateur.
 */
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crée le service avec ses dépendances vers le dépôt et l'encodeur de mots de passe.
     *
     * @param userRepository dépôt des utilisateurs
     * @param passwordEncoder encodeur de mot de passe utilisé avant l'enregistrement des identifiants
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Liste tous les comptes utilisateur.
     *
     * @return utilisateurs
     */
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id identifiant utilisateur
     * @return utilisateur demandé
     */
    public UserResponse findById(Long id) {
        return toResponse(findUser(id));
    }

    /**
     * Recherche un utilisateur par email.
     *
     * @param email user email
     * @return utilisateur demandé
     */
    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    /**
     * Crée un nouveau compte.
     *
     * @param request données utilisateur
     * @return utilisateur créé
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
     * Met à jour un compte existant.
     *
     * @param id identifiant utilisateur
     * @param request données utilisateur
     * @return utilisateur mis à jour
     */
    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = findUser(id);
        applyRequest(user, request, false);
        return toResponse(user);
    }

    /**
     * Supprime un compte utilisateur.
     *
     * @param id identifiant utilisateur
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
