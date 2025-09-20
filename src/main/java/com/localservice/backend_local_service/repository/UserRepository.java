package com.localservice.backend_local_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.localservice.backend_local_service.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = 'ROLE_SERVICE_PROVIDER'")
    List<User> findAllServiceProviders();
    
}
