package com.example.UtilityProject.service;

import com.example.UtilityProject.model.AuthenticationDetails;
import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.repository.AuthenticationRepository;
import com.example.UtilityProject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    private final Random random = new Random();

    // Generate OTP if user is not logged in
    public String generateOtp(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (employee.isEmpty()) {
            return "Email does not exist!";
        }

        if (hasActiveSession(email)) {
            return "User already logged in. Cannot generate OTP.";
        }

        String otp = String.valueOf(100000 + random.nextInt(900000)); // Generate 6-digit OTP

        // Save OTP to the database with expiration time
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setEmail(email);
        authDetails.setOtp(otp);
        authDetails.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

        authenticationRepository.save(authDetails);
        return otp;
    }

    // Verify OTP - Returns true if valid and not expired
    public boolean verifyOtp(String email, String otp) {
        Optional<AuthenticationDetails> authDetails = authenticationRepository.findByEmailAndOtp(email, otp);

        if (authDetails.isEmpty()) {
            return false;  // OTP does not exist for the email
        }

        // Check if OTP is expired
        if (authDetails.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;  // OTP expired
        }

        return true;  // OTP valid
    }

    // Check if the user already has an active session
    public boolean hasActiveSession(String email) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            // Ensure the principal is a String (email in this case)
            if (principal instanceof String) {
                String loggedInEmail = (String) principal;
                if (loggedInEmail.equalsIgnoreCase(email)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    if (sessions != null && !sessions.isEmpty()) {
                        return true;  // Active session found for the user
                    }
                }
            }
        }
        return false;  // No active session found for the user
    }
}
