package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.AuditLog;
import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.AuditLogRepository;
import com.example.UtilityProject.repository.UserRepository;
import com.example.UtilityProject.service.AuditLogService;
import com.example.UtilityProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")  // Allow frontend requests
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUniqueUsers() {
        return ResponseEntity.ok(userService.getUniqueUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(
            @RequestBody User user,
            @RequestHeader("X-EMPLOYEE-EMAIL") String employeeEmail
    ) {
        if (user.getCreditCardBalance() == 0) user.setCreditCardBalance(50000.0);
        if (user.getDebitCardBalance() == 0) user.setDebitCardBalance(30000.0);
        if (user.getWalletBalance() == 0) user.setWalletBalance(20000.0);
        User savedUser = userRepository.save(user);
        auditLogService.log(
                employeeEmail,
                "ADD_USER",
                "USER_ID: " + savedUser.getCustomerId(),
                "User added with initial balances"
        );
        return ResponseEntity.ok(savedUser);
    }



    @PostMapping("/bulk-upload")
    public ResponseEntity<Map<String, Object>> bulkUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("employeeId") String employeeId
    ) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("message", "Please upload a CSV file.");
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println("Received file: " + file.getOriginalFilename());
        System.out.println("Uploaded by employee ID: " + employeeId);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            List<User> users = new ArrayList<>();
            List<String> skippedRecords = new ArrayList<>();

            for (CSVRecord record : records) {
                try {
                    User user = new User();
                    user.setCustomerId(record.get("customer_id"));
                    user.setName(record.get("name"));
                    user.setServiceConnectionNo(record.get("service_connection_no"));
                    user.setEmail(record.get("email"));
                    user.setPhone(record.get("phone"));
                    user.setAddress(record.get("address"));
                    user.setUnitsConsumption(Integer.parseInt(record.get("units_consumption")));
                    user.setStartDate(record.get("start_date"));

                    user.setCreditCardBalance(50000.0);
                    user.setDebitCardBalance(30000.0);
                    user.setWalletBalance(20000.0);

                    if (!userRepository.existsByCustomerId(user.getCustomerId())) {
                        users.add(user);
                    } else {
                        skippedRecords.add(user.getCustomerId());
                    }
                } catch (Exception e) {
                    skippedRecords.add("Error in row: " + record.toString());
                }
            }

            userRepository.saveAll(users);
            String auditDetails = String.format(
                    "Bulk upload by employeeId: %s | File: %s | Inserted: %d | Skipped: %d",
                    employeeId,
                    file.getOriginalFilename(),
                    users.size(),
                    skippedRecords.size()
            );

            AuditLog auditLog = AuditLog.builder()
                    .actor(employeeId)
                    .action("BULK_UPLOAD")
                    .target("User")
                    .details(auditDetails)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);

            response.put("message", skippedRecords.isEmpty() ? "Bulk upload successful!" : "Bulk upload completed with some skipped rows");
            response.put("skipped", skippedRecords);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            response.put("message", "Error processing CSV file: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user,
            @RequestHeader("X-EMPLOYEE-EMAIL") String employeeEmail) {

        User updatedUser = userService.updateUser(id, user, employeeEmail);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestHeader("X-EMPLOYEE-EMAIL") String employeeEmail) {
        userService.deleteUser(id, employeeEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(userService.getUserCount());
    }

    @GetMapping("/consumption/{serviceConnectionNo}")
    public ResponseEntity<Integer> getUnitsConsumed(@PathVariable String serviceConnectionNo) {
        int unitsConsumed = userService.getUnitsConsumedByServiceConnectionNo(serviceConnectionNo);
        return ResponseEntity.ok(unitsConsumed);
    }


}
