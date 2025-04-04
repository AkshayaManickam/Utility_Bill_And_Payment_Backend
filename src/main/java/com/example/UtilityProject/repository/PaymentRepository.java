package com.example.UtilityProject.repository;


import com.example.UtilityProject.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE DATE(p.paymentDate) = CURRENT_DATE")
    Double getTotalAmountReceivedToday();
}
