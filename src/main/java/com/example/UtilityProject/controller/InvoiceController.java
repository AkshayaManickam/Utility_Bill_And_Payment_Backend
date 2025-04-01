package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.InvoiceRepository;
import com.example.UtilityProject.repository.UserRepository;
import com.example.UtilityProject.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4200")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceService invoiceService;

    public InvoiceController(InvoiceRepository invoiceRepository, UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveInvoice(@RequestBody Invoice invoice) {
        // Fetch the user using serviceConnectionNumber
        Optional<User> userOptional = userRepository.findByServiceConnectionNo(invoice.getServiceConnectionNumber());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Service Connection Number");
        }
        invoice.setUser(userOptional.get());
        User user = userOptional.get();
        user.setUnitsConsumption(user.getUnitsConsumption());

        // Save the updated user
        userRepository.save(user);
        user.setUnitsConsumption(invoice.getUnitsConsumed());
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return ResponseEntity.ok(savedInvoice);
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getBillCount() {
        return ResponseEntity.ok(invoiceService.getBillCount());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice updatedInvoice) {
        System.out.println("Updating Invoice ID: " + id);
        System.out.println("Received Data: " + updatedInvoice);

        Invoice invoice = invoiceService.updateInvoice(id, updatedInvoice);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoiceDetails(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceDetails(invoiceId);
    }
}
