package com.example.UtilityProject.service;

import com.example.UtilityProject.model.AuditLog;
import com.example.UtilityProject.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String actor, String action, String target, String details) {
        AuditLog log = AuditLog.builder()
                .actor(actor)
                .action(action)
                .target(target)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    public void logWithValues(String actor, String action, String target, String oldValue, String newValue, String details) {
        AuditLog log = AuditLog.builder()
                .actor(actor)
                .action(action)
                .target(target)
                .oldValue(oldValue)
                .newValue(newValue)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }
}
