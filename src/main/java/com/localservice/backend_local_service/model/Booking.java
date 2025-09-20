package com.localservice.backend_local_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Job Seeker who made the request
    @ManyToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private User jobSeeker;

    // Service Provider being requested
    @ManyToOne
    @JoinColumn(name = "service_provider_id", nullable = false)
    private User serviceProvider;

    // Store the service name directly from the provider
    private String serviceName;

    // Booking status: PENDING, ACCEPTED, DECLINED
    private String status = "PENDING";

    // When the booking was requested
    private LocalDateTime requestedAt = LocalDateTime.now();

    // Constructors
    public Booking() {}

    public Booking(User jobSeeker, User serviceProvider, String serviceName) {
        this.jobSeeker = jobSeeker;
        this.serviceProvider = serviceProvider;
        this.serviceName = serviceName;
        this.status = "PENDING";
        this.requestedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getJobSeeker() { return jobSeeker; }
    public void setJobSeeker(User jobSeeker) { this.jobSeeker = jobSeeker; }

    public User getServiceProvider() { return serviceProvider; }
    public void setServiceProvider(User serviceProvider) { this.serviceProvider = serviceProvider; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
