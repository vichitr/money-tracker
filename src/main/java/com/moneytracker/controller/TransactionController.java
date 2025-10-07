package com.moneytracker.controller;

import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import com.moneytracker.model.Category;
import com.moneytracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    // In-memory storage for demo purposes
    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private TransactionService transactionService;
    
    // Initialize with some dummy data
    public TransactionController() {
        initializeDummyData();
    }
    
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionsById(@PathVariable long id) {
        Optional<Transaction> transaction = transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
        
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        transaction.setId(counter.incrementAndGet());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        
        transactions.add(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, 
                                                       @Valid @RequestBody Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
        
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            transaction.setDescription(updatedTransaction.getDescription());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setType(updatedTransaction.getType());
            transaction.setCategory(updatedTransaction.getCategory());
            transaction.setUpdatedAt(LocalDateTime.now());
            
            return ResponseEntity.ok(transaction);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        boolean removed = transactions.removeIf(t -> t.getId().equals(id));
        
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/summary")
    public ResponseEntity<TransactionSummary> getTransactionSummary() {
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal balance = totalIncome.subtract(totalExpense);
        
        TransactionSummary summary = new TransactionSummary(totalIncome, totalExpense, balance, transactions.size());
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable TransactionType type) {
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(t -> t.getType() == type)
                .toList();
        
        return ResponseEntity.ok(filteredTransactions);
    }
    
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory(@PathVariable Category category) {
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(t -> t.getCategory() == category)
                .toList();
        
        return ResponseEntity.ok(filteredTransactions);
    }
    
    private void initializeDummyData() {
        // Add some sample transactions
        Transaction transaction1 = new Transaction("Salary", new BigDecimal("5000.00"), TransactionType.INCOME, Category.SALARY);
        transaction1.setId(counter.incrementAndGet());
        transactions.add(transaction1);
        
        Transaction transaction2 = new Transaction("Grocery Shopping", new BigDecimal("150.50"), TransactionType.EXPENSE, Category.FOOD);
        transaction2.setId(counter.incrementAndGet());
        transactions.add(transaction2);
        
        Transaction transaction3 = new Transaction("Gas Bill", new BigDecimal("75.00"), TransactionType.EXPENSE, Category.BILLS);
        transaction3.setId(counter.incrementAndGet());
        transactions.add(transaction3);
        
        Transaction transaction4 = new Transaction("Freelance Project", new BigDecimal("800.00"), TransactionType.INCOME, Category.FREELANCE);
        transaction4.setId(counter.incrementAndGet());
        transactions.add(transaction4);
    }
    
    // Inner class for summary response
    public static class TransactionSummary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal balance;
        private int transactionCount;
        
        public TransactionSummary(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance, int transactionCount) {
            this.totalIncome = totalIncome;
            this.totalExpense = totalExpense;
            this.balance = balance;
            this.transactionCount = transactionCount;
        }
        
        // Getters
        public BigDecimal getTotalIncome() { return totalIncome; }
        public BigDecimal getTotalExpense() { return totalExpense; }
        public BigDecimal getBalance() { return balance; }
        public int getTransactionCount() { return transactionCount; }
    }
}
