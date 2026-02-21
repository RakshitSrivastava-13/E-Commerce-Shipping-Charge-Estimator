# Postman Testing Guide

This guide provides detailed instructions for testing all API endpoints of the **E-Commerce Shipping Charge Estimator** using Postman.

---

## 📥 Prerequisites

1. **Install Postman**: Download from [postman.com/downloads](https://www.postman.com/downloads/)
2. **Start the Application**: Ensure the Spring Boot application is running on `http://localhost:8080`
   ```bash
   mvn spring-boot:run
   ```

---

## 🚀 Quick Setup

### Create a New Collection

1. Open Postman
2. Click **"New"** → **"Collection"**
3. Name it: `E-Commerce Shipping Estimator`
4. Add a description: `API endpoints for B2B shipping charge calculation`

### Set Base URL Variable (Optional but Recommended)

1. In your collection, go to **Variables** tab
2. Add a variable:
   - **Variable**: `baseUrl`
   - **Initial Value**: `http://localhost:8080`
   - **Current Value**: `http://localhost:8080`
3. Now you can use `{{baseUrl}}` in all requests

---

## 📡 API Endpoints

### 1️⃣ Home / Welcome Endpoint

Get application information and available endpoints.

**Request Details:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/`
- **Headers**: None required

**Steps in Postman:**
1. Click **"New Request"**
2. Name: `Get Home / Welcome`
3. Method: `GET`
4. URL: `http://localhost:8080/`
5. Click **"Send"**

**Expected Response (200 OK):**
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

---

### 2️⃣ Get Nearest Warehouse

Find the nearest warehouse for a seller to drop off a product.

**Request Details:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/warehouse/nearest`
- **Query Parameters**:
  - `sellerId` (required): `1`
  - `productId` (required): `1`

**Steps in Postman:**
1. Create new request: `Get Nearest Warehouse`
2. Method: `GET`
3. URL: `http://localhost:8080/api/v1/warehouse/nearest`
4. Go to **"Params"** tab
5. Add parameters:
   | KEY       | VALUE |
   |-----------|-------|
   | sellerId  | 1     |
   | productId | 1     |
6. Click **"Send"**

**Sample URLs to Test:**
```
# Test Case 1: Nestle Seller (Mumbai) with Maggie product
http://localhost:8080/api/v1/warehouse/nearest?sellerId=1&productId=1

# Test Case 2: Rice Seller (Hyderabad) with Rice bag
http://localhost:8080/api/v1/warehouse/nearest?sellerId=2&productId=2

# Test Case 3: Sugar Seller (Pune) with Sugar bag
http://localhost:8080/api/v1/warehouse/nearest?sellerId=3&productId=3
```

**Expected Response (200 OK):**
```json
{
  "warehouseId": 2,
  "warehouseName": "MUMB_Warehouse",
  "warehouseLocation": {
    "lat": 19.07599,
    "lng": 72.877426
  },
  "distanceKm": 0.03
}
```

**Error Scenarios to Test:**

1. **Missing Parameter**
   - URL: `http://localhost:8080/api/v1/warehouse/nearest?sellerId=1`
   - Expected: `400 Bad Request`
   ```json
   {
     "status": 400,
     "error": "Missing Parameter",
     "message": "Required parameter 'productId' is missing"
   }
   ```

2. **Seller Not Found**
   - URL: `http://localhost:8080/api/v1/warehouse/nearest?sellerId=999&productId=1`
   - Expected: `404 Not Found`
   ```json
   {
     "status": 404,
     "error": "Resource Not Found",
     "message": "Seller not found with ID: 999"
   }
   ```

---

### 3️⃣ Get Shipping Charge

Calculate shipping charge from a warehouse to a customer.

**Request Details:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/shipping-charge`
- **Query Parameters**:
  - `warehouseId` (required): `1`
  - `customerId` (required): `1`
  - `deliverySpeed` (required): `STANDARD` or `EXPRESS`

**Steps in Postman:**
1. Create new request: `Get Shipping Charge`
2. Method: `GET`
3. URL: `http://localhost:8080/api/v1/shipping-charge`
4. Go to **"Params"** tab
5. Add parameters:
   | KEY           | VALUE    |
   |---------------|----------|
   | warehouseId   | 1        |
   | customerId    | 1        |
   | deliverySpeed | STANDARD |
6. Click **"Send"**

**Sample URLs to Test:**

```
# Test Case 1: Standard delivery from Bangalore warehouse to Bangalore customer
http://localhost:8080/api/v1/shipping-charge?warehouseId=1&customerId=1&deliverySpeed=STANDARD

# Test Case 2: Express delivery from Mumbai warehouse to Mumbai customer
http://localhost:8080/api/v1/shipping-charge?warehouseId=2&customerId=2&deliverySpeed=EXPRESS

# Test Case 3: Standard delivery - long distance (Bangalore to Kolkata)
http://localhost:8080/api/v1/shipping-charge?warehouseId=1&customerId=4&deliverySpeed=STANDARD

# Test Case 4: Express delivery - medium distance (Mumbai to Delhi)
http://localhost:8080/api/v1/shipping-charge?warehouseId=2&customerId=3&deliverySpeed=EXPRESS
```

**Expected Response (200 OK):**
```json
{
  "shippingCharge": 117.26,
  "distanceKm": 35.75,
  "transportMode": "MINI_VAN",
  "deliverySpeed": "STANDARD"
}
```

**Error Scenarios to Test:**

1. **Invalid Delivery Speed**
   - URL: `http://localhost:8080/api/v1/shipping-charge?warehouseId=1&customerId=1&deliverySpeed=SUPER_FAST`
   - Expected: `400 Bad Request`
   ```json
   {
     "status": 400,
     "error": "Invalid Delivery Speed",
     "message": "Invalid delivery speed: SUPER_FAST. Must be STANDARD or EXPRESS"
   }
   ```

2. **Warehouse Not Found**
   - URL: `http://localhost:8080/api/v1/shipping-charge?warehouseId=999&customerId=1&deliverySpeed=STANDARD`
   - Expected: `404 Not Found`

---

### 4️⃣ Calculate Combined Shipping Charge

Calculate shipping charge for a seller and customer (finds nearest warehouse automatically).

**Request Details:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/shipping-charge/calculate`
- **Headers**:
  - `Content-Type`: `application/json`
- **Request Body** (JSON):
  ```json
  {
    "sellerId": 1,
    "customerId": 2,
    "deliverySpeed": "EXPRESS"
  }
  ```

**Steps in Postman:**
1. Create new request: `Calculate Combined Shipping Charge`
2. Method: `POST`
3. URL: `http://localhost:8080/api/v1/shipping-charge/calculate`
4. Go to **"Headers"** tab
5. Add header:
   | KEY          | VALUE            |
   |--------------|------------------|
   | Content-Type | application/json |
6. Go to **"Body"** tab
7. Select **"raw"** and **"JSON"** format
8. Paste the JSON request body
9. Click **"Send"**

**Test Cases:**

**Test Case 1: Nestle Seller (Mumbai) to Andheri Mini Mart (Mumbai) - Express**
```json
{
  "sellerId": 1,
  "customerId": 2,
  "deliverySpeed": "EXPRESS"
}
```

**Expected Response (200 OK):**
```json
{
  "shippingCharge": 16.99,
  "nearestWarehouse": {
    "warehouseId": 2,
    "warehouseName": "MUMB_Warehouse",
    "warehouseLocation": {
      "lat": 19.07599,
      "lng": 72.877426
    },
    "distanceKm": 0.03
  },
  "distanceKm": 4.26,
  "transportMode": "MINI_VAN",
  "deliverySpeed": "EXPRESS"
}
```

**Test Case 2: Rice Seller (Hyderabad) to Bangalore Customer - Standard**
```json
{
  "sellerId": 2,
  "customerId": 1,
  "deliverySpeed": "STANDARD"
}
```

**Test Case 3: Sugar Seller (Pune) to Delhi Customer - Express**
```json
{
  "sellerId": 3,
  "customerId": 3,
  "deliverySpeed": "EXPRESS"
}
```

**Test Case 4: All customers with different sellers**
```json
{
  "sellerId": 1,
  "customerId": 4,
  "deliverySpeed": "STANDARD"
}
```

**Error Scenarios to Test:**

1. **Missing Required Field**
   ```json
   {
     "sellerId": 1,
     "deliverySpeed": "STANDARD"
   }
   ```
   - Expected: `400 Bad Request`
   ```json
   {
     "status": 400,
     "error": "Validation Error",
     "message": "customerId is required"
   }
   ```

2. **Invalid Seller ID**
   ```json
   {
     "sellerId": 999,
     "customerId": 1,
     "deliverySpeed": "EXPRESS"
   }
   ```
   - Expected: `404 Not Found`

---

## 🧪 Testing Transport Modes

The application automatically selects transport mode based on distance:

| Distance       | Transport Mode | Rate (Rs/km/kg) |
|----------------|----------------|-----------------|
| 0-100 km       | MINI_VAN       | 3               |
| 100-500 km     | TRUCK          | 2               |
| 500+ km        | AEROPLANE      | 1               |

**To Test Different Transport Modes:**

1. **MINI_VAN (Short Distance)**
   - Warehouse: BLR_Warehouse (ID: 1)
   - Customer: Bangalore (ID: 1)
   - Expected distance: ~35 km

2. **TRUCK (Medium Distance)**
   - Warehouse: BLR_Warehouse (ID: 1)
   - Customer: Mumbai (ID: 2)
   - Expected distance: ~800+ km → Will use AEROPLANE (adjust test)

3. **AEROPLANE (Long Distance)**
   - Warehouse: BLR_Warehouse (ID: 1)
   - Customer: Kolkata (ID: 4)
   - Expected distance: ~1500+ km

---

## 📊 Sample Data Reference

Use these IDs for testing:

### Customers
| ID | Name                        | City      | Location                    |
|----|-----------------------------|-----------|-----------------------------|
| 1  | Shree Kirana Store          | Bangalore | 12.9716, 77.5946           |
| 2  | Andheri Mini Mart           | Mumbai    | 19.1136, 72.8697           |
| 3  | Delhi Central Store         | Delhi     | 28.6315, 77.2167           |
| 4  | Kolkata Provision Store     | Kolkata   | 22.5544, 88.3515           |

### Sellers
| ID | Name         | City       | Location              |
|----|--------------|------------|-----------------------|
| 1  | Nestle Seller| Mumbai     | 19.0760, 72.8777     |
| 2  | Rice Seller  | Hyderabad  | 17.3850, 78.4867     |
| 3  | Sugar Seller | Pune       | 18.5204, 73.8567     |

### Products
| ID | Name              | Weight  | Seller ID |
|----|-------------------|---------|-----------|
| 1  | Maggie 500g       | 0.5 kg  | 1         |
| 2  | Rice Bag 10Kg     | 10 kg   | 2         |
| 3  | Sugar Bag 25kg    | 25 kg   | 3         |
| 4  | Cooking Oil 5L    | 5 kg    | 1         |

### Warehouses
| ID | Name            | City      | Location              |
|----|-----------------|-----------|-----------------------|
| 1  | BLR_Warehouse   | Bangalore | 12.99999, 77.923273  |
| 2  | MUMB_Warehouse  | Mumbai    | 19.07599, 72.877426  |
| 3  | DELHI_Warehouse | Delhi     | 28.7041, 77.1025     |

---

## 🎯 Postman Collection Export

You can create a Postman collection with all these endpoints. Here's the structure:

```
E-Commerce Shipping Estimator
├── 1. Get Home / Welcome
├── 2. Get Nearest Warehouse
│   ├── Success - Seller 1
│   ├── Success - Seller 2
│   ├── Error - Missing Parameter
│   └── Error - Seller Not Found
├── 3. Get Shipping Charge
│   ├── Success - Standard Delivery
│   ├── Success - Express Delivery
│   ├── Error - Invalid Delivery Speed
│   └── Error - Warehouse Not Found
└── 4. Calculate Combined Shipping
    ├── Success - Mumbai to Mumbai
    ├── Success - Hyderabad to Bangalore
    ├── Success - Pune to Delhi
    ├── Error - Missing Field
    └── Error - Invalid Seller
```

---

## 🔍 Advanced Testing

### Environment Variables

Create environments for different scenarios:

**Local Environment:**
- `baseUrl`: `http://localhost:8080`

**Production Environment:**
- `baseUrl`: `https://your-production-url.com`

### Test Scripts

Add test scripts in Postman to automate validation:

**Example Test Script for "Get Nearest Warehouse":**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has warehouseId", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('warehouseId');
});

