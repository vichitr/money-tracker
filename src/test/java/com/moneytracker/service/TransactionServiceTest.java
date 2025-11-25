package com.moneytracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import com.moneytracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionService transactionService;

  @Test
  void getAllTransactions() {
    // Arrange
    Transaction t1 = new Transaction("T1", BigDecimal.TEN, TransactionType.INCOME, Category.SALARY);
    Transaction t2 = new Transaction("T2", BigDecimal.ONE, TransactionType.EXPENSE, Category.FOOD);
    when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

    // Act
    List<Transaction> result = transactionService.getAllTransactions();

    // Assert
    assertEquals(2, result.size());
    verify(transactionRepository).findAll();
  }

  @Test
  void getTransactionById() {
    // Arrange
    Long id = 1L;
    Transaction t1 = new Transaction("T1", BigDecimal.TEN, TransactionType.INCOME, Category.SALARY);
    t1.setId(id);
    when(transactionRepository.findById(id)).thenReturn(Optional.of(t1));

    // Act
    Optional<Transaction> result = transactionService.getTransactionById(id);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    verify(transactionRepository).findById(id);
  }

  @Test
  void createTransaction() {
    // Arrange
    Transaction t1 = new Transaction("T1", BigDecimal.TEN, TransactionType.INCOME, Category.SALARY);
    when(transactionRepository.save(any(Transaction.class))).thenReturn(t1);

    // Act
    Transaction result = transactionService.createTransaction(t1);

    // Assert
    assertEquals("T1", result.getDescription());
    verify(transactionRepository).save(t1);
  }
}
