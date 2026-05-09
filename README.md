# Stock Management System

A full-stack stock management application with a Spring Boot REST API backend and a React frontend, backed by MySQL.

---

## Tech Stack

| Layer     | Technology              |
|-----------|-------------------------|
| Backend   | Java 22, Spring Boot 3.2.0 |
| Frontend  | React 18, Vite          |
| Database  | MySQL 8.x               |
| Excel     | Apache POI 5.4.0        |
| Build     | Maven                   |

---

## Features

- CRUD operations via REST API
- Search stocks by product name
- Excel import/export
- Auto-import `stocks.xlsx` on startup
- React frontend served from Spring Boot (single port)

---

## Project Structure

```
stock mgmt/
├── src/main/java/org/example/
│   ├── Main.java              # Spring Boot entry point
│   ├── Stock.java             # JPA Entity
│   ├── StockRepository.java   # Spring Data JPA repository
│   ├── StockController.java   # REST endpoints for stocks
│   ├── ExcelController.java   # Excel import/export endpoints
│   ├── ExcelUtil.java         # Excel read/write utility
│   ├── DataLoader.java        # Auto-imports stocks.xlsx on startup
│   ├── StockDAO.java          # Raw JDBC operations
│   ├── WebConfig.java         # CORS configuration
│   └── SpaController.java     # Serves React frontend
├── src/main/resources/
│   ├── application.properties # App configuration (env-based)
│   ├── schema.sql             # DB schema
│   └── static/                # React build output (served by Spring Boot)
├── stock-frontend/            # React + Vite source
│   └── src/
│       ├── App.jsx
│       ├── StockModal.jsx
│       ├── api.js
│       └── App.css
├── .env.example               # Environment variable template
└── stocks.xlsx                # Sample data (optional)
```

---

## Setup

### 1. Prerequisites

- Java 22+
- Maven 3.8+
- MySQL 8.x
- Node.js 18+ (only needed to rebuild frontend)

### 2. Database

```sql
CREATE DATABASE IF NOT EXISTS stock_db;
```

> The table is auto-created by Hibernate on startup.

### 3. Environment Variables

Set the following environment variables before running:

| Variable      | Description              | Default                        |
|---------------|--------------------------|--------------------------------|
| `DB_URL`      | JDBC connection URL      | `jdbc:mysql://localhost:3306/stock_db...` |
| `DB_USERNAME` | MySQL username           | `root`                         |
| `DB_PASSWORD` | MySQL password           | *(required, no default)*       |
| `SERVER_PORT` | Port to run the server   | `8080`                         |

**Windows (cmd):**
```cmd
set DB_PASSWORD=your_password
set DB_USERNAME=root
```

**Linux/macOS:**
```bash
export DB_PASSWORD=your_password
export DB_USERNAME=root
```

Or copy `.env.example` to `.env` and configure it if using a tool like dotenv.

### 4. Run Backend

```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

The React frontend is served at the same URL.

### 5. Rebuild Frontend (optional)

Only needed if you modify the React source:

```bash
cd stock-frontend
npm install
npm run build
xcopy /E /Y dist ..\src\main\resources\static
```

---

## REST API Endpoints

### Stocks

| Method | Endpoint                        | Description          |
|--------|---------------------------------|----------------------|
| GET    | `/api/stocks`                   | Get all stocks       |
| GET    | `/api/stocks/{id}`              | Get stock by ID      |
| GET    | `/api/stocks/search?name=rice`  | Search by name       |
| POST   | `/api/stocks`                   | Create stock         |
| PUT    | `/api/stocks/{id}`              | Update stock         |
| DELETE | `/api/stocks/{id}`              | Delete stock         |

### Excel

| Method | Endpoint             | Description                        |
|--------|----------------------|------------------------------------|
| GET    | `/api/excel/export`  | Download all stocks as `.xlsx`     |
| POST   | `/api/excel/import`  | Upload `.xlsx` to import stocks    |

---

## API Examples

**Create stock:**
```bash
curl -X POST http://localhost:8080/api/stocks \
  -H "Content-Type: application/json" \
  -d '{"productName":"Mouse","price":25.99,"stockLeft":100,"sell":10,"high":30.0}'
```

**Search:**
```bash
curl http://localhost:8080/api/stocks/search?name=rice
```

**Import Excel:**
```bash
curl -X POST http://localhost:8080/api/excel/import -F "file=@stocks.xlsx"
```

**Export Excel:**
```bash
curl -O http://localhost:8080/api/excel/export
```

---

## Excel File Format

Place `stocks.xlsx` in the project root for auto-import on startup.

| ID | Product Name | Price | Stock Left | Sell | High  |
|----|--------------|-------|------------|------|-------|
| 1  | Urad Dal     | 120.5 | 50         | 5    | 150.0 |
| 2  | Rice         | 45.0  | 200        | 20   | 60.0  |

- Row 1 must be headers
- Data starts from row 2
- ID and numeric columns must contain valid numbers

---

## Troubleshooting

**Database connection error**
- Verify MySQL is running
- Check `DB_USERNAME` and `DB_PASSWORD` env variables
- Ensure `stock_db` database exists

**Port already in use**
```cmd
set SERVER_PORT=8081
```

**Excel import error**
- Ensure headers are in row 1
- All numeric columns must contain valid numbers

---

## Author

Stock Management System — Spring Boot + React
