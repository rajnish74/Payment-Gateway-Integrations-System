# Payment Gateway Integration System

A Spring Boot-based Payment Gateway Integration System inspired by Razorpay architecture. This project is being built to understand how large-scale payment systems work — including merchant onboarding, API key management, payment processing, settlements, refunds, webhooks, and card tokenization.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- MapStruct (DTO Mapping)
- Git & GitHub

---

## 📌 Project Status

🚧 **Currently Under Development**

---

## ✅ Completed

- Project Setup & Configuration
- Package Structure Design (Domain-Driven)
- Database Entity Design (All Domains)
- Database Indexes on Entity Tables
- JPA Auditing (`@EnableJpaAuditing` — createdAt, updatedAt auto-managed)
- DTO Layer (Request & Response)
- MapStruct Integration (Merchant & Payment domain mappers)
- Repository Layer
- Controller Layer
- Service Layer Structure
- Global Exception Handling
- Merchant Registration API Structure
- API Key Management Module (Full CRUD — Service complete)
- Auth Controller & Service Structure
- Order Controller Structure

---

## 🔄 In Progress

- Authentication Module (JWT — Service wired, filter pending)
- Order Creation Business Logic
- Payment Module Business Logic

---

## 🎯 Planned Features

- JWT Authentication (filter & token validation)
- Customer Management
- Payment Processing (state machine)
- Refund API
- Settlement Engine
- Webhook Event Handling
- Dead Letter Queue (DLQ)
- Merchant Dashboard APIs
- Payment Analytics

---

## 🌐 API Endpoints (Working)

### API Key Management — `/api/keys`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/keys` | Create a new API key for a merchant |
| GET | `/api/keys/{id}` | Get API key details |
| PUT | `/api/keys/{id}` | Update API key |
| DELETE | `/api/keys/{id}` | Delete API key |

### Auth — `/api/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | Merchant registration (structure ready) |

### Orders — `/api/orders`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create order (in progress) |

---

## 📂 Module Structure

### Merchant Module
- Merchant Registration
- API Key Management (CRUD complete)
- User Management
- Customer Management

### Payment Module
- Orders
- Payments
- Refunds
- Webhook Configuration

### Operations Module
- Settlements
- Settlement Payments
- Webhook Events
- DLQ Events

### Vault Module
- Card Vault
- Card Token Management (Entity only, no API layer yet)

---

## 🗄️ Database Entities

**Merchant Domain** — Merchant, AppUser, ApiKey, Customer

**Payment Domain** — OrderRecord, Payments, Refund, MerchantWebhookConfig, PaymentTransitionLog

**Operations Domain** — Settlement, SettlementPayment, SettlementPaymentId, WebhookEvent, DLQEvent

**Vault Domain** — VaultCard, CardToken

---

## 🏗️ Architecture

```
Client Request
      ↓
Controller Layer  (REST endpoints, input validation)
      ↓
Service Layer     (Business logic, transaction management)
      ↓
Repository Layer  (Spring Data JPA)
      ↓
PostgreSQL Database
```

---

## 🔮 Future Enhancements

- Razorpay-style Checkout Flow
- API Key Rotation
- Rate Limiting
- Kafka Integration (Event-Driven Processing)
- Redis Caching
- Docker Deployment
- CI/CD Pipeline
- Unit & Integration Testing
- Monitoring & Logging

---

## 📖 Learning Objectives

This project is being built to gain hands-on experience with:

- Payment Gateway Architecture
- Spring Boot Backend Development
- Database Design & Relationships
- Secure API Development
- Transaction Processing
- Event-Driven Systems
- Scalable Software Architecture

---

## 👨‍💻 Author

**Rajnish Kumar**

- GitHub: [github.com/rajnish74](https://github.com/rajnish74)
- LinkedIn: [linkedin.com/in/rajnish-kumar-74](https://www.linkedin.com/in/rajnish-kumar-74/)