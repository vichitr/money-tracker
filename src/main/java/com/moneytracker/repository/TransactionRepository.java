package com.moneytracker.repository;

import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByType(TransactionType type);
    
    List<Transaction> findByCategory(Category category);
}

