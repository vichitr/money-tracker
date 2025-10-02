# Money Tracker Backend

A simple Spring Boot backend application for tracking personal finances.

## Features

- **Transaction Management**: Create, read, update, and delete financial transactions
- **Transaction Types**: Support for Income and Expense transactions
- **Categories**: Predefined categories for better organization
- **Summary API**: Get financial summary with total income, expenses, and balance
- **Filtering**: Filter transactions by type and category

## API Endpoints

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Summary & Filtering
- `GET /api/transactions/summary` - Get financial summary
- `GET /api/transactions/by-type/{type}` - Get transactions by type (INCOME/EXPENSE)
- `GET /api/transactions/by-category/{category}` - Get transactions by category

## Transaction Model

```json
{
  "id": 1,
  "description": "Salary",
  "amount": 5000.00,
  "type": "INCOME",
  "category": "SALARY",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

## Categories

### Income Categories
- SALARY
- FREELANCE
- INVESTMENT
- BUSINESS
- OTHER_INCOME

### Expense Categories
- FOOD
- TRANSPORTATION
- ENTERTAINMENT
- SHOPPING
- BILLS
- HEALTHCARE
- EDUCATION
- TRAVEL
- RENT
- OTHER_EXPENSE

## Running the Application

1. Make sure you have Java 17+ installed
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. The application will start on `http://localhost:8080`
4. Access H2 database console at `http://localhost:8080/h2-console`

## Sample API Calls

### Create a new transaction
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Coffee",
    "amount": 4.50,
    "type": "EXPENSE",
    "category": "FOOD"
  }'
```

### Get all transactions
```bash
curl http://localhost:8080/api/transactions
```

### Get financial summary
```bash
curl http://localhost:8080/api/transactions/summary
```

## Technologies Used

- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- Java 17
