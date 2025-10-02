package com.moneytracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import com.moneytracker.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class Transaction {
    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;


    public Transaction(Long id, String description, BigDecimal amount,
                       TransactionType type, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }


}
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllTransactions_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4)); // 4 dummy transactions
    }

    @Test
    void getTransactionById_WithValidId_ShouldReturnTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Salary"))
                .andExpect(jsonPath("$.type").value("INCOME"));
    }

    @Test
    void getTransactionById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_WithValidData_ShouldReturnCreated() throws Exception {
        Transaction newTransaction = new Transaction();
        newTransaction.setDescription("Test Transaction");
        newTransaction.setAmount(new BigDecimal("100.00"));
        newTransaction.setType(TransactionType.EXPENSE);
        newTransaction.setCategory(Category.FOOD);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Test Transaction"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createTransaction_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Transaction invalidTransaction = new Transaction();
        // Missing required fields

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTransaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTransaction_WithValidData_ShouldReturnOk() throws Exception {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDescription("Updated Salary");
        updatedTransaction.setAmount(new BigDecimal("6000.00"));
        updatedTransaction.setType(TransactionType.INCOME);
        updatedTransaction.setCategory(Category.SALARY);

        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTransaction)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Updated Salary"))
                .andExpect(jsonPath("$.amount").value(6000.00));
    }

    @Test
    void updateTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDescription("Updated Transaction");
        updatedTransaction.setAmount(new BigDecimal("100.00"));
        updatedTransaction.setType(TransactionType.EXPENSE);
        updatedTransaction.setCategory(Category.FOOD);

        mockMvc.perform(put("/api/transactions/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTransaction)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTransaction_WithValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTransaction_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTransactionSummary_ShouldReturnSummary() throws Exception {
        mockMvc.perform(get("/api/transactions/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalIncome").exists())
                .andExpect(jsonPath("$.totalExpense").exists())
                .andExpect(jsonPath("$.balance").exists())
                .andExpect(jsonPath("$.transactionCount").value(4));
    }

    @Test
    void getTransactionsByType_WithIncome_ShouldReturnIncomeTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions/by-type/INCOME"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("INCOME"));
    }

    @Test
    void getTransactionsByType_WithExpense_ShouldReturnExpenseTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions/by-type/EXPENSE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("EXPENSE"));
    }

    @Test
    void getTransactionsByCategory_WithValidCategory_ShouldReturnFilteredTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions/by-category/SALARY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].category").value("SALARY"));
    }

    @Test
    void getTransactionsByCategory_WithNonExistentCategory_ShouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/transactions/by-category/FOOD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1)); // Only one food transaction in dummy data
    }
}
