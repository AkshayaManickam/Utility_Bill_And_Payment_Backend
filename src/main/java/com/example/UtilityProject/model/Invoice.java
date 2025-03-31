package com.example.UtilityProject.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "Service Connection Number is required")
    private String serviceConnectionNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_customer"))
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    @Min(value = 1, message = "Units consumed must be at least 1")
    private int unitsConsumed;

    @Column(nullable = false)
    @Min(value = 0, message = "Total amount must be non-negative")
    private double totalAmount;

    @Column(nullable = false)
    private LocalDate billGeneratedDate;

    @Column(nullable = false)
    private LocalDate dueDate;



    @Column(nullable = false, length = 10)
    private String isPaid = "Not Paid"; // Default value

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceConnectionNumber() {
        return serviceConnectionNumber;
    }

    public void setServiceConnectionNumber(String serviceConnectionNumber) {
        this.serviceConnectionNumber = serviceConnectionNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(int unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getBillGeneratedDate() {
        return billGeneratedDate;
    }

    public void setBillGeneratedDate(LocalDate billGeneratedDate) {
        this.billGeneratedDate = billGeneratedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

