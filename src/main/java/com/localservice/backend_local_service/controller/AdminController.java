package com.localservice.backend_local_service.controller;


import com.localservice.backend_local_service.model.User;
import com.localservice.backend_local_service.repository.BookingRepository;
import com.localservice.backend_local_service.repository.BookingResponse;
import com.localservice.backend_local_service.repository.UserRepository;
import com.localservice.backend_local_service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get all bookings (admin only)
    @GetMapping("/bookings")
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingResponse::new)
                .toList();
    }
    
@DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // Check if the user exists
        if (!userRepository.existsById(id)) {
            // Return a 404 Not Found status
            return ResponseEntity.notFound().build();
        }

        try {
            // Attempt to delete the user
            userRepository.deleteById(id);
            
            // Return a 200 OK status with a success message
            return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
            
            // Alternatively, for no response body:
            // return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            // In case of a database error or other issue during deletion
            // Return a 500 Internal Server Error
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
    
}
