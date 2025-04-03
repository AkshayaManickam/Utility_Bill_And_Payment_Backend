package com.example.UtilityProject.service.Discount;


import org.springframework.stereotype.Component;

@Component("afterDueDate")
public class AfterDueDateDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount + (totalAmount * 0.05); // 5% additional charge
    }
}
