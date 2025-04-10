package com.example.UtilityProject.service;

import com.example.UtilityProject.model.AuthenticationDetails;
import com.example.UtilityProject.model.Employee;
import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.repository.AuthenticationRepository;
import com.example.UtilityProject.repository.EmployeeRepository;
import com.example.UtilityProject.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
            invoice.setIsPaid(updatedInvoice.getIsPaid());
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

    public Invoice getInvoiceDetails(Long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        return invoice.orElse(null);  // Return null if invoice is not found
    }

    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Service
    public static class AuthenticationService {

        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private AuthenticationRepository authenticationRepository;

        private final Random random = new Random();

        public String generateOtp(String email) {
            Optional<Employee> employee = employeeRepository.findByEmail(email);

            if (employee.isEmpty()) {
                return "Email does not exist!";
            }

            String otp = String.valueOf(100000 + random.nextInt(900000)); // 6-digit OTP
            AuthenticationDetails authDetails = new AuthenticationDetails();
            authDetails.setEmail(email);
            authDetails.setOtp(otp);
            authDetails.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

            authenticationRepository.save(authDetails);
            return otp;
        }

        public boolean verifyOtp(String email, String otp) {
            Optional<AuthenticationDetails> authDetails = authenticationRepository.findByEmailAndOtp(email, otp);
            return authDetails.isPresent() && authDetails.get().getExpiresAt().isAfter(LocalDateTime.now());
        }
    }
}
