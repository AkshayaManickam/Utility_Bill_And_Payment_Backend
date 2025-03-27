package com.example.UtilityProject.repository;

import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM Employee u")
    List<Employee> findDistinctEmployees();


}
