package com.moneytracker.dao;

import com.moneytracker.model.AccountType;
import com.moneytracker.model.Category;
import com.moneytracker.model.ListTransactionRequest;
import com.moneytracker.model.Transaction;
import com.moneytracker.model.TransactionType;
import java.util.HashMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DAO layer for Transaction entity with direct SQL query support.
 * This allows you to write and execute custom SQL queries directly.
 */
@Repository
public class TransactionDAO extends BaseDAO {
    
    public TransactionDAO(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate,
                         org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }
    
    /**
     * RowMapper for Transaction entity.
     */
    private static final RowMapper<Transaction> TRANSACTION_ROW_MAPPER = new RowMapper<Transaction>() {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("id"));
            transaction.setDescription(rs.getString("description"));
            transaction.setAmount(rs.getBigDecimal("amount"));
            transaction.setType(TransactionType.valueOf(rs.getString("transaction_type")));
            transaction.setCategory(Category.valueOf(rs.getString("category")));
            transaction.setAccountType(AccountType.valueOf(rs.getString("account_type")));
            transaction.setDate(rs.getObject("date", LocalDate.class));
            transaction.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            transaction.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
            return transaction;
        }
    };
    
    /**
     * Execute a custom SQL query and return Transaction list.
     *
     * Example usage:
     * String sql = "SELECT * FROM transactions WHERE amount > ? AND transaction_type = ?";
     * List<Transaction> transactions = transactionDAO.executeQuery(sql, 1000.0, "EXPENSE");
     * 
     * @param sql The SQL query string
     * @param args Query parameters
     * @return List of Transaction objects
     */
    public List<Transaction> executeQuery(String sql, Object... args) {
        return query(sql, TRANSACTION_ROW_MAPPER, args);
    }

    public List<Transaction> findAllWithPagination(int limit, int offset) {
        PaginationValidator.validate(limit, offset);

        String sql = """
        SELECT * FROM transactions
        ORDER BY date DESC
        LIMIT ? OFFSET ?
        """;

        return executeQuery(sql, limit, offset);
    }

    public List<Transaction> findByTypeWithPagination(
        TransactionType type,
        int limit,
        int offset
    ) {


        String sql = """
        SELECT * FROM transactions
        WHERE transaction_type = ?
        ORDER BY date DESC
        LIMIT ? OFFSET ?
        """;

        return executeQuery(sql, type.name(), limit, offset);
    }



    public List<Transaction> findAllByPage(int pageNumber, int pageSize) {
        int offset = PaginationValidator.offsetFromPage(pageNumber, pageSize);

        String sql = """
        SELECT * FROM transactions
        ORDER BY date DESC
        LIMIT ? OFFSET ?
        """;

        return executeQuery(sql, pageSize, offset);
    }



    /**
     * Execute a custom SQL query with named parameters and return Transaction list.
     * 
     * Example usage:
     * String sql = "SELECT * FROM transactions WHERE amount > :minAmount AND transaction_type = :type";
     * Map<String, Object> params = new HashMap<>();
     * params.put("minAmount", 1000.0);
     * params.put("type", "EXPENSE");
     * List<Transaction> transactions = transactionDAO.executeQueryWithNamedParams(sql, params);
     * 
     * @param sql The SQL query string with named parameters (:paramName)
     * @param paramMap Map of named parameters
     * @return List of Transaction objects
     */
    public List<Transaction> executeQueryWithNamedParams(String sql, Map<String, Object> paramMap) {
        return queryWithNamedParams(sql, TRANSACTION_ROW_MAPPER, paramMap);
    }
    
    /**
     * Execute a custom SQL query and return a single Transaction.
     * 
     * @param sql The SQL query string
     * @param args Query parameters
     * @return Single Transaction object or null if not found
     */
    public Transaction executeQueryForObject(String sql, Object... args) {
        return queryForObject(sql, TRANSACTION_ROW_MAPPER, args);
    }
    
    /**
     * Execute a custom SQL query with named parameters and return a single Transaction.
     * 
     * @param sql The SQL query string with named parameters
     * @param paramMap Map of named parameters
     * @return Single Transaction object or null if not found
     */
    public Transaction executeQueryForObjectWithNamedParams(String sql, Map<String, Object> paramMap) {
        return queryForObjectWithNamedParams(sql, TRANSACTION_ROW_MAPPER, paramMap);
    }
    
    /**
     * Execute a custom UPDATE, INSERT, or DELETE SQL query.
     * 
     * Example usage:
     * String sql = "UPDATE transactions SET amount = ? WHERE id = ?";
     * int rowsAffected = transactionDAO.executeUpdate(sql, 1500.0, 1L);
     * 
     * @param sql The SQL query string
     * @param args Query parameters
     * @return Number of affected rows
     */
    public int executeUpdate(String sql, Object... args) {
        return update(sql, args);
    }
    
    /**
     * Execute a custom UPDATE, INSERT, or DELETE SQL query with named parameters.
     * 
     * Example usage:
     * String sql = "UPDATE transactions SET amount = :amount WHERE id = :id";
     * Map<String, Object> params = new HashMap<>();
     * params.put("amount", 1500.0);
     * params.put("id", 1L);
     * int rowsAffected = transactionDAO.executeUpdateWithNamedParams(sql, params);
     * 
     * @param sql The SQL query string with named parameters
     * @param paramMap Map of named parameters
     * @return Number of affected rows
     */
    public int executeUpdateWithNamedParams(String sql, Map<String, Object> paramMap) {
        return updateWithNamedParams(sql, paramMap);
    }
    
    // ========== Convenience Methods for Common Queries ==========
    
    /**
     * Find transactions by type using SQL.
     */
    public List<Transaction> findByType(TransactionType type) {
        String sql = "SELECT * FROM transactions WHERE transaction_type = ? ORDER BY date DESC";
        return executeQuery(sql, type.name());
    }
    
    /**
     * Find transactions by category using SQL.
     */
    public List<Transaction> findByCategory(Category category) {
        String sql = "SELECT * FROM transactions WHERE category = ? ORDER BY date DESC";
        return executeQuery(sql, category.name());
    }
    
    /**
     * Find transactions by date range using SQL.
     */
    public List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM transactions WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        return executeQuery(sql, startDate, endDate);
    }
    
    /**
     * Find transactions by account type using SQL.
     */
    public List<Transaction> findByAccountType(AccountType accountType) {
        String sql = "SELECT * FROM transactions WHERE account_type = ? ORDER BY date DESC";
        return executeQuery(sql, accountType.name());
    }
    
    /**
     * Get total amount by transaction type using SQL.
     */
    public BigDecimal getTotalAmountByType(TransactionType type) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE transaction_type = ?";
        BigDecimal result = queryForObject(sql, BigDecimal.class, type.name());
        return result != null ? result : BigDecimal.ZERO;
    }
    
    /**
     * Get total amount by category using SQL.
     */
    public BigDecimal getTotalAmountByCategory(Category category) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE category = ?";
        BigDecimal result = queryForObject(sql, BigDecimal.class, category.name());
        return result != null ? result : BigDecimal.ZERO;
    }
    
    /**
     * Get total amount by date range using SQL.
     */
    public BigDecimal getTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE date BETWEEN ? AND ?";
        BigDecimal result = queryForObject(sql, BigDecimal.class, startDate, endDate);
        return result != null ? result : BigDecimal.ZERO;
    }
    
    /**
     * Get transaction count using SQL.
     */
    public Long getTransactionCount() {
        String sql = "SELECT COUNT(*) FROM transactions";
        Long result = queryForObject(sql, Long.class);
        return result != null ? result : 0L;
    }
    
    /**
     * Get transaction count by type using SQL.
     */
    public Long getTransactionCountByType(TransactionType type) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE transaction_type = ?";
        Long result = queryForObject(sql, Long.class, type.name());
        return result != null ? result : 0L;
    }
    
    /**
     * Delete transactions by date range using SQL.
     */
    public int deleteByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "DELETE FROM transactions WHERE date BETWEEN ? AND ?";
        return executeUpdate(sql, startDate, endDate);
    }
    
    /**
     * Delete transactions by type using SQL.
     */
    public int deleteByType(TransactionType type) {
        String sql = "DELETE FROM transactions WHERE transaction_type = ?";
        return executeUpdate(sql, type.name());
    }
    
    /**
     * Get transactions with custom WHERE clause.
     * 
     * Example: getTransactionsWithCustomWhere("amount > 1000 AND transaction_type = 'EXPENSE'")
     */
    public List<Transaction> getTransactionsWithCustomWhere(String whereClause) {
        String sql = "SELECT * FROM transactions WHERE " + whereClause + " ORDER BY date DESC";
        return executeQuery(sql);
    }
    
    /**
     * Execute any raw SQL query and return results as List of Maps.
     * This is useful for complex queries or aggregations that don't map directly to Transaction objects.
     * 
     * Example:
     * String sql = "SELECT category, SUM(amount) as total FROM transactions GROUP BY category";
     * List<Map<String, Object>> results = transactionDAO.executeRawQuery(sql);
     */
    public List<Map<String, Object>> executeRawQuery(String sql, Object... args) {
        return queryForList(sql, args);
    }
    
    /**
     * Execute raw SQL query with named parameters and return results as List of Maps.
     */
    public List<Map<String, Object>> executeRawQueryWithNamedParams(String sql, Map<String, Object> paramMap) {
        return queryForListWithNamedParams(sql, paramMap);
    }

    public List<Transaction> findWithFilters(ListTransactionRequest request) {

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM transactions WHERE 1=1"
        );

        Map<String, Object> params = new HashMap<>();

        if (request.getType() != null) {
            sql.append(" AND transaction_type = :type");
            params.put("type", request.getType().name());
        }

        if (request.getCategory() != null) {
            sql.append(" AND category = :category");
            params.put("category", request.getCategory().name());
        }

        if (request.getAccountType() != null) {
            sql.append(" AND account_type = :accountType");
            params.put("accountType", request.getAccountType().name());
        }

        if (request.getStartDate() != null) {
            sql.append(" AND date >= :startDate");
            params.put("startDate", request.getStartDate());
        }

        if (request.getEndDate() != null) {
            sql.append(" AND date <= :endDate");
            params.put("endDate", request.getEndDate());
        }

        if (request.getMinAmount() != null) {
            sql.append(" AND amount >= :minAmount");
            params.put("minAmount", request.getMinAmount());
        }

        if (request.getMaxAmount() != null) {
            sql.append(" AND amount <= :maxAmount");
            params.put("maxAmount", request.getMaxAmount());
        }

        sql.append(" ORDER BY date DESC LIMIT :limit OFFSET :offset");

        params.put("limit", request.getLimit());
        params.put("offset", request.getOffset());

        return queryWithNamedParams(sql.toString(), TRANSACTION_ROW_MAPPER, params);
    }

}

