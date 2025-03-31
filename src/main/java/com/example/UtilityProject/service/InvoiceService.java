package com.example.UtilityProject.service;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public long getBillCount() {
        LocalDate today = LocalDate.now();
        return invoiceRepository.countBillsGeneratedToday(today);
    }

    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setUnitsConsumed(updatedInvoice.getUnitsConsumed());
            invoice.setTotalAmount(updatedInvoice.getTotalAmount());
            invoice.setDueDate(updatedInvoice.getDueDate());
            if (updatedInvoice.getIsPaid() != null) {
                invoice.setIsPaid(updatedInvoice.getIsPaid());
            }
            return invoiceRepository.save(invoice);
        } else {
            System.out.println("Invoice not found for ID: " + id);
        }
        return null;
    }

}
