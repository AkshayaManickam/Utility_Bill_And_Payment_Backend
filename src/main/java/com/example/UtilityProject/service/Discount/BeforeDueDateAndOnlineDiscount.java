package com.example.UtilityProject.service.Discount;

import org.springframework.stereotype.Component;

@Component("beforeDueDateAndOnline")
public class BeforeDueDateAndOnlineDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount + (totalAmount * 0.10); // 10% additional charge
    }
}
