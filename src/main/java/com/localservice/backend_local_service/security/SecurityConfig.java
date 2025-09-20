package com.localservice.backend_local_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {}) // ✅ enable CORS using your WebConfig
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                	    .requestMatchers(
                	        "/api/auth/register",
                	        "/api/auth/login",
                	        "/api/auth/providers",
                	        "/api/auth/providers/**",
                	        "/api/auth/me",
                	        "/ws/**"
                	        
                	    ).permitAll()
                	    .requestMatchers("/api/bookings/request").hasAuthority("ROLE_JOBSEEKER")
                	    .requestMatchers("/api/bookings/provider/**").hasAuthority("ROLE_SERVICE_PROVIDER")
                	    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                	    .requestMatchers("/api/bookings/**").authenticated()
                	    .requestMatchers("/api/users/**").authenticated() // allow only logged in users
                        .requestMatchers("/api/chat/**").authenticated()
                	    .anyRequest().authenticated()
                	)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}