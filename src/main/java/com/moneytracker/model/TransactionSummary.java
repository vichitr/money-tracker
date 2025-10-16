package com.moneytracker.model;

import java.math.BigDecimal;

public class TransactionSummary {

  private BigDecimal totalIncome;
  private BigDecimal totalExpense;
  private BigDecimal balance;
  private int transactionCount;

  public TransactionSummary(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance,
      int transactionCount) {
    this.totalIncome = totalIncome;
    this.totalExpense = totalExpense;
    this.balance = balance;
    this.transactionCount = transactionCount;
  }

  // Getters
  public BigDecimal getTotalIncome() {
    return totalIncome;
  }

  public BigDecimal getTotalExpense() {
    return totalExpense;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public int getTransactionCount() {
    return transactionCount;
  }
}