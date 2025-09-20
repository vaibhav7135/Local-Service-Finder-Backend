package com.localservice.backend_local_service.repository;

import com.localservice.backend_local_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByServiceProviderId(Long providerId);
    List<Booking> findByJobSeekerId(Long jobSeekerId);
}
