package com.moneytracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.moneytracker.dao.TransactionDAO;
import com.moneytracker.model.AccountType;
import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import com.moneytracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @MockBean
  private TransactionDAO transactionDAO;

  @InjectMocks
  private TransactionService transactionService;


  void getAllTransactions() {
    // Arrange
    Transaction t1 = new Transaction("T1", BigDecimal.TEN, TransactionType.INCOME, Category.SALARY,AccountType.CASH, LocalDate.of(2025,12,11));
    Transaction t2 = new Transaction("T2", BigDecimal.ONE, TransactionType.EXPENSE, Category.FOOD,AccountType.CREDIT_CARD, LocalDate.of(2025,12,26));
    when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2));
    when(transactionDAO.executeQuery(any())).thenReturn(List.of(t1, t2));
    doReturn(List.of(t1, t2)).when(transactionDAO).executeQuery(anyString(), any());

    // Act
//    List<Transaction> result = transactionService.getAllTransactions();

    // Assert
//    assertEquals(2, result.size());
    verify(transactionRepository).findAll();
  }


  void getTransactionById() {
    // Arrange
    Long id = 1L;
    Transaction t1 = new Transaction("T1", BigDecimal.TEN, TransactionType.INCOME, Category.SALARY, AccountType.WALLET,LocalDate.of(2025,9,25));
    t1.setId(id);
    when(transactionRepository.findById(id)).thenReturn(Optional.of(t1));

    // Act
    Optional<Transaction> result = transactionService.getTransactionById(id);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    verify(transactionRepository).findById(id);
  }


  void createTransaction() {
    // Arrange
    Transaction t1 = new Transaction("T1", BigDecimal.TEN, TransactionType.INCOME, Category.SALARY, AccountType.BANK_TRANSFER, LocalDate.of(2024,9,14));
    when(transactionRepository.save(any(Transaction.class))).thenReturn(t1);

    // Act
    Transaction result = transactionService.createTransaction(t1);

    // Assert
    assertEquals("T1", result.getDescription());
    verify(transactionRepository).save(t1);
  }
}
