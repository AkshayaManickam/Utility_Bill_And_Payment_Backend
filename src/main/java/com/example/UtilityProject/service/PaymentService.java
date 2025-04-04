package com.example.UtilityProject.service;


import com.example.UtilityProject.model.Payment;
import com.example.UtilityProject.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Double getTodayAmountReceived() {
        return paymentRepository.getTotalAmountReceivedToday();
    }
}
