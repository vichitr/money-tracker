package com.moneytracker.model;

import java.util.List;

public class ListTransactionResponse {

  private List<Transaction> transactionList;

 public ListTransactionResponse() {
  }

 public ListTransactionResponse(List<Transaction> transactionList) {
    this.transactionList = transactionList;
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  public void  setTransactionList(List<Transaction> transactionList){
    this.transactionList = transactionList;
  }

}