package com.moneytracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytracker.model.AccountType;
import com.moneytracker.model.Category;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import com.moneytracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TransactionRepository transactionRepository;

  @BeforeEach
  void setUp() {
    transactionRepository.deleteAll();
    
    Transaction t1 = new Transaction("Salary", new BigDecimal("5000.00"), TransactionType.INCOME, Category.SALARY,
        AccountType.CASH, LocalDate.EPOCH);
    Transaction t2 = new Transaction("Grocery", new BigDecimal("150.50"), TransactionType.EXPENSE, Category.FOOD, AccountType.BANK_TRANSFER, LocalDate.EPOCH);
    Transaction t3 = new Transaction("Gas", new BigDecimal("75.00"), TransactionType.EXPENSE, Category.BILLS, AccountType.WALLET, LocalDate.MIN);
    Transaction t4 = new Transaction("Freelance", new BigDecimal("800.00"), TransactionType.INCOME, Category.FREELANCE, AccountType.REWARD_POINTS, LocalDate.EPOCH);
    
    transactionRepository.save(t1);
    transactionRepository.save(t2);
    transactionRepository.save(t3);
    transactionRepository.save(t4);
  }

  @Test
  void getAllTransactions_ShouldReturnOk() throws Exception {
    mockMvc.perform(get("/api/transactions"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(4));
  }

  @Test
  void getTransactionById_WithValidId_ShouldReturnTransaction() throws Exception {
    // Get ID from saved transaction
    Long id = transactionRepository.findAll().get(0).getId();

    mockMvc.perform(get("/api/transactions/" + id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id));
  }

  @Test
  void getTransactionById_WithInvalidId_ShouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/api/transactions/99999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createTransaction_WithValidData_ShouldReturnCreated() throws Exception {
    Transaction newTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"), TransactionType.EXPENSE, Category.FOOD,AccountType.CREDIT_CARD, LocalDate.EPOCH);

    mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newTransaction)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.description").value("Test Transaction"))
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  void createTransaction_WithInvalidData_ShouldReturnBadRequest() throws Exception {
    Transaction invalidTransaction = new Transaction(); // Missing required fields

    mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidTransaction)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateTransaction_WithValidData_ShouldReturnOk() throws Exception {
    Long id = transactionRepository.findAll().get(0).getId();
    
    Transaction updatedTransaction = new Transaction("Updated Salary", new BigDecimal("6000.00"), TransactionType.INCOME, Category.SALARY,AccountType.BANK_TRANSFER, LocalDate.EPOCH);

    mockMvc.perform(put("/api/transactions/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTransaction)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Updated Salary"))
        .andExpect(jsonPath("$.amount").value(6000.00));
  }

  @Test
  void updateTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
    Transaction updatedTransaction = new Transaction("Updated", new BigDecimal("100.00"), TransactionType.EXPENSE, Category.FOOD, AccountType.CASH
    , LocalDate.EPOCH);

    mockMvc.perform(put("/api/transactions/99999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTransaction)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteTransaction_WithValidId_ShouldReturnNoContent() throws Exception {
    Long id = transactionRepository.findAll().get(0).getId();

    mockMvc.perform(delete("/api/transactions/" + id))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
    mockMvc.perform(delete("/api/transactions/99999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTransactionSummary_ShouldReturnSummary() throws Exception {
    mockMvc.perform(get("/api/transactions/summary"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalIncome").exists())
        .andExpect(jsonPath("$.totalExpense").exists())
        .andExpect(jsonPath("$.balance").exists())
        .andExpect(jsonPath("$.transactionCount").value(4));
  }

  @Test
  void getTransactionsByType_WithIncome_ShouldReturnIncomeTransactions() throws Exception {
    mockMvc.perform(get("/api/transactions/by-type/INCOME"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].type").value("INCOME"));
  }
}
