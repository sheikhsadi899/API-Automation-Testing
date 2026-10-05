# Backend Automation QA Assessment

### Swagger Petstore API Automation

API automation project for testing **Store Inventory & Order Management** and **Pet Profile CRUD & API Key behavior**.

---

## 🛠️ Technology Stack

| Tool        | Purpose                                  |
| ----------- | ---------------------------------------- |
| Java 21     | Programming language                     |
| RestAssured | REST API automation                      |
| TestNG      | Test execution and assertions            |
| Maven       | Dependency management and test execution |
| Lombok      | Model/POJO classes                       |
| Jackson     | JSON serialization and deserialization   |
| Allure      | Test reporting                           |

---

# 📁 Project Design

The project follows a simple **Base Test + Model + Utility + Test Class** structure.

```text
src/
└── test/
    └── java/
        │
        ├── constants/
        │   ├── Endpoints.java
        │   ├── JsonPaths.java
        │   └── Params.java
        │
        ├── models/
        │   ├── ApiError.java
        │   ├── Category.java
        │   ├── Order.java
        │   ├── Pet.java
        │   └── Tag.java
        │
        ├── utils/
        │   └── RandomUtils.java
        │
        └── tests/
            ├── BaseTest.java
            ├── StoreOrderTest.java
            └── PetCrudApiKeyTest.java
```

### Design Approach

**BaseTest**

Contains common API configuration and test-data cleanup.

**Models**

Represent API request and response objects such as `Pet`, `Order`, and `ApiError`.

**Constants**

Store API endpoints, path parameters, and simple JSON paths in one place.

**Utils**

Contains reusable helper methods such as random test-data generation.

**Test Classes**

Contain the actual assessment scenarios:

* `StoreOrderTest` — Task 1
* `PetCrudApiKeyTest` — Task 2

This structure keeps the test code separated, reusable, and easier to maintain.

---

# 🧪 Test Scenarios

## Task 1 — Store Inventory & Order Management

### Endpoints

```text
GET    /store/inventory
POST   /store/order
GET    /store/order/{orderId}
DELETE /store/order/{orderId}
```

### Covered Scenarios

**Inventory**

* Verify successful response
* Verify JSON response
* Verify inventory structure
* Verify inventory values are not negative

**Order E2E Lifecycle**

```text
Get Inventory
      ↓
Create Order
      ↓
Get Created Order
      ↓
Verify Order Data
      ↓
Delete Order
```

The created order is tracked and automatically cleaned up if the test fails before deletion.

**Non-existent Order**

* Request an order that does not exist
* Verify `404`
* Validate the error response structure

**Boundary / Invalid Data**

* Send an order with an invalid quantity
* Verify how the public API handles the boundary value

---

# 🐾 Task 2 — Pet Profile CRUD & API Key Simulation

### Endpoints

```text
POST   /pet
PUT    /pet
GET    /pet/{petId}
DELETE /pet/{petId}
```

### Covered Scenarios

**Pet CRUD Lifecycle**

```text
Create Pet
    ↓
Status = available
    ↓
Update Pet
    ↓
Status = sold
    ↓
Get Pet
    ↓
Verify Status = sold
    ↓
Delete Pet
```

**API Key Simulation**

The DELETE operation is tested with:

* Valid dummy `api_key`
* No API key
* Invalid API key

The purpose is to verify how the public Swagger Petstore API handles the `api_key` header.

**Data Validation**

Pet status is validated against the allowed values:

```text
available
pending
sold
```

---

# 🧠 Why These Scenarios and Tools?

The selected scenarios cover the main business flows requested by the assessment while also checking important negative and boundary behavior.

**RestAssured** was selected because it provides a simple and readable way to create HTTP requests and validate REST API responses.

**TestNG** was selected for test execution, assertions, and lifecycle annotations such as `@BeforeMethod` and `@AfterMethod`.

**Java 21** is used as required by the assessment.

**Model classes with Jackson** are used instead of raw JSON strings. This makes request and response data easier to maintain and provides type-safe serialization and deserialization.

**Allure** is used to generate a readable test execution report.

---

# 📊 Test Coverage Justification

The test suite focuses on the most important API behaviors rather than testing every possible combination.

### Functional Coverage

* Store inventory retrieval
* Order creation
* Order retrieval
* Order deletion
* Non-existent order handling
* Invalid/boundary order data
* Pet creation
* Pet update
* Pet retrieval
* Pet deletion

### Data Validation

* Order data consistency
* Pet status validation
* Error response structure
* Response data integrity

### API Behavior

* HTTP status codes
* Content type
* API key/header behavior
* Positive and negative scenarios

### Test Data Management

Created Pets and Orders are stored in tracking lists and automatically removed after each test through `BaseTest`.

This prevents test data from unnecessarily remaining in the public Petstore environment.

---

# ⚙️ Installation

## Prerequisites

Install:

* Java 21
* Maven
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

# 📥 Clone the Repository

```bash
git clone <YOUR_PUBLIC_GITHUB_REPOSITORY_URL>
```

Navigate to the project:

```bash
cd <PROJECT_FOLDER>
```

---

# 📦 Install Dependencies

Maven automatically downloads all required dependencies from `pom.xml`.

Run:

```bash
mvn clean install
```

---

# ▶️ Run Tests

Run the complete test suite:

```bash
mvn clean test
```

To run the tests directly from IntelliJ IDEA, right-click the test class or test suite and select **Run**.

---

# 📈 Allure Report

The project uses Allure for test reporting.

After executing the tests, generate and open the report with:

```bash
allure serve target/allure-results
```

The Allure report provides:

* Passed tests
* Failed tests
* Test duration
* Test steps
* Failure details
* Execution results

---

# 📄 Maven Surefire Report

A standard HTML report can also be generated using Maven:

```bash
mvn surefire-report:report
```

The report is generated at:

```text
target/site/surefire-report.html
```

---

# 🔗 API

Swagger Petstore API:

```text
https://petstore.swagger.io/v2
```

---

# 📦 Deliverables

| Deliverable                     | Status |
| ------------------------------- | ------ |
| Public GitHub/GitLab repository | ✅      |
| Source code                     | ✅      |
| README.md                       | ✅      |
| Installation instructions       | ✅      |
| Test execution instructions     | ✅      |
| Test scenario justification     | ✅      |
| Test coverage justification     | ✅      |
| Allure report instructions      | ✅      |
| HTML report instructions        | ✅      |

---

# 👤 Author

**Sheikh Sadi**

Backend Automation QA Assessment
