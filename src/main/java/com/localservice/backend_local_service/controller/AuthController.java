package com.localservice.backend_local_service.controller;

import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.localservice.backend_local_service.model.Role;
import com.localservice.backend_local_service.model.User;
import com.localservice.backend_local_service.service.UserService;
import com.localservice.backend_local_service.security.JwtUtils;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email field is required.");
        }

        // If registering as SERVICE_PROVIDER, service name is required
        if (user.getRole() == Role.ROLE_SERVICE_PROVIDER) {
            if (user.getService() == null || user.getService().isEmpty()) {
                return ResponseEntity.badRequest().body("Service name is required for providers.");
            }
        }

        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (userService.authenticateUser(email, password)) {
            User user = userService.findByEmail(email);
            if (user != null) {
            	String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(),user.getId()); // Pass the role as a StringPass the Role enum directly
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }
    
    @GetMapping("/providers")
    public ResponseEntity<?> getAllProviders() {
        return ResponseEntity.ok(userService.getAllServiceProviders());
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        response.put("service", user.getService());
        response.put("id", user.getId());
        

        return ResponseEntity.ok(response);
    }
    @GetMapping("/providers/{id}")
    public ResponseEntity<?> getProviderById(@PathVariable Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.status(404).body("Provider not found");
        }

        if (user.getRole() != Role.ROLE_SERVICE_PROVIDER) {
            return ResponseEntity.badRequest().body("User is not a service provider");
        }

        return ResponseEntity.ok(user);
    }



    @GetMapping("/test")
    public String test() {
        return "Backend is running!";
    }
}