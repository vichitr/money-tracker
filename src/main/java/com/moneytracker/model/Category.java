package com.moneytracker.model;

public enum Category {
    // Income Categories
    SALARY("Salary"),
    FREELANCE("Freelance"),
    INVESTMENT("Investment"),
    BUSINESS("Business"),
    OTHER_INCOME("Other Income"),
    
    // Expense Categories
    FOOD("Food & Dining"),
    TRANSPORTATION("Transportation"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    BILLS("Bills & Utilities"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    TRAVEL("Travel"),
    RENT("Rent & Housing"),
    OTHER_EXPENSE("Other Expense");
    
    private final String displayName;
    
    Category(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
