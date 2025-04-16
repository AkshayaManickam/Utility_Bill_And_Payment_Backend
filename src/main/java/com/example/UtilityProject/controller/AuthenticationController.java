package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.repository.EmployeeRepository;
import com.example.UtilityProject.service.AuditLogService;
import com.example.UtilityProject.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")// Adjust for your frontend
public class AuthenticationController {

    @Autowired
    private InvoiceService.AuthenticationService authenticationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditLogService auditLogService;



    @PostMapping ("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = authenticationService.generateOtp(email);

        Map<String, String> response = new HashMap<>();
        response.put("message", otp.equals("Email does not exist!") ? otp : "OTP Sent Successfully");
        response.put("otp", otp); // For development (Remove in production)
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = authenticationService.verifyOtp(email, otp);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);

            employeeOpt.ifPresent(emp -> {
                response.put("employeeId", emp.getEmployeeId());
                auditLogService.log(
                        email,                                 // actor
                        "LOGIN",                               // action
                        "EMPLOYEE_ID: " + emp.getEmployeeId(), // target
                        "OTP verified and login successful"    // details
                );
            });
        }
        return ResponseEntity.ok(response);
    }
}

