package com.localservice.backend_local_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.localservice.backend_local_service.model.Role;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; // The role is now an enum

    // Corrected getters and setters for the Role enum
    public Role getRole() {
        return role;
    }
    @OneToMany(
            mappedBy = "serviceProvider",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Booking> bookingsAsProvider = new ArrayList<>();

    // 👇 ADD THIS: Relationship to bookings where the user is the job seeker
    @OneToMany(
            mappedBy = "jobSeeker",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Booking> bookingsAsSeeker = new ArrayList<>();


    public void setRole(Role role) {
        this.role = role;
    }

    // Other fields and methods
    private String password;
    private String phone;
    private String address;
    private String service;

    public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	// Existing getters/setters for other fields
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}