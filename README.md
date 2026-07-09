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
- Spring Security Crypto (AES-GCM Encryption)
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
- Payment Controller — Initiate & Capture Payment APIs
- PaymentServiceImpl — Full payment initiation + capture flow
- Payment State Machine — 14 transitions covering full payment lifecycle
- Payment Transition Log — Full audit trail of every state change
- Gateway Layer — Adapter Pattern (NetBanking & UPI fully wired, Card wired via Vault)
- Processor Layer — Strategy Pattern (NetBanking & UPI simulated, Card via VaultService)
- PaymentGatewayRouter & PaymentProcessorRouter
- Sealed Interface — PaymentResult & PaymentProcessorResponse (exhaustive switch)
- InvalidStateTransitionException — illegal state+event guard
- Vault Module — Card tokenization with envelope encryption (PAN → DEK → Master Key, AES-GCM)
- Card Brand Detection — VISA / MASTERCARD / AMEX / RUPAY
- Custom Validation — @ExpiryYear annotation + @LuhnCheck on PAN
- Bank Callback Simulator — @Scheduled poller with per-method chaos config

---

## 🔄 In Progress

- Authentication Module (JWT — Service wired, filter pending)
- Order Creation Business Logic (OrderServiceImpl)
- Bank Callback Simulator — simulateCallback() logic pending
- Refund API

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

### Vault — `/tokenize`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/tokenize` | Tokenize a card (returns `tok_` token) |

### Auth — `/v1/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/auth/signup` | Merchant registration |

### API Key Management — `/v1/merchants/{merchantId}/api-keys`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/merchants/{merchantId}/api-keys` | Create a new API key |
| GET | `/v1/merchants/{merchantId}/api-keys` | List all API keys for merchant |
| DELETE | `/v1/merchants/{merchantId}/api-keys/{keyId}` | Delete an API key |
| POST | `/v1/merchants/{merchantId}/api-keys/{keyId}/rotate` | Rotate an API key |

### Orders — `/v1/orders`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/orders` | Create a new order |

### Payments — `/v1/payments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/payments` | Initiate a payment for an order |
| POST | `/v1/payments/{paymentId}/capture` | Capture an authorized payment |

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
- Card Tokenization (`tok_` prefix tokens)
- AES-GCM Envelope Encryption (PAN → DEK → Master Key)
- Card Brand Detection
- Token-based charge routing to PaymentProcessorRouter

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