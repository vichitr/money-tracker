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

### Option 1: Using Docker (Recommended)

1. Make sure you have Docker and Docker Compose installed
2. Run the application with MySQL:
   ```bash
   docker-compose up -d
   ```
3. The application will start on `http://localhost:8080`
4. Check health status at `http://localhost:8080/actuator/health`

For detailed Docker instructions, see [DOCKER.md](DOCKER.md)

### Option 2: Running Locally

1. Make sure you have Java 17+ and MySQL installed
2. Create a MySQL database named `money_tracker`
3. Configure database connection in `application.properties` or use environment variables:
   ```bash
   export DB_HOST=localhost
   export DB_PORT=3306
   export DB_NAME=money_tracker
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. The application will start on `http://localhost:8080`

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
- MySQL 8.0
- Spring Boot Actuator
- Docker & Docker Compose
- Maven
- Java 17

## Database

The application uses MySQL for persistent data storage. When running with Docker, MySQL is automatically configured and initialized. For local development, you'll need to set up MySQL manually.

## Restart Docker
```
docker stop $(docker ps -q)
docker rm $(docker ps -q) 
docker system prune
docker system prune --volumes
```