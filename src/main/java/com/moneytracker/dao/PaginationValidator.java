package com.moneytracker.dao;

public final class PaginationValidator {

  private static final int MAX_LIMIT = 100;

  private PaginationValidator() {}

  public static void validate(int limit, int offset) {
    if (limit <= 0 || limit > MAX_LIMIT) {
      throw new IllegalArgumentException("Limit must be between 1 and 100");
    }
    if (offset < 0) {
      throw new IllegalArgumentException("Offset must be >= 0");
    }
  }

  public static int offsetFromPage(int pageNumber, int pageSize) {
    if (pageNumber < 0) {
      throw new IllegalArgumentException("Page number must be >= 0");
    }
    validate(pageSize, 0);
    return pageNumber * pageSize;
  }
}