pm.test("Distance is a number", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.distanceKm).to.be.a('number');
});
```

---

## 📝 Tips for Effective Testing

1. **Save Requests**: Save all requests in your collection for reusability
2. **Use Variables**: Use collection/environment variables for `baseUrl`
3. **Organize**: Group related endpoints in folders
4. **Document**: Add descriptions to each request
5. **Test Scripts**: Add automated assertions to validate responses
6. **Examples**: Save response examples for each request
7. **Share**: Export and share the collection with your team

---

## 🐛 Common Issues

### Issue 1: Connection Refused
**Error**: `Could not get any response`  
**Solution**: Ensure the Spring Boot application is running on port 8080

```bash
mvn spring-boot:run
```

### Issue 2: 404 Not Found
**Error**: `{"status":404,"error":"Not Found"}`  
**Solution**: Double-check the URL and ensure it matches the API endpoint exactly

### Issue 3: 400 Bad Request
**Error**: `{"status":400,"error":"Bad Request"}`  
**Solution**: Verify all required parameters are included and have valid values

### Issue 4: Content-Type Error
**Error**: `415 Unsupported Media Type`  
**Solution**: For POST requests, ensure `Content-Type: application/json` header is set

---

## 📚 Additional Resources

- **H2 Database Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:shippingdb`
  - Username: `sa`
  - Password: *(leave blank)*

- **Application Logs**: Check console output for detailed error messages

---

## ✅ Testing Checklist

- [ ] Application is running on port 8080
- [ ] Can access home endpoint (`/`)
- [ ] Can find nearest warehouse for all sellers
- [ ] Can calculate shipping charge with STANDARD delivery
- [ ] Can calculate shipping charge with EXPRESS delivery
- [ ] Can calculate combined shipping charge
- [ ] Error handling works for missing parameters
- [ ] Error handling works for invalid IDs
- [ ] Error handling works for invalid delivery speed
- [ ] All transport modes (MINI_VAN, TRUCK, AEROPLANE) are tested
- [ ] Response times are acceptable (< 200ms for cached requests)

---

**Happy Testing! 🚀**

For any issues or questions, refer to the [README.md](README.md) or [QUICKSTART.md](QUICKSTART.md) documentation.
