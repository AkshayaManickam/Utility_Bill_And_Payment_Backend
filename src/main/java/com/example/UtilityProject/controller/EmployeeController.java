package com.example.UtilityProject.controller;


import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.repository.EmployeeRepository;
import com.example.UtilityProject.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin("*")  // Allow frontend requests
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getUniqueEmployees() {
        return ResponseEntity.ok(employeeService.getUniqueEmployees());
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
