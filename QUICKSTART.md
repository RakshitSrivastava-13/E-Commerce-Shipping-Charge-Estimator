# Quick Start Guide

## Prerequisites
- Java 17+
- Maven 3.6+

## Running the Application

### Option 1: Using Maven
```bash
mvn spring-boot:run
```

### Option 2: Using JAR
```bash
mvn clean package
java -jar target/shipping-charge-estimator-1.0.0.jar
```

The application will start on **http://localhost:8080**

## Testing the APIs

### 0. Home / Welcome Page
```bash
curl http://localhost:8080/
```

### 1. Get Nearest Warehouse
```bash
curl "http://localhost:8080/api/v1/warehouse/nearest?sellerId=1&productId=1"
```

### 2. Get Shipping Charge
```bash
curl "http://localhost:8080/api/v1/shipping-charge?warehouseId=1&customerId=1&deliverySpeed=STANDARD"
```

### 3. Calculate Combined Shipping Charge
```bash
curl -X POST http://localhost:8080/api/v1/shipping-charge/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "sellerId": 1,
    "customerId": 2,
    "deliverySpeed": "EXPRESS"
  }'
```

## Sample Data

The application auto-loads sample data on startup:

### Customers (Kirana Stores)
- **ID 1**: Shree Kirana Store (Bangalore)
- **ID 2**: Andheri Mini Mart (Mumbai)
- **ID 3**: Delhi Central Store (Delhi)
- **ID 4**: Kolkata Provision Store (Kolkata)

### Sellers
- **ID 1**: Nestle Seller (Mumbai)
- **ID 2**: Rice Seller (Hyderabad)
- **ID 3**: Sugar Seller (Pune)

### Products
- **ID 1**: Maggie 500g Packet (0.5 kg) - Seller 1
- **ID 2**: Rice Bag 10Kg - Seller 2
- **ID 3**: Sugar Bag 25kg - Seller 3
- **ID 4**: Cooking Oil 5L (5 kg) - Seller 1

### Warehouses
- **ID 1**: BLR_Warehouse (Bangalore)
- **ID 2**: MUMB_Warehouse (Mumbai)
- **ID 3**: DELHI_Warehouse (Delhi)

## H2 Database Console

Access the H2 console at: **http://localhost:8080/h2-console**

- **JDBC URL**: `jdbc:h2:mem:shippingdb`
- **Username**: `sa`
- **Password**: *(leave blank)*

## Running Tests

```bash
mvn test
```

## Building the Project

```bash
mvn clean install
```

## Shipping Charge Calculation

### Transport Modes
- **Mini Van**: 0-100 km @ Rs 3/km/kg
- **Truck**: 100-500 km @ Rs 2/km/kg
- **Aeroplane**: 500+ km @ Rs 1/km/kg

### Delivery Speeds
- **STANDARD**: Rs 10 base + calculated shipping
- **EXPRESS**: Rs 10 base + Rs 1.2/kg extra + calculated shipping

### Formula
```
Total Charge = Base Courier Charge + (Distance × Weight × Rate) + Express Extra (if applicable)
```

## Example Scenarios

### Scenario 1: Local Delivery (Mini Van)
- Distance: 50 km
- Weight: 10 kg
- Mode: MINI_VAN (Rs 3/km/kg)
- Speed: STANDARD
- **Charge**: Rs 10 + (50 × 10 × 3) = Rs 1510

### Scenario 2: Express Long Distance (Aeroplane)
- Distance: 600 km
- Weight: 5 kg
- Mode: AEROPLANE (Rs 1/km/kg)
- Speed: EXPRESS
- **Charge**: Rs 10 + (600 × 5 × 1) + (5 × 1.2) = Rs 3016

## Stopping the Application

Press `Ctrl + C` in the terminal where the application is running.
