# E-Commerce Shipping Charge Estimator

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)

A **B2B e-commerce marketplace application** that calculates shipping charges for delivering products from sellers to Kirana store customers. This application provides REST APIs to determine the nearest warehouse for sellers and calculate shipping costs based on distance, transport mode, and delivery speed.

---

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Technologies Used](#-technologies-used)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Postman Testing Guide](#-postman-testing-guide)
- [Database Schema](#-database-schema)
- [Design Patterns](#-design-patterns)
- [Testing](#-testing)
- [Caching](#-caching)
- [Error Handling](#-error-handling)
- [Future Enhancements](#-future-enhancements)

---

## ✨ Features

### Core Functionality

1. **Nearest Warehouse Discovery** - Find the closest warehouse for sellers to drop off products
2. **Shipping Charge Calculation** - Calculate shipping costs based on:
   - Distance (Haversine formula)
   - Transport mode (Aeroplane, Truck, Mini Van)
   - Delivery speed (Standard, Express)
   - Product weight

3. **Combined Calculation** - End-to-end calculation from seller to customer via nearest warehouse

### Technical Features

- ✅ **RESTful APIs** with proper HTTP status codes
- ✅ **Global Exception Handling** with standardized error responses
- ✅ **Input Validation** using Jakarta Bean Validation
- ✅ **Caching** using Caffeine for improved performance
- ✅ **Design Patterns** (Strategy, Singleton, Repository)
- ✅ **Unit Tests** with JUnit 5 and Mockito
- ✅ **Clean Code** with proper documentation
- ✅ **Sample Data** auto-loaded on startup

---

## 🏗 Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│      Controllers (REST APIs)        │
├─────────────────────────────────────┤
│      Services (Business Logic)      │
├─────────────────────────────────────┤
│   Repositories (Data Access Layer)  │
├─────────────────────────────────────┤
│      Entities (Domain Models)       │
└─────────────────────────────────────┘
```

### Key Components

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic (distance calculation, warehouse finding, charge calculation)
- **Repositories**: JPA repositories for database operations
- **Entities**: JPA entities representing domain models
- **DTOs**: Data Transfer Objects for API contracts
- **Exception Handlers**: Global exception handling with `@RestControllerAdvice`

---

## 🛠 Technologies Used

| Technology          | Version | Purpose                          |
|---------------------|---------|----------------------------------|
| Java                | 17      | Programming language             |
| Spring Boot         | 3.2.1   | Application framework            |
| Spring Data JPA     | 3.2.1   | Data persistence                 |
| H2 Database         | Runtime | In-memory database               |
| Caffeine            | Latest  | Caching library                  |
| Lombok              | Latest  | Reduce boilerplate code          |
| JUnit 5             | Latest  | Unit testing                     |
| Mockito             | Latest  | Mocking framework                |
| Maven               | 3.6+    | Build tool                       |

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/jumbotail/shipping/
│   │   ├── ShippingChargeEstimatorApplication.java
│   │   ├── config/
│   │   │   ├── CacheConfig.java
│   │   │   └── DataLoader.java
│   │   ├── controller/
│   │   │   ├── WarehouseController.java
│   │   │   └── ShippingController.java
│   │   ├── dto/
│   │   │   ├── LocationDto.java
│   │   │   ├── NearestWarehouseResponse.java
│   │   │   ├── ShippingChargeResponse.java
│   │   │   ├── ShippingCalculateRequest.java
│   │   │   ├── ShippingCalculateResponse.java
│   │   │   └── ErrorResponse.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   ├── NoWarehousesAvailableException.java
│   │   │   └── InvalidDeliverySpeedException.java
│   │   ├── model/
│   │   │   ├── Customer.java
│   │   │   ├── Seller.java
│   │   │   ├── Product.java
│   │   │   ├── Warehouse.java
│   │   │   └── enums/
│   │   │       ├── DeliverySpeed.java
│   │   │       └── TransportMode.java
│   │   ├── repository/
│   │   │   ├── CustomerRepository.java
│   │   │   ├── SellerRepository.java
│   │   │   ├── ProductRepository.java
│   │   │   └── WarehouseRepository.java
│   │   └── service/
│   │       ├── DistanceService.java
│   │       ├── WarehouseService.java
│   │       └── ShippingChargeService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/jumbotail/shipping/
        ├── controller/
        │   ├── WarehouseControllerTest.java
        │   └── ShippingControllerTest.java
        └── service/
            ├── DistanceServiceTest.java
            ├── WarehouseServiceTest.java
            └── ShippingChargeServiceTest.java
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd "E-Commerce Shipping Charge Estimator"
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Application URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:shippingdb`
     - Username: `sa`
     - Password: *(leave blank)*

---

## 📡 API Documentation

> 💡 **For Postman Testing**: See the detailed [POSTMAN_GUIDE.md](POSTMAN_GUIDE.md) for step-by-step instructions on testing all endpoints with Postman, including request examples, error scenarios, and automated test scripts.

### 0. Home / Welcome Endpoint

**Endpoint:** `GET /`

**Description:** Welcome page with application information and available endpoints.

**Success Response (200 OK):**
```json
{
  "application": "E-Commerce Shipping Charge Estimator",
  "version": "1.0.0",
  "description": "A B2B e-commerce marketplace application for calculating shipping charges",
  "status": "running",
  "endpoints": {
    "GET /api/v1/warehouse/nearest": "Find nearest warehouse for a seller and product",
    "GET /api/v1/shipping-charge": "Calculate shipping charge from warehouse to customer",
    "POST /api/v1/shipping-charge/calculate": "Calculate combined shipping charge for seller and customer",
    "GET /h2-console": "Access H2 database console"
  },
  "documentation": {
    "README": "See README.md for detailed documentation",
    "QUICKSTART": "See QUICKSTART.md for quick start guide"
  }
}
```

**Example Request:**
```bash
curl http://localhost:8080/
```

---

### 1. Get Nearest Warehouse

**Endpoint:** `GET /api/v1/warehouse/nearest`

**Description:** Find the nearest warehouse for a seller to drop off a product.

**Query Parameters:**
- `sellerId` (Long, required) - ID of the seller
- `productId` (Long, required) - ID of the product

**Success Response (200 OK):**
```json
{
  "warehouseId": 1,
  "warehouseName": "BLR_Warehouse",
  "warehouseLocation": {
    "lat": 12.99999,
    "lng": 77.923273
  },
  "distanceKm": 15.42
}
```

**Example Request:**
```bash
curl "http://localhost:8080/api/v1/warehouse/nearest?sellerId=1&productId=1"
```

---

### 2. Get Shipping Charge

**Endpoint:** `GET /api/v1/shipping-charge`

**Description:** Calculate shipping charge from a warehouse to a customer.

**Query Parameters:**
- `warehouseId` (Long, required) - ID of the warehouse
- `customerId` (Long, required) - ID of the customer
- `deliverySpeed` (String, required) - `STANDARD` or `EXPRESS`

**Success Response (200 OK):**
```json
{
  "shippingCharge": 150.0,
  "distanceKm": 45.67,
  "transportMode": "MINI_VAN",
  "deliverySpeed": "STANDARD"
}
```

**Example Request:**
```bash
curl "http://localhost:8080/api/v1/shipping-charge?warehouseId=1&customerId=1&deliverySpeed=STANDARD"
```

---

### 3. Calculate Shipping for Seller and Customer

**Endpoint:** `POST /api/v1/shipping-charge/calculate`

**Description:** Calculate shipping charge for a seller and customer (combines warehouse finding and charge calculation).

**Request Body:**
```json
{
  "sellerId": 1,
  "customerId": 2,
  "deliverySpeed": "EXPRESS"
}
```

**Success Response (200 OK):**
```json
{
  "shippingCharge": 180.0,
  "nearestWarehouse": {
    "warehouseId": 2,
    "warehouseName": "MUMB_Warehouse",
    "warehouseLocation": {
      "lat": 19.07599,
      "lng": 72.877426
    },
    "distanceKm": 5.0
  },
  "distanceKm": 100.0,
  "transportMode": "TRUCK",
  "deliverySpeed": "EXPRESS"
}
```

**Example Request:**
```bash
curl -X POST http://localhost:8080/api/v1/shipping-charge/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "sellerId": 1,
    "customerId": 2,
    "deliverySpeed": "EXPRESS"
  }'
```

---

### Error Response Format

All errors return a standardized error response:

```json
{
  "status": 404,
  "error": "Resource Not Found",
  "message": "Seller not found with ID: 999",
  "path": "/api/v1/warehouse/nearest",
  "timestamp": "2026-02-21T10:30:00"
}
```

**Common Status Codes:**
- `200 OK` - Successful request
- `400 Bad Request` - Invalid parameters or validation errors
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Unexpected server error
- `503 Service Unavailable` - No warehouses available

---

## 🗄 Database Schema

### Entities

#### Customer
| Column       | Type          | Description                  |
|--------------|---------------|------------------------------|
| id           | BIGINT (PK)   | Primary key                  |
| name         | VARCHAR       | Kirana store name            |
| phone_number | VARCHAR(15)   | Contact number               |
| address      | VARCHAR       | Full address                 |
| city         | VARCHAR       | City name                    |
| pincode      | VARCHAR       | PIN code                     |
| latitude     | DOUBLE        | Latitude coordinate          |
| longitude    | DOUBLE        | Longitude coordinate         |

#### Seller
| Column        | Type          | Description                  |
|---------------|---------------|------------------------------|
| id            | BIGINT (PK)   | Primary key                  |
| name          | VARCHAR       | Seller name                  |
| phone_number  | VARCHAR(15)   | Contact number               |
| business_name | VARCHAR       | Business/company name        |
| city          | VARCHAR       | City name                    |
| latitude      | DOUBLE        | Latitude coordinate          |
| longitude     | DOUBLE        | Longitude coordinate         |

#### Product
| Column        | Type          | Description                  |
|---------------|---------------|------------------------------|
| id            | BIGINT (PK)   | Primary key                  |
| name          | VARCHAR       | Product name                 |
| category      | VARCHAR       | Product category             |
| selling_price | DOUBLE        | Price in Rupees              |
| weight_kg     | DOUBLE        | Weight in kilograms          |
| length_cm     | DOUBLE        | Length in cm                 |
| width_cm      | DOUBLE        | Width in cm                  |
| height_cm     | DOUBLE        | Height in cm                 |
| sku           | VARCHAR       | Stock Keeping Unit           |
| seller_id     | BIGINT (FK)   | Reference to seller          |

#### Warehouse
| Column      | Type          | Description                  |
|-------------|---------------|------------------------------|
| id          | BIGINT (PK)   | Primary key                  |
| name        | VARCHAR       | Warehouse name (unique)      |
| city        | VARCHAR       | City name                    |
| capacity_kg | DOUBLE        | Storage capacity             |
| latitude    | DOUBLE        | Latitude coordinate          |
| longitude   | DOUBLE        | Longitude coordinate         |

---

## 🎨 Design Patterns

### 1. Strategy Pattern
**Used in:** `ShippingChargeService`

The shipping charge calculation logic uses the Strategy pattern to determine transport mode based on distance:
- **Aeroplane**: Distance ≥ 500 km (Rs 1/km/kg)
- **Truck**: 100 km ≤ Distance < 500 km (Rs 2/km/kg)
- **Mini Van**: Distance < 100 km (Rs 3/km/kg)

### 2. Repository Pattern
**Used throughout:** All `*Repository` interfaces

Provides abstraction over data access using Spring Data JPA repositories.

### 3. Singleton Pattern
**Used in:** Configuration classes and cache manager

Ensures a single instance of configuration and cache manager throughout the application.

### 4. Dependency Injection
**Used throughout:** Constructor-based DI with Lombok's `@RequiredArgsConstructor`

---

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Test Coverage

- **Service Layer Tests**: `DistanceServiceTest`, `WarehouseServiceTest`, `ShippingChargeServiceTest`
- **Controller Layer Tests**: `WarehouseControllerTest`, `ShippingControllerTest`

### Test Scenarios Covered

✅ Successful API calls  
✅ Missing/invalid parameters  
✅ Resource not found scenarios  
✅ No warehouses available  
✅ Invalid delivery speed  
✅ Distance calculation accuracy  
✅ Transport mode selection  

---

## 💾 Caching

**Cache Provider:** Caffeine (in-memory cache)

**Cached Operations:**
1. **Nearest Warehouse Lookup** - Cache key: `sellerId-productId`
2. **Shipping Charge Calculation** - Cache key: `warehouseId-customerId-deliverySpeed`

**Cache Configuration:**
- Maximum size: 500 entries
- TTL (Time-to-Live): 5 minutes
- Statistics recording: Enabled

**Benefits:**
- Reduced database queries
- Faster response times for repeated requests
- Improved application scalability

---

## ⚠️ Error Handling

The application uses **Global Exception Handling** with `@RestControllerAdvice`:

| Exception                          | HTTP Status | Description                          |
|------------------------------------|-------------|--------------------------------------|
| `ResourceNotFoundException`        | 404         | Entity not found in database         |
| `NoWarehousesAvailableException`   | 503         | No warehouses in the system          |
| `InvalidDeliverySpeedException`    | 400         | Invalid delivery speed provided      |
| `MethodArgumentNotValidException`  | 400         | Bean validation failure              |
| `MissingServletRequestParameter`   | 400         | Required parameter missing           |
| `MethodArgumentTypeMismatch`       | 400         | Type mismatch in parameters          |
| `Exception`                        | 500         | Unexpected errors                    |

---

## 🔮 Future Enhancements

1. **Multi-product Orders**: Support calculating shipping for multiple products in a single order
2. **Real-time Rate Integration**: Integrate with actual logistics providers for dynamic pricing
3. **Route Optimization**: Implement advanced routing algorithms
4. **Warehouse Capacity Management**: Track and enforce warehouse capacity limits
5. **Order Tracking**: Add order placement and tracking functionality
6. **Authentication & Authorization**: Implement JWT-based security
7. **API Documentation**: Add Swagger/OpenAPI documentation
8. **Metrics & Monitoring**: Integrate with Prometheus and Grafana
9. **Database Migration**: Support PostgreSQL/MySQL for production use
10. **Docker Support**: Containerize the application

---

## 👨‍💻 Developer Information

**Assignment For:** Jumbotail  
**Contact:** shreya.palit@jumbotail.com

---

## 📄 License

This project is developed as part of a technical assessment for Jumbotail.

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Jumbotail for the opportunity to work on this assignment

---

**Happy Coding! 🚀**
