package com.example.UtilityProject.controller;


import com.example.UtilityProject.model.AuditLog;
import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.repository.AuditLogRepository;
import com.example.UtilityProject.repository.EmployeeRepository;
import com.example.UtilityProject.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin("*")  // Allow frontend requests
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping("/all-employees")
    public ResponseEntity<List<Employee>> getUniqueEmployees() {
        return ResponseEntity.ok(employeeService.getUniqueEmployees());
    }

    @PostMapping("/add-employee")
    public ResponseEntity<Employee> addEmployee(
            @RequestBody Employee employee,
            @RequestParam String loggedInEmpId) {

        System.out.println("Logged in Emp ID: " + loggedInEmpId);

        Employee savedEmployee = employeeRepository.save(employee);

        AuditLog auditLog = AuditLog.builder()
                .actor(loggedInEmpId)
                .action("ADD_EMPLOYEE")
                .target(employee.getEmployeeId())
                .details("New employee added")
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);

        return ResponseEntity.ok(savedEmployee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee,
            @RequestParam("loggedInEmpId") String loggedInEmpId) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Employee oldEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        String oldValueJson = mapper.writeValueAsString(oldEmployee);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        String newValueJson = mapper.writeValueAsString(updatedEmployee);
        AuditLog auditLog = AuditLog.builder()
                .actor(loggedInEmpId)
                .action("UPDATE_EMPLOYEE")
                .target(updatedEmployee.getEmployeeId())
                .oldValue(oldValueJson)
                .newValue(newValueJson)
                .details("Employee updated")
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(auditLog);
        return updatedEmployee;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id,
            @RequestParam("loggedInEmpId") String loggedInEmpId) throws JsonProcessingException {

        Employee employeeToDelete = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));

        ObjectMapper mapper = new ObjectMapper();
        String oldValue = mapper.writeValueAsString(employeeToDelete);

        // Perform soft delete by setting isDeleted = true
        employeeToDelete.setDeleted(true);
        employeeRepository.save(employeeToDelete); // Save updated employee

        AuditLog auditLog = AuditLog.builder()
                .actor(loggedInEmpId)
                .action("DELETE_EMPLOYEE")
                .target(employeeToDelete.getEmployeeId())
                .oldValue(oldValue)
                .newValue(null)
                .details("Employee marked as deleted (soft delete)")
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(auditLog);

        return ResponseEntity.noContent().build();
    }


}
