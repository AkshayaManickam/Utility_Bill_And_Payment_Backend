package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.InvoiceRepository;
import com.example.UtilityProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4200")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveInvoice(@RequestBody Invoice invoice) {
        if (invoice.getUser() == null || invoice.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        // Fetch the user from the database
        Optional<User> userOptional = userRepository.findById(invoice.getUser().getId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid User ID");
        }

        invoice.setUser(userOptional.get()); // Set the fetched user
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return ResponseEntity.ok(savedInvoice);
    }

}
