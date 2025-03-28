package com.example.UtilityProject.repository;

import com.example.UtilityProject.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
