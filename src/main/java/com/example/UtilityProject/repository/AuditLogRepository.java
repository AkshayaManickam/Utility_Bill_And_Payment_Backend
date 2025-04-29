package com.example.UtilityProject.repository;



import com.example.UtilityProject.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTargetOrderByTimestampDesc(String target);
    List<AuditLog> findByTargetContaining(String userId);  // this works too

}
