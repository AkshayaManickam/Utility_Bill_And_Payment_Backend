package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.AuditLog;
import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.AuditLogRepository;
import com.example.UtilityProject.repository.InvoiceRepository;
import com.example.UtilityProject.repository.UserRepository;
import com.example.UtilityProject.service.InvoiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private AuditLogRepository auditLogRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    public InvoiceController(InvoiceRepository invoiceRepository, UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveInvoice(
            @RequestBody Invoice invoice,
            @RequestParam("loggedInEmpId") String loggedInEmpId) throws JsonProcessingException {
        Optional<User> userOptional = userRepository.findByServiceConnectionNo(invoice.getServiceConnectionNumber());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Service Connection Number");
        }
        User user = userOptional.get();
        Long userId = user.getId();
        LocalDate billDate = invoice.getBillGeneratedDate();
        int month = billDate.getMonthValue();
        int year = billDate.getYear();
        List<Invoice> existingInvoices = invoiceRepository.findByUserIdAndMonthAndYear(userId, month, year);
        if (!existingInvoices.isEmpty()) {
            return ResponseEntity.badRequest().body("Bill already generated for this user this month.");
        }
        if (invoice.getTotalAmount() == 0) {
            invoice.setIsPaid("EXCEPTION");
        }
        user.setUnitsConsumption(invoice.getUnitsConsumed());
        userRepository.save(user);
        invoice.setUser(user);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        ObjectMapper mapper = new ObjectMapper();
        String newValue = objectMapper.writeValueAsString(savedInvoice);
        AuditLog auditLog = AuditLog.builder()
                .actor(loggedInEmpId)
                .action("CREATE_INVOICE")
                .target("Invoice-" + savedInvoice.getId())
                .newValue(newValue)
                .oldValue(null)
                .details("New invoice generated for user ID: " + userId)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);
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
    public Invoice updateInvoice(
            @PathVariable Long id,
            @RequestBody Invoice updatedInvoice,
            @RequestParam("loggedInEmpId") String loggedInEmpId) throws JsonProcessingException {
        //ObjectMapper mapper = new ObjectMapper();
        Invoice oldInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
        String oldValueJson = objectMapper.writeValueAsString(oldInvoice);
        Invoice savedInvoice = invoiceService.updateInvoice(id, updatedInvoice);
        String newValueJson = objectMapper.writeValueAsString(savedInvoice);
        AuditLog auditLog = AuditLog.builder()
                .actor(loggedInEmpId)
                .action("UPDATE_INVOICE")
                .target(savedInvoice.getId().toString())
                .oldValue(oldValueJson)
                .newValue(newValueJson)
                .details("Invoice updated successfully")
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(auditLog);
        return savedInvoice;
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoiceDetails(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceDetails(invoiceId);
    }
}
