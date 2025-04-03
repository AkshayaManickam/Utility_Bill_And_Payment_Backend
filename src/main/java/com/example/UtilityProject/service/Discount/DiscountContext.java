package com.example.UtilityProject.service.Discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DiscountContext {
    private final Map<String, DiscountStrategy> strategyMap;

    @Autowired
    public DiscountContext(Map<String, DiscountStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public double calculateDiscountedAmount(String discountType, double totalAmount) {
        DiscountStrategy strategy = strategyMap.get(discountType);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid discount type selected!");
        }
        return strategy.applyDiscount(totalAmount);
    }
}
