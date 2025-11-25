package com.moneytracker.service;

import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionSummary;
import com.moneytracker.model.TransactionType;
import com.moneytracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionService {

  private final TransactionRepository transactionRepository;

  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Transactional(readOnly = true)
  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<Transaction> getTransactionById(long id) {
    return transactionRepository.findById(id);
  }

  public Transaction createTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  public Optional<Transaction> updateTransaction(Long id, Transaction updatedTransaction) {
    Optional<Transaction> existingTransaction = transactionRepository.findById(id);

    if (existingTransaction.isPresent()) {
      Transaction transaction = existingTransaction.get();
      transaction.setDescription(updatedTransaction.getDescription());
      transaction.setAmount(updatedTransaction.getAmount());
      transaction.setType(updatedTransaction.getType());
      transaction.setCategory(updatedTransaction.getCategory());

      return Optional.of(transactionRepository.save(transaction));
    }
    return Optional.empty();
  }

  public boolean deleteTransaction(Long id) {
    if (transactionRepository.existsById(id)) {
      transactionRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Transactional(readOnly = true)
  public TransactionSummary getTransactionSummary() {
    List<Transaction> transactions = transactionRepository.findAll();
    
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

  @Transactional(readOnly = true)
  public List<Transaction> getTransactionsByType(TransactionType type) {
    return transactionRepository.findByType(type);
  }

  @Transactional(readOnly = true)
  public List<Transaction> getTransactionsByCategory(Category category) {
    return transactionRepository.findByCategory(category);
  }
}
