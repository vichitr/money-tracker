package com.moneytracker.controller;

import com.moneytracker.model.Category;
import com.moneytracker.model.ListTransactionRequest;
import com.moneytracker.model.ListTransactionResponse;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionSummary;
import com.moneytracker.model.TransactionType;
import com.moneytracker.service.TransactionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

  @Autowired private TransactionService transactionService;

  @GetMapping
  public ResponseEntity<List<Transaction>> getAllTransactions() {
    return ResponseEntity.ok(transactionService.getAllTransactions());
  }

  @PostMapping("/list")
  public ResponseEntity<ListTransactionResponse> listTransactions (
      @RequestBody ListTransactionRequest request) {
    ListTransactionResponse response = transactionService.listTransactions(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getTransactionById(@PathVariable long id) {
    Optional<Transaction> transaction = transactionService.getTransactionById(id);

    return transaction.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Transaction> createTransaction(
      @Valid @RequestBody Transaction transaction) {
    Transaction createdTransaction = transactionService.createTransaction(transaction);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id,
      @Valid @RequestBody Transaction updatedTransaction) {
    Optional<Transaction> existingTransaction = transactionService.updateTransaction(id,
        updatedTransaction);

    return existingTransaction.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());

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
  public ResponseEntity<List<Transaction>> getTransactionsByType(
      @PathVariable TransactionType type) {
    List<Transaction> filteredTransactions = transactionService.getTransactionsByType(type);

    return ResponseEntity.ok(filteredTransactions);
  }

  @GetMapping("/by-category/{category}")
  public ResponseEntity<List<Transaction>> getTransactionsByCategory(
      @PathVariable Category category) {
    List<Transaction> filteredTransactions = transactionService.getTransactionsByCategory(category);

    return ResponseEntity.ok(filteredTransactions);
  }
}
