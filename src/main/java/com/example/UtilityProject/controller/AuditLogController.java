package com.example.UtilityProject.controller;




import com.example.UtilityProject.model.AuditLog;
import com.example.UtilityProject.repository.AuditLogRepository;
import com.example.UtilityProject.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow CORS from all origins (modify as needed)
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Autowired
    private AuditLogService auditLogService;


    @GetMapping("/audit-logs/employee/{employeeId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByEmployee(@PathVariable String employeeId) {
        List<AuditLog> logs = auditLogRepository.findByTargetOrderByTimestampDesc(employeeId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/audit-logs/user/{userIdentifier}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(@PathVariable String userIdentifier) {
        List<AuditLog> logs = auditLogService.getLogsByTarget(userIdentifier);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/audit-logs/invoice/{invoiceId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByInvoiceId(@PathVariable Long invoiceId) {
        String target1 = "Invoice-" + invoiceId;
        String target2 = String.valueOf(invoiceId);

        List<AuditLog> logs1 = auditLogRepository.findByTargetOrderByTimestampDesc(target1);
        List<AuditLog> logs2 = auditLogRepository.findByTargetOrderByTimestampDesc(target2);

        // Merge both lists
        List<AuditLog> mergedLogs = new ArrayList<>();
        mergedLogs.addAll(logs1);
        mergedLogs.addAll(logs2);

        // Sort merged logs by timestamp descending
        mergedLogs.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        if (mergedLogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(mergedLogs);
    }


    @GetMapping("/audit-logs/request/{helpId}")
    public ResponseEntity<List<AuditLog>> getHelpAuditLogs(@PathVariable Long helpId) {
        String helpTarget = "Help-" + helpId;
        List<AuditLog> logs = auditLogRepository.findByTargetOrderByTimestampDesc(helpTarget);
        return logs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(logs);
    }






}

