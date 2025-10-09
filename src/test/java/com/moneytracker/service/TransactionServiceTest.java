package com.moneytracker.service;

import com.moneytracker.model.Transaction;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TransactionService.class)
class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @Test
    void getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Assertions.assertEquals(4,transactions.size());
    }

    @Test
    void getTransactionById() {
        Optional<Transaction> transaction = transactionService.getTransactionById(1L);
        Assertions.assertTrue(transaction.isPresent(), "Transaction with ID 1 should exist");
        Assertions.assertEquals(1L, transaction.get().getId());
    }

    @Test
    void createTransaction(){
       var Transaction   = transactionService.createTransaction();

    }

    @Test
    void updateTransaction(){
        Optional<Transaction> existingTransaction = transactionService.updateTransaction(1L,);

    }

    @Test
    void deleteTransaction(){
        boolean removed = transactionService.deleteTransaction(1L);



    }

    @Test
    void getTransactionSummary(){
        var TransactionSummary = transactionService.getTransactionSummary();

    }

    @Test
    void getTransactionsByType(){
        List<Transaction> filteredTransactions = transactionService.getTransactionsByType();
        Assertions.assertEquals();
    }

    @Test
    void getTransactionByCategory(){
        List<Transaction> filteredTransactions = transactionService.getTransactionsByCategory();
    }





}