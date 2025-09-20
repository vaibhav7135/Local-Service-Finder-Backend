package com.localservice.backend_local_service.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.localservice.backend_local_service.model.User;
import com.localservice.backend_local_service.repository.UserRepository;
import com.localservice.backend_local_service.model.Role;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_JOBSEEKER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean authenticateUser(String email, String password) {
        User user = findByEmail(email);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
    
    public List<User> getAllServiceProviders() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().name().equals("ROLE_SERVICE_PROVIDER"))
                .toList();
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


}