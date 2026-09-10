package com.moneytracker.model;

import java.util.List;

public class PaginationResponse {
  private List<Transaction> transactionList;
  private Long totalCount;
  private Integer offset;
  private Integer limit;

  public PaginationResponse() {
  }

  public PaginationResponse(List<Transaction> transactionList, Long totalCount, Integer offset, Integer limit) {
    this.transactionList = transactionList;
    this.totalCount = totalCount;
    this.offset = offset;
    this.limit = limit;
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  public void setTransactionList(List<Transaction> transactionList) {
    this.transactionList = transactionList;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }
}
