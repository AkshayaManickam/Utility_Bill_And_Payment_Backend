package com.example.UtilityProject.repository;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser(User user);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE CAST(i.billGeneratedDate AS date) = :date")
    long countBillsGeneratedToday(@Param("date") LocalDate date);

    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND FUNCTION('MONTH', i.billGeneratedDate) = :month AND FUNCTION('YEAR', i.billGeneratedDate) = :year")
    List<Invoice> findByUserIdAndMonthAndYear(@Param("userId") Long userId,
                                              @Param("month") int month,
                                              @Param("year") int year);


}
