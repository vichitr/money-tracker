-- Initialize the money_tracker database
-- This script is automatically executed when the MySQL container starts for the first time

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS money_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE money_tracker;

-- Grant privileges to the application user
GRANT ALL PRIVILEGES ON money_tracker.* TO 'money_tracker_user'@'%';
FLUSH PRIVILEGES;

-- Insert some sample data (optional - will be inserted if tables are created)
-- Note: The actual table creation is handled by Hibernate with ddl-auto=update

-- You can add additional initialization queries here if needed

