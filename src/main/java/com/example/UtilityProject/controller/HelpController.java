package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.AuditLog;
import com.example.UtilityProject.model.Help;
import com.example.UtilityProject.model.Status;
import com.example.UtilityProject.repository.AuditLogRepository;
import com.example.UtilityProject.repository.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/help")
@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
public class HelpController {

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Help>> getAllHelpRequests() {
        List<Help> helpRequests = helpRepository.findAll();
        if (helpRequests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(helpRequests);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Help> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<Help> helpRequest = helpRepository.findById(id);
        if (helpRequest.isPresent()) {
            Help help = helpRequest.get();
            String newStatus = request.get("status");
            String empId = request.get("empId");
            if (newStatus == null || empId == null) {
                return ResponseEntity.badRequest().body(null);
            }
            String oldStatus = help.getStatus().toString();
            help.setStatus(Status.valueOf(newStatus));
            Help updatedHelp = helpRepository.save(help);
            AuditLog audit = AuditLog.builder()
                    .actor(empId)
                    .action("HELP_STATUS_UPDATE")
                    .target("Help-" + id)
                    .oldValue("Status: " + oldStatus)
                    .newValue("Status: " + newStatus)
                    .details("Help ID " + id + " status changed by Emp ID " + empId)
                    .timestamp(LocalDateTime.now())
                    .build();
            auditLogRepository.save(audit);
            return ResponseEntity.ok(updatedHelp);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
