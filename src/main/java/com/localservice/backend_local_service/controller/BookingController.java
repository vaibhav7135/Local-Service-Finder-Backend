package com.localservice.backend_local_service.controller;

import com.localservice.backend_local_service.model.Booking;
import com.localservice.backend_local_service.model.User;
import com.localservice.backend_local_service.repository.BookingRepository;
import com.localservice.backend_local_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // Job Seeker requests a service
    @PostMapping("/request")
    public Booking requestBooking(@RequestBody Booking booking, Authentication authentication) {
        String email = authentication.getName();
        User jobSeeker = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

        if (!jobSeeker.getId().equals(booking.getJobSeeker().getId())) {
            throw new RuntimeException("Cannot create booking for another user");
        }

        User serviceProvider = userRepository.findById(booking.getServiceProvider().getId())
                .orElseThrow(() -> new RuntimeException("Service Provider not found"));

        booking.setJobSeeker(jobSeeker);
        booking.setServiceProvider(serviceProvider);
        booking.setServiceName(serviceProvider.getService());
        booking.setStatus("PENDING");
        booking.setRequestedAt(java.time.LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    // Get all bookings for a service provider
    @GetMapping("/provider/{providerId}")
    public List<Booking> getProviderBookings(@PathVariable Long providerId, Authentication authentication) {
        String email = authentication.getName();
        
        // Fetch the logged-in provider
        User provider = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated provider not found"));

        // Ensure the authenticated provider matches the requested ID
        if (!provider.getId().equals(providerId)) {
            throw new RuntimeException("Cannot access bookings for another provider");
        }

        return bookingRepository.findByServiceProviderId(providerId);
    }


    // Get all bookings made by a job seeker
    @GetMapping("/jobseeker/{jobSeekerId}")
    public List<Booking> getJobSeekerBookings(@PathVariable Long jobSeekerId, Authentication authentication) {
        String email = authentication.getName();
        User jobSeeker = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

        if (!jobSeeker.getId().equals(jobSeekerId)) {
            throw new RuntimeException("Cannot access bookings for another job seeker");
        }
        return bookingRepository.findByJobSeekerId(jobSeekerId);
    }

    // Update booking status (Accept / Decline)
    @PutMapping("/{id}/status")
    public Booking updateStatus(@PathVariable Long id, @RequestParam String status, Authentication authentication) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String email = authentication.getName();
        if (!booking.getServiceProvider().getEmail().equals(email)) {
            throw new RuntimeException("Cannot update booking of another provider");
        }

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    // Delete a booking
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id, Authentication authentication) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String email = authentication.getName();
        if (!booking.getServiceProvider().getEmail().equals(email)) {
            throw new RuntimeException("Cannot delete booking of another provider");
        }

        bookingRepository.deleteById(id);
    }
}
