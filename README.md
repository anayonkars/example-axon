# Example Axon Account Domain

This is a **Event Sourcing** and **CQRS** (Command Query Responsibility Segregation) application built using **Axon Framework** and **Spring Boot**. It demonstrates a simple bank account domain where you can perform various operations like opening an account, crediting involves, debiting, and closing accounts.

## 🚀 Features

- **Account Management**: Open, Activate, and Close accounts.
- **Transactions**: Credit and Debit amounts.
- **Event Sourcing**: All state changes are stored as a sequence of events.
- **CQRS**: Separation of write model (Commands) and read model (Queries).
- **Projections**: `AccountView` projection tailored for querying account details.

## 🛠 Tech Stack

- **Language**: Java 8
- **Frameworks**: Spring Boot 2.7.18, Axon Framework 4.9.3
- **Database**: H2 (In-Memory)
- **Schema Migration**: Liquibase
- **Build Tool**: Gradle

## 📋 Prerequisites

- JDK 8 or higher installed.
- Gradle installed (or use the provided `gradlew` wrapper).

## 🏃‍♂️ Getting Started

### 1. Build the Application

Use the Gradle wrapper to build the project:

```bash
./gradlew clean build
```

### 2. Run the Application

Start the Spring Boot application:

```bash
./gradlew bootRun
```

The application will start on port `8080` (default).

## 🔌 API Reference

The application exposes a REST API for interacting with the account domain.

**Base URL**: `http://localhost:8080/account`

### 1. Open Account

**Endpoint**: `POST /account/open`

```json
/* Request Body */
{
  "name": "John Doe"
}
```

**Curl Command**:

```bash
curl -X POST http://localhost:8080/account/open \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe"}'
```

**Response**: Returns the generated `id`.

### 2. Activate Account

**Endpoint**: `POST /account/activate`

```json
/* Request Body */
{
    "id": "<ACCOUNT_ID>"
}
```

**Curl Command**:

```bash
curl -X POST http://localhost:8080/account/activate \
  -H "Content-Type: application/json" \
  -d '{"id": "YOUR_ACCOUNT_ID"}'
```

### 3. Credit Amount

**Endpoint**: `POST /account/credit`

```json
/* Request Body */
{
    "id": "<ACCOUNT_ID>",
    "amount": 100
}
```

**Curl Command**:

```bash
curl -X POST http://localhost:8080/account/credit \
  -H "Content-Type: application/json" \
  -d '{"id": "YOUR_ACCOUNT_ID", "amount": 100}'
```

### 4. Debit Amount

**Endpoint**: `POST /account/debit`

```json
/* Request Body */
{
    "id": "<ACCOUNT_ID>",
    "amount": 50
}
```

**Curl Command**:

```bash
curl -X POST http://localhost:8080/account/debit \
  -H "Content-Type: application/json" \
  -d '{"id": "YOUR_ACCOUNT_ID", "amount": 50}'
```

### 5. Close Account

**Endpoint**: `POST /account/close`

```json
/* Request Body */
{
    "id": "<ACCOUNT_ID>"
}
```

**Curl Command**:

```bash
curl -X POST http://localhost:8080/account/close \
  -H "Content-Type: application/json" \
  -d '{"id": "YOUR_ACCOUNT_ID"}'
```

### 6. Get Account Details

**Endpoint**: `GET /account/get/{id}`

**Curl Command**:

```bash
curl -X GET http://localhost:8080/account/get/YOUR_ACCOUNT_ID
```
