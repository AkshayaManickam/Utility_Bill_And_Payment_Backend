package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.*;
import com.example.UtilityProject.repository.AuditLogRepository;
import com.example.UtilityProject.service.InvoiceService;
import com.example.UtilityProject.service.PaymentService;
import com.example.UtilityProject.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4200") // Update if frontend is hosted elsewhere
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping("/save")
    public ResponseEntity<Transaction> saveTransaction(
            @RequestBody Transaction transaction,
            @RequestParam("loggedInEmpId") String loggedInEmpId // <-- Add this parameter
    ) throws JsonProcessingException {

        Invoice invoice = invoiceService.findById(transaction.getInvoice().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        invoice.setIsPaid("PAID");
        invoiceService.saveInvoice(invoice);

        transaction.setPaymentMethod(PaymentMethod.CASH);
        transaction.setTransactionStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());
        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        Payment payment = new Payment();
        payment.setInvoiceId(invoice.getId());
        payment.setUserId(invoice.getUser().getId());
        payment.setAmountPaid(transaction.getAmountPaid());
        payment.setPaymentMethod(transaction.getPaymentMethod().toString());
        payment.setPaymentDate(LocalDateTime.now());
        paymentService.savePayment(payment);

        String newValueJson = objectMapper.writeValueAsString(savedTransaction);

        AuditLog auditLog = AuditLog.builder()
                .actor(loggedInEmpId)
                .action("SAVE_TRANSACTION")
                .target("Transaction-" + savedTransaction.getTransactionId())
                .newValue(newValueJson)
                .oldValue(null)
                .details("Transaction saved and payment recorded for Invoice ID: " + invoice.getId())
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);

        return ResponseEntity.ok(savedTransaction);
    }

}
