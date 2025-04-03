package com.example.UtilityProject.controller;

import com.example.UtilityProject.service.Discount.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")// Adjust for your frontend
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

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
    public ResponseEntity<Map<String, Boolean>> verifyOtp(@RequestBody Map<String, String> request) {
        boolean isValid = authenticationService.verifyOtp(request.get("email"), request.get("otp"));

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
}

