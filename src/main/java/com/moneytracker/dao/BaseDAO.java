package com.moneytracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Base DAO class providing common SQL query execution methods.
 * This class provides direct SQL query capabilities using JdbcTemplate.
 */
@Component
public class BaseDAO {
    
    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public BaseDAO(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    
    /**
     * Execute a SELECT query and return a list of results.
     * 
     * @param sql The SQL query string
     * @param rowMapper The RowMapper to map results to objects
     * @param args Query parameters
     * @return List of mapped objects
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }
    
    /**
     * Execute a SELECT query with named parameters and return a list of results.
     * 
     * @param sql The SQL query string with named parameters (:paramName)
     * @param rowMapper The RowMapper to map results to objects
     * @param paramMap Map of named parameters
     * @return List of mapped objects
     */
    public <T> List<T> queryWithNamedParams(String sql, RowMapper<T> rowMapper, Map<String, Object> paramMap) {
        return namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
    }
    
    /**
     * Execute a SELECT query and return a single result.
     * 
     * @param sql The SQL query string
     * @param rowMapper The RowMapper to map result to object
     * @param args Query parameters
     * @return Single mapped object or null if not found
     */
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, args);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    /**
     * Execute a SELECT query with named parameters and return a single result.
     * 
     * @param sql The SQL query string with named parameters
     * @param rowMapper The RowMapper to map result to object
     * @param paramMap Map of named parameters
     * @return Single mapped object or null if not found
     */
    public <T> T queryForObjectWithNamedParams(String sql, RowMapper<T> rowMapper, Map<String, Object> paramMap) {
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, paramMap, rowMapper);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    /**
     * Execute an UPDATE, INSERT, or DELETE query.
     * 
     * @param sql The SQL query string
     * @param args Query parameters
     * @return Number of affected rows
     */
    public int update(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }
    
    /**
     * Execute an UPDATE, INSERT, or DELETE query with named parameters.
     * 
     * @param sql The SQL query string with named parameters
     * @param paramMap Map of named parameters
     * @return Number of affected rows
     */
    public int updateWithNamedParams(String sql, Map<String, Object> paramMap) {
        return namedParameterJdbcTemplate.update(sql, paramMap);
    }
    
    /**
     * Execute a query and return a single value (e.g., COUNT, SUM, etc.).
     * 
     * @param sql The SQL query string
     * @param requiredType The expected return type
     * @param args Query parameters
     * @return Single value result
     */
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
        try {
            return jdbcTemplate.queryForObject(sql, requiredType, args);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    /**
     * Execute a query with named parameters and return a single value.
     * 
     * @param sql The SQL query string with named parameters
     * @param requiredType The expected return type
     * @param paramMap Map of named parameters
     * @return Single value result
     */
    public <T> T queryForObjectWithNamedParams(String sql, Class<T> requiredType, Map<String, Object> paramMap) {
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, paramMap, requiredType);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    /**
     * Execute a batch update operation.
     * 
     * @param sql The SQL query string
     * @param batchArgs Array of parameter arrays for batch operations
     * @return Array of affected row counts
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    
    /**
     * Execute raw SQL query and return List of Maps (column name -> value).
     * Useful for ad-hoc queries or when you don't have a specific object mapper.
     * 
     * @param sql The SQL query string
     * @param args Query parameters
     * @return List of Maps representing rows
     */
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        return jdbcTemplate.queryForList(sql, args);
    }
    
    /**
     * Execute raw SQL query with named parameters and return List of Maps.
     * 
     * @param sql The SQL query string with named parameters
     * @param paramMap Map of named parameters
     * @return List of Maps representing rows
     */
    public List<Map<String, Object>> queryForListWithNamedParams(String sql, Map<String, Object> paramMap) {
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
    }
}

