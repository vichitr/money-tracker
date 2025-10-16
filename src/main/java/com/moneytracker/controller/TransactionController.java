package com.moneytracker.controller;

import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionSummary;
import com.moneytracker.model.TransactionType;
import com.moneytracker.model.Category;
import com.moneytracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable long id) {
        Optional<Transaction> transaction =transactionService.getTransactionById(id);
        
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, 
                                                       @Valid @RequestBody Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactionService.updateTransaction(id, updatedTransaction);

        if (existingTransaction.isPresent()) {
            return ResponseEntity.ok(existingTransaction.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        boolean removed = transactionService.deleteTransaction(id);
        
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/summary")
    public ResponseEntity<TransactionSummary> getTransactionSummary() {

        
        TransactionSummary summary = transactionService.getTransactionSummary();
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable TransactionType type) {
        List<Transaction> filteredTransactions =transactionService.getTransactionsByType(type);
        
        return ResponseEntity.ok(filteredTransactions);
    }
    
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory(@PathVariable Category category) {
        List<Transaction> filteredTransactions = transactionService.getTransactionsByCategory(category);
        
        return ResponseEntity.ok(filteredTransactions);
    }
}
