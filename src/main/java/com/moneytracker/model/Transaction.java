package com.moneytracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    
    @NotBlank(message = "Description is required")
    @Column(nullable = false, name = "description")
    private String description;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "10000000", message = "Amount must be lesser than 1 Cr")
    @Column(nullable = false, precision = 10, scale = 2, name = "amount")
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "transaction_type")
    private TransactionType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false , name = "category")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "account_type")
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @Column(name = "date", nullable = false)
    @NotNull(message = "date cannot be empty")
    private LocalDate date;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "fee", nullable = false)
    private BigDecimal serviceFee = BigDecimal.ZERO;

    public Transaction() {}
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public Transaction(Long id, String description, BigDecimal amount,
                       TransactionType type, Category category,AccountType accountType,LocalDate date,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.accountType = accountType;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public Transaction(String description, BigDecimal amount, TransactionType type, Category category, AccountType accountType, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.accountType = accountType;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType){
        this.accountType = accountType;
    }

    public BigDecimal getServiceFee(){return serviceFee;}

    public void setServiceFee(BigDecimal serviceFee){this.serviceFee = serviceFee;}

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
