package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Invoice;
import com.example.UtilityProject.service.Discount.DiscountContext;
import com.example.UtilityProject.service.InvoiceService;
import com.example.UtilityProject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final DiscountContext discountContext;
    private final InvoiceService invoiceService;

    public PaymentController(DiscountContext discountContext, InvoiceService invoiceService) {
        this.discountContext = discountContext;
        this.invoiceService = invoiceService;
    }

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculatePayment(@RequestParam Long invoiceId, @RequestParam String discountType) {
        try {
            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
            if (invoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            double amount = discountContext.calculateDiscountedAmount(discountType, invoice.getTotalAmount());
            return ResponseEntity.ok(amount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/today-amount")
    public Double getTodayAmountReceived() {
        return paymentService.getTodayAmountReceived();
    }

}