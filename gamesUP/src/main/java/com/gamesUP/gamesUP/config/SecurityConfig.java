package com.gamesUP.gamesUP.config;

import com.gamesUP.gamesUP.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
/**
 * Configure l'authentification, les autorisations et l'encodage des mots de passe de l'API.
 */
public class SecurityConfig {

    private final UserRepository userRepository;

    /**
     * Crée la configuration de sécurité avec sa dépendance vers le dépôt des utilisateurs.
     *
     * @param userRepository dépôt utilisé pour charger les utilisateurs par email
     */
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Définit les droits d'accès des routes publiques, client et administrateur.
     *
     * @param http builder HTTP de Spring Security
     * @return chaîne de filtres de sécurité configurée
     * @throws Exception lorsque Spring Security ne peut pas construire la chaîne de filtres
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games/**", "/api/categories/**", "/api/authors/**", "/api/publishers/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers("/api/recommendations/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers("/api/users/me").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Charge les utilisateurs authentifiés depuis la base de données.
     *
     * @return user details service based on user emails
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
    }

    /**
     * Configures DAO authentication with the user service and password encoder.
     *
     * @param userDetailsService user details lookup service
     * @param passwordEncoder password encoder
     * @return authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Expose le gestionnaire d'authentification Spring.
     *
     * @param configuration configuration d'authentification Spring
     * @return authentication manager
     * @throws Exception lorsque Spring ne peut pas fournir le gestionnaire
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Encodes passwords with BCrypt.
     *
     * @return password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
