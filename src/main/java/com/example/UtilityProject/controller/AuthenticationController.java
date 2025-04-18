package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.repository.EmployeeRepository;
import com.example.UtilityProject.service.AuditLogService;
import com.example.UtilityProject.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = authenticationService.generateOtp(email);
        Map<String, String> response = new HashMap<>();

        if (otp.equals("Email does not exist!")) {
            response.put("message", otp);
            return ResponseEntity.badRequest().body(response);
        }

        if (otp.equals("User already logged in. Cannot generate OTP.")) {
            response.put("message", "User already has an active session.");
            return ResponseEntity.status(409).body(response); // << Show toastr from Angular
        }

        response.put("message", "OTP Sent Successfully");
        response.put("otp", otp); // Only for development
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request,
                                                         HttpSession session) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = authenticationService.verifyOtp(email, otp);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);
            employeeOpt.ifPresent(emp -> {
                response.put("employeeId", emp.getEmployeeId());
                response.put("sessionId", session.getId());
                session.setAttribute("email", email);  // Store email in the session

                // Create Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                        );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);

                session.setAttribute("SPRING_SECURITY_CONTEXT", context);  // Store the context in session

                sessionRegistry.registerNewSession(session.getId(), email);

                auditLogService.log(
                        email,
                        "LOGIN",
                        "EMPLOYEE_ID: " + emp.getEmployeeId(),
                        "OTP verified and login successful"
                );
            });
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        String sessionId = session.getId();
        // Invalidate the session (removes the session context)
        session.invalidate();



        // Expire the session from the session registry
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            sessions.forEach(si -> {
                if (si.getSessionId().equals(sessionId)) {
                    si.expireNow(); // Mark session as expired
                }
            });
        });

        // Return a JSON response instead of a plain string
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session-status")
    public ResponseEntity<Map<String, Object>> sessionStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String email = (String) session.getAttribute("email");

        if (email != null) {
            response.put("isLoggedIn", true);
            response.put("email", email);
        } else {
            response.put("isLoggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}
