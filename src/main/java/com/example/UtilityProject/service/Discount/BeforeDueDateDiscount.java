package com.example.UtilityProject.service.Discount;

import org.springframework.stereotype.Component;

@Component("beforeDuedate")
public class BeforeDueDateDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - (totalAmount * 0.05); // 5% discount
    }
}
