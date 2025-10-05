package com.moneytracker.service;

import com.moneytracker.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

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
}