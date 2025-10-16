package com.moneytracker.service;

import com.moneytracker.model.Transaction;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(TransactionService.class)
class TransactionServiceTest {

  @Autowired
  private TransactionService transactionService;

  @Test
  void getAllTransactions() {
    List<Transaction> transactions = transactionService.getAllTransactions();
    Assertions.assertEquals(4, transactions.size());
  }

  @Test
  void getTransactionById() {
    Optional<Transaction> transaction = transactionService.getTransactionById(1L);
    Assertions.assertTrue(transaction.isPresent(), "Transaction with ID 1 should exist");
    Assertions.assertEquals(1L, transaction.get().getId());
  }

}