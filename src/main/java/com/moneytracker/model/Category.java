package com.moneytracker.model;

public enum Category {
    // Income Categories
    SALARY("Salary"),
    FREELANCE("Freelance"),
    RENTAL_INCOME("Rental Income"),
    TAX_REFUND("Tax Refund"),
    INVESTMENT("Investment"),
    BUSINESS("Business"),
    OTHER_INCOME("Other Income"),
    
    // Expense Categories
    FOOD("Food & Dining"),
    TRANSPORTATION("Transportation"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    BILLS("Bills & Utilities"),
    EDUCATION("Education"),
    GROCERY("Grocery"),
    GIFT("Gifs"),
    HOUSEHOLD("Household"),
    INSURANCE("Insurance"),
    ELECTRONICS("Electronics"),
    HEALTHCARE("Healthcare"),

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
