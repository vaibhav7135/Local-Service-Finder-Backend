package com.localservice.backend_local_service.repository;



import com.localservice.backend_local_service.model.Booking;

public class BookingResponse {
    private Long id;
    private String userName;
    private String providerName;
    private String serviceName;
    private String status;
    private String requestedAt;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.userName = booking.getJobSeeker() != null ? booking.getJobSeeker().getName() : null;
        this.providerName = booking.getServiceProvider() != null ? booking.getServiceProvider().getName() : null;
        this.serviceName = booking.getServiceName();
        this.status = booking.getStatus();
        this.requestedAt = booking.getRequestedAt() != null ? booking.getRequestedAt().toString() : null;
    }

    // getters
    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getProviderName() { return providerName; }
    public String getServiceName() { return serviceName; }
    public String getStatus() { return status; }
    public String getRequestedAt() { return requestedAt; }
}
