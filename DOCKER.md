# Docker Setup Guide for Money Tracker

This guide explains how to run the Money Tracker application using Docker and Docker Compose with MySQL database.

## Prerequisites

- Docker (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)

## Quick Start

### 1. Start the Application

Run the following command in the project root directory:

```bash
docker-compose up -d
```

This will:
- Pull the MySQL 8.0 image
- Build the Spring Boot application image
- Create a Docker network for inter-service communication
- Start MySQL database container
- Start the application container
- Create a persistent volume for MySQL data

### 2. Check Container Status

```bash
docker-compose ps
```

You should see both `money-tracker-mysql` and `money-tracker-app` containers running.

### 3. View Logs

To view application logs:
```bash
docker-compose logs -f app
```

To view MySQL logs:
```bash
docker-compose logs -f mysql
```

### 4. Access the Application

The application will be available at:
- **API Base URL**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

## Available Commands

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose stop
```

### Stop and Remove Containers
```bash
docker-compose down
```

### Stop and Remove Containers + Volumes (CAUTION: This deletes all data!)
```bash
docker-compose down -v
```

### Rebuild Application Image
```bash
docker-compose build app
```

### Rebuild and Restart
```bash
docker-compose up -d --build
```

### View Real-time Logs
```bash
docker-compose logs -f
```

### Execute Commands in Running Container
```bash
# Access application container shell
docker exec -it money-tracker-app sh

# Access MySQL container shell
docker exec -it money-tracker-mysql bash

# Connect to MySQL database
docker exec -it money-tracker-mysql mysql -u money_tracker_user -pmoney_tracker_pass money_tracker
```

## Environment Variables

The application uses the following environment variables (configurable in `docker-compose.yml`):

| Variable | Default Value | Description |
|----------|--------------|-------------|
| DB_HOST | mysql | MySQL host name |
| DB_PORT | 3306 | MySQL port |
| DB_NAME | money_tracker | Database name |
| DB_USERNAME | money_tracker_user | Database username |
| DB_PASSWORD | money_tracker_pass | Database password |

## Database Configuration

### MySQL Credentials

**Root User:**
- Username: `root`
- Password: `root`

**Application User:**
- Username: `money_tracker_user`
- Password: `money_tracker_pass`
- Database: `money_tracker`

### Data Persistence

MySQL data is persisted in a Docker volume named `money_tracker_mysql_data`. This ensures your data survives container restarts.

## API Endpoints

Once the application is running, you can test the following endpoints:

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Create Transaction
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Salary",
    "amount": 5000.00,
    "type": "INCOME",
    "category": "SALARY"
  }'
```

### Get All Transactions
```bash
curl http://localhost:8080/api/transactions
```

### Get Transaction Summary
```bash
curl http://localhost:8080/api/transactions/summary
```

## Troubleshooting

### Application Won't Start

1. Check if MySQL is healthy:
   ```bash
   docker-compose logs mysql
   ```

2. Ensure MySQL is ready before the app starts (configured via health checks)

3. Check application logs:
   ```bash
   docker-compose logs app
   ```

### Database Connection Issues

1. Verify MySQL container is running:
   ```bash
   docker ps | grep mysql
   ```

2. Check if database exists:
   ```bash
   docker exec -it money-tracker-mysql mysql -u root -proot -e "SHOW DATABASES;"
   ```

3. Test database connectivity:
   ```bash
   docker exec -it money-tracker-mysql mysql -u money_tracker_user -pmoney_tracker_pass -e "USE money_tracker; SHOW TABLES;"
   ```

### Port Already in Use

If port 8080 or 3306 is already in use, modify the port mappings in `docker-compose.yml`:

```yaml
services:
  mysql:
    ports:
      - "3307:3306"  # Change host port to 3307
  
  app:
    ports:
      - "8081:8080"  # Change host port to 8081
```

### Reset Everything

To completely reset the application and database:

```bash
# Stop and remove containers, networks, and volumes
docker-compose down -v

# Remove images (optional)
docker rmi money-tracker-app mysql:8.0

# Start fresh
docker-compose up -d --build
```

## Development Workflow

### Making Code Changes

1. Modify your code
2. Rebuild the application image:
   ```bash
   docker-compose build app
   ```
3. Restart the application container:
   ```bash
   docker-compose up -d app
   ```

### Running Tests

To run tests before building:
```bash
mvn clean test
```

## Production Considerations

For production deployment, consider:

1. **Use secrets management** instead of plain text passwords
2. **Enable SSL/TLS** for MySQL connections
3. **Configure proper health checks**
4. **Set up monitoring and logging**
5. **Use environment-specific configuration files**
6. **Implement database backups**
7. **Change default passwords**

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [MySQL Docker Hub](https://hub.docker.com/_/mysql)

