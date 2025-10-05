package com.moneytracker.service;

import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {

    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public TransactionService(){
        initializeDummyData();
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
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
}
