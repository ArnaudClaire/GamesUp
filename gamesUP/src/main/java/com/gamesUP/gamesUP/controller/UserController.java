package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.UserRequest;
import com.gamesUP.gamesUP.dto.UserResponse;
import com.gamesUP.gamesUP.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
/**
 * Expose les endpoints REST liés aux comptes utilisateur.
 */
public class UserController {

    private final UserService userService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service des utilisateurs.
     *
     * @param userService service responsable des opérations liées aux utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Liste tous les comptes utilisateur.
     *
     * @return utilisateurs
     */
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    /**
     * Renvoie le compte de l'utilisateur authentifié.
     *
     * @param principal principal de l'utilisateur authentifié
     * @return utilisateur courant
     */
    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id identifiant utilisateur
     * @return utilisateur demandé
     */
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Crée un compte client.
     *
     * @param request données utilisateur validées
     * @return réponse de l'utilisateur créé avec son emplacement
     */
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.created(URI.create("/api/users/" + response.id())).body(response);
    }

    /**
     * Met à jour un compte utilisateur.
     *
     * @param id identifiant utilisateur
     * @param request données utilisateur validées
     * @return utilisateur mis à jour
     */
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    /**
     * Supprime un compte utilisateur.
     *
     * @param id identifiant utilisateur
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
