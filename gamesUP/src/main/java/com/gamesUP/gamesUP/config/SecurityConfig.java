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
 * Configures authentication, authorization and password encoding for the API.
 */
public class SecurityConfig {

    private final UserRepository userRepository;

    /**
     * Creates the security configuration with its user repository dependency.
     *
     * @param userRepository repository used to load users by email
     */
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Defines public, client and administrator route permissions.
     *
     * @param http Spring Security HTTP builder
     * @return configured security filter chain
     * @throws Exception when Spring Security cannot build the filter chain
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
     * Loads authenticated users from the database.
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
     * Exposes the Spring authentication manager.
     *
     * @param configuration Spring authentication configuration
     * @return authentication manager
     * @throws Exception when Spring cannot provide the manager
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
