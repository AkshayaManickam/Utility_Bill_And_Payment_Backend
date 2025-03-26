package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.UserRepository;
import com.example.UtilityProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUniqueUsers() {
        return ResponseEntity.ok(userService.getUniqueUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<Map<String, Object>> bulkUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("message", "Please upload a CSV file.");
            return ResponseEntity.badRequest().body(response);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            List<User> users = new ArrayList<>();
            List<String> skippedRecords = new ArrayList<>();

            for (CSVRecord record : records) {
                try {
                    User user = new User();
                    user.setCustomerId(record.get("customer_id"));
                    user.setServiceConnectionNo(record.get("service_connection_no"));
                    user.setEmail(record.get("email"));
                    user.setPhone(record.get("phone"));
                    user.setAddress(record.get("address"));
                    user.setUnitsConsumption(Integer.parseInt(record.get("units_consumption")));
                    user.setStartDate(record.get("start_date"));

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

            if (!skippedRecords.isEmpty()) {
                response.put("message", "Bulk upload completed with some skipped records.");
                response.put("skipped", skippedRecords);
                return ResponseEntity.ok(response);  // ✅ Ensure response is always 200 OK
            } else {
                response.put("message", "Bulk upload successful!");
                return ResponseEntity.ok(response);  // ✅ Ensure success response
            }

        } catch (Exception e) {
            response.put("message", "Error processing CSV file: " + e.getMessage());
            return ResponseEntity.status(500).body(response);  // ✅ Handle critical failures properly
        }
    }

}
