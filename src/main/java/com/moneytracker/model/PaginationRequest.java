package com.moneytracker.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PaginationRequest {

  @NotNull(message = "Offset is required")
  @Min(value = 0, message = "Offset must be >= 0")
  private Integer offset;

  @NotNull(message = "Limit is required")
  @Min(value = 1, message = "Limit must be > 0")
  @Max(value = 100, message = "Limit must be <= 100")
  private Integer limit;

  public PaginationRequest() {
  }

  public PaginationRequest(Integer offset, Integer limit) {
    this.offset = offset;
    this.limit = limit;
  }

  public PaginationRequest(Long totalCount, Integer offset, Integer limit) {

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
