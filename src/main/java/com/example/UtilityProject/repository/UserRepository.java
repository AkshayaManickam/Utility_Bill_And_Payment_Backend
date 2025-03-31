package com.example.UtilityProject.repository;

import com.example.UtilityProject.model.User;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT u FROM User u")
    List<User> findDistinctUsers();

    // Check if a customer exists by customerId
    boolean existsByCustomerId(String customerId);

    // Check if a customer exists by email
    boolean existsByEmail(String email);

    Optional<User> findByServiceConnectionNo(String serviceConnectionNo);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
}
