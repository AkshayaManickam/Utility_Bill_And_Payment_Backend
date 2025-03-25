package com.example.UtilityProject.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "authentication_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public AuthenticationDetails() {
    }

    public AuthenticationDetails(Long id, String email, String otp, LocalDateTime expiresAt) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}

