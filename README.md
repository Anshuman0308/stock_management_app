# Stock Management System

Spring Boot REST API for stock management with MySQL database and Excel integration.

## Features
- CRUD operations via REST API
- MySQL database integration with JDBC
- Excel import/export functionality
- Auto-import data on startup
- Search by ID and product name

## Technologies
- Java 22
- Spring Boot 3.2.0
- MySQL 8.x
- Apache POI 5.2.5
- Maven

## Setup

### 1. Database Configuration
Create MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS stock_db;
USE stock_db;

CREATE TABLE IF NOT EXISTS stocks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL
);
```

### 2. Update Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Excel File Setup
Place `stocks.xlsx` in project root with format:
| ID | Product Name | Price |
|----|--------------|-------|
| 1  | Urad Dal     | 120.5 |
| 2  | Rice         | 45.0  |

### 4. Run Application
```bash
mvn spring-boot:run
```
Server starts at: http://localhost:8080

## REST API Endpoints

### Stock Operations
- **GET** `/api/stocks` - Get all stocks
- **GET** `/api/stocks/{id}` - Get stock by ID
- **GET** `/api/stocks/search?name=urad` - Search by product name
- **POST** `/api/stocks` - Create new stock
- **PUT** `/api/stocks/{id}` - Update stock
- **DELETE** `/api/stocks/{id}` - Delete stock

### Excel Operations
- **GET** `/api/excel/export` - Export all stocks to Excel
- **POST** `/api/excel/import` - Import Excel file (form-data: file)

## API Examples

### Create Stock (POST)
```bash
curl -X POST http://localhost:8080/api/stocks \
-H "Content-Type: application/json" \
-d '{"productName":"Mouse","price":25.99}'
```

### Get All Stocks (GET)
```bash
curl http://localhost:8080/api/stocks
```

### Search by Name (GET)
```bash
curl http://localhost:8080/api/stocks/search?name=urad
```

### Import Excel (POST)
```bash
curl -X POST http://localhost:8080/api/excel/import \
-F "file=@stocks.xlsx"
```

### Export to Excel (GET)
```bash
curl -O http://localhost:8080/api/excel/export
```

## Project Structure
```
src/main/java/org/example/
├── Main.java              # Spring Boot application
├── Stock.java             # JPA Entity
├── StockRepository.java   # Data repository
├── StockController.java   # REST endpoints
├── ExcelController.java   # Excel import/export
├── ExcelUtil.java         # Excel utility
├── DataLoader.java        # Auto-import on startup
└── StockDAO.java          # JDBC operations

src/main/resources/
├── application.properties # Configuration
└── schema.sql            # Database schema
```

## Data Types
- **int** - Stock ID
- **String** - Product Name
- **double** - Price

## Auto-Import
Application automatically imports `stocks.xlsx` on startup if file exists in project root.

## Troubleshooting

### Database Connection Error
- Verify MySQL is running
- Check username/password in application.properties
- Ensure database `stock_db` exists

### Excel Import Error
- Ensure Excel file has headers in row 1
- Data should start from row 2
- ID column must be numeric
- Price column must be numeric

### Port Already in Use
Change port in application.properties:
```properties
server.port=8081
```

## Author
Stock Management System - Spring Boot Application
