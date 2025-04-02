package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.PaymentMethod;
import com.example.UtilityProject.model.Transaction;
import com.example.UtilityProject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4200") // Update if frontend is hosted elsewhere
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/save")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
        transaction.setPaymentMethod(PaymentMethod.CASH);
        transaction.setTransactionStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }
}
