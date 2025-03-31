package com.example.UtilityProject.repository;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser(User user);
}
