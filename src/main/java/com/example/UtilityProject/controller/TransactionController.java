package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.model.Payment;
import com.example.UtilityProject.model.PaymentMethod;
import com.example.UtilityProject.model.Transaction;
import com.example.UtilityProject.service.InvoiceService;
import com.example.UtilityProject.service.PaymentService;
import com.example.UtilityProject.service.TransactionService;
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

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping("/save")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
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
        return ResponseEntity.ok(savedTransaction);
    }
}
