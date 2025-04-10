package com.example.UtilityProject.service;

import com.example.UtilityProject.repository.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InvoiceStatusScheduler {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostConstruct
    public void init() {
        System.out.println("Checking overdue invoices at startup...");
        updateOverdueInvoices();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleOverdueCheck() {
        System.out.println("Running scheduled overdue check...");
        updateOverdueInvoices();
    }

    private void updateOverdueInvoices() {
        LocalDate today = LocalDate.now();
        int updatedCount = invoiceRepository.updateOverdueInvoices(today);
        System.out.println("Overdue invoices updated: " + updatedCount);
    }
}
