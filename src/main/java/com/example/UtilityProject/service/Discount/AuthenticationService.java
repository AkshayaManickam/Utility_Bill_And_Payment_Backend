package com.example.UtilityProject.service.Discount;

import com.example.UtilityProject.model.AuthenticationDetails;
import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.repository.AuthenticationRepository;
import com.example.UtilityProject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    private final Random random = new Random();

    public String generateOtp(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (employee.isEmpty()) {
            return "Email does not exist!";
        }

        String otp = String.valueOf(100000 + random.nextInt(900000)); // 6-digit OTP
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setEmail(email);
        authDetails.setOtp(otp);
        authDetails.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

        authenticationRepository.save(authDetails);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<AuthenticationDetails> authDetails = authenticationRepository.findByEmailAndOtp(email, otp);
        return authDetails.isPresent() && authDetails.get().getExpiresAt().isAfter(LocalDateTime.now());
    }
}

