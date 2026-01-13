package com.example.btportal.security;

import com.example.btportal.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor; // <-- Import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // <-- Import
import org.springframework.security.authentication.AuthenticationProvider; // <-- Import
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // <-- Import
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // <-- Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Use constructor injection
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService; // <-- Inject your new service

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // This line permits ALL requests to ALL endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                );

        // We don't even add the JWT filter because it's not needed
        return http.build();
    }

    // ✅ 3. THIS BEAN WIRES YOUR USER SERVICE TO SPRING SECURITY
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set your user service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set your password encoder
        return authProvider;
    }

    // ✅ 4. THIS BEAN IS NEEDED FOR THE LOGIN CONTROLLER
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        // ... (Your CORS config is fine)
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}