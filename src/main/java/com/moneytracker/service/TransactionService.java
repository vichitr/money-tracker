package com.moneytracker.service;

import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionSummary;
import com.moneytracker.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final List<Transaction> transactions = new ArrayList<>();
  private final AtomicLong counter = new AtomicLong();

  public TransactionService() {
    initializeDummyData();
  }

  public List<Transaction> getAllTransactions() {
    return transactions;
  }

  public Optional<Transaction> getTransactionById(long id) {
    Optional<Transaction> transaction = transactions.stream()
        .filter(t -> t.getId().equals(id))
        .findFirst();
    return transaction;
  }

  public Transaction createTransaction(Transaction transaction) {
    transaction.setId(counter.incrementAndGet());
    transaction.setCreatedAt(LocalDateTime.now());
    transaction.setUpdatedAt(LocalDateTime.now());

    transactions.add(transaction);
    return transaction;
  }

  public Optional<Transaction> updateTransaction(Long id, Transaction updatedTransaction) {
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

      return Optional.of(transaction);
    }
    return Optional.empty();
  }

  public boolean deleteTransaction(Long id) {
    boolean removed = transactions.removeIf(t -> t.getId().equals(id));
    return removed;
  }

  public TransactionSummary getTransactionSummary() {
    BigDecimal totalIncome = transactions.stream()
        .filter(t -> t.getType() == TransactionType.INCOME)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalExpense = transactions.stream()
        .filter(t -> t.getType() == TransactionType.EXPENSE)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal balance = totalIncome.subtract(totalExpense);

    return new TransactionSummary(totalIncome, totalExpense, balance, transactions.size());
  }

  public List<Transaction> getTransactionsByType(TransactionType type) {
    List<Transaction> filteredTransactions = transactions.stream()
        .filter(t -> t.getType() == type)
        .toList();
    return filteredTransactions;
  }

  public List<Transaction> getTransactionsByCategory(Category category) {
    List<Transaction> filteredTransactions = transactions.stream()
        .filter(t -> t.getCategory() == category)
        .toList();
    return filteredTransactions;
  }

  private void initializeDummyData() {
    // Add some sample transactions
    Transaction transaction1 = new Transaction("Salary", new BigDecimal("5000.00"),
        TransactionType.INCOME, Category.SALARY);
    transaction1.setId(counter.incrementAndGet());
    transactions.add(transaction1);

    Transaction transaction2 = new Transaction("Grocery Shopping", new BigDecimal("150.50"),
        TransactionType.EXPENSE, Category.FOOD);
    transaction2.setId(counter.incrementAndGet());
    transactions.add(transaction2);

    Transaction transaction3 = new Transaction("Gas Bill", new BigDecimal("75.00"),
        TransactionType.EXPENSE, Category.BILLS);
    transaction3.setId(counter.incrementAndGet());
    transactions.add(transaction3);

    Transaction transaction4 = new Transaction("Freelance Project", new BigDecimal("800.00"),
        TransactionType.INCOME, Category.FREELANCE);
    transaction4.setId(counter.incrementAndGet());
    transactions.add(transaction4);
  }
}
