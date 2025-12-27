package com.moneytracker.service;

import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionSummary;
import com.moneytracker.model.TransactionType;
import com.moneytracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    // validate user inputs
    if (transaction.getDescription().isBlank()) {
      throw new RuntimeException("Trxn description cant be blank");
    }
    if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0
        || transaction.getAmount().compareTo(new BigDecimal("10000000")) > 0) {
      throw new IllegalArgumentException("can't be less than zero and greater than 1Cr");
    }
    if (transaction.getType() == null) {
      throw new RuntimeException("Trxn Type can't be null");
    }
    if(transaction.getCategory() == null) {
      throw new RuntimeException("Category can't be empty");
    }
    if(transaction.getAccountType() == null){
      throw new RuntimeException("provide account type");
    }
    if(transaction.getDate() == null || transaction.getDate().isAfter(LocalDate.now())){
      throw new RuntimeException("Date can't be empty or in future");
    }

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
