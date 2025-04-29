package com.example.UtilityProject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "customerId"),
        @UniqueConstraint(columnNames = "serviceConnectionNo"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer ID is required")
    @Column(nullable = false, unique = true)
    private String customerId;

    @NotBlank(message = "Service Connection Number is required")
    @Column(nullable = false, unique = true)
    private String serviceConnectionNo;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email must be a valid format"
    )
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone must be a 10-digit number starting with 6-9"
    )
    @Column(nullable = false, unique = true)
    private String phone;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    @Min(value = 0, message = "Units consumption must be non-negative")
    @Column(nullable = false)
    private int unitsConsumption;

    @NotBlank(message = "Start date is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Start date must be in the format YYYY-MM-DD"
    )
    @Column(nullable = false)
    private String startDate;

    @Min(value = 0, message = "Credit card balance must be non-negative")
    @Column(nullable = false)
    private double creditCardBalance = 50000.0;

    @Min(value = 0, message = "Debit card balance must be non-negative")
    @Column(nullable = false)
    private double debitCardBalance = 30000.0;

    @Min(value = 0, message = "Wallet balance must be non-negative")
    @Column(nullable = false)
    private double walletBalance = 20000.0;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getServiceConnectionNo() { return serviceConnectionNo; }
    public void setServiceConnectionNo(String serviceConnectionNo) { this.serviceConnectionNo = serviceConnectionNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getUnitsConsumption() { return unitsConsumption; }
    public void setUnitsConsumption(int unitsConsumption) { this.unitsConsumption = unitsConsumption; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public double getCreditCardBalance() { return creditCardBalance; }
    public void setCreditCardBalance(double creditCardBalance) { this.creditCardBalance = creditCardBalance; }

    public double getDebitCardBalance() { return debitCardBalance; }
    public void setDebitCardBalance(double debitCardBalance) { this.debitCardBalance = debitCardBalance; }

    public double getWalletBalance() { return walletBalance; }
    public void setWalletBalance(double walletBalance) { this.walletBalance = walletBalance; }
}
