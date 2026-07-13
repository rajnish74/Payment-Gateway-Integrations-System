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
- Spring Security (JWT + BCrypt + AES-GCM Encryption)
- JJWT (JSON Web Token)
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
- Global Exception Handling (+ MethodArgumentNotValidException → HTTP 400 field errors)
- Merchant Registration API Structure
- API Key Management Module (Full CRUD — Service complete)
- Auth Controller & Service Structure
- Login API — JWT token generation on authenticate
- Order Controller Structure
- Payment Controller — Initiate & Capture Payment APIs
- PaymentServiceImpl — Full initiation + capture + resolveAuthorization flow
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
- Bank Callback Simulator — fully implemented with ChaosMode (SUCCESS/FAILURE/TIMEOUT/NORMAL/SLOW)
- JWT Authentication — JwtUtils, MerchantUserDetailsService, WebSecurityConfig (dual chain)
- JwtAuthenticationFilter — Bearer token extraction, SecurityContext + MerchantContext population
- ApiKeyAuthenticationFilter — Basic Auth (Base64 keyId:secret), BCrypt match, 24hr grace period fallback
- MerchantContext — @RequestScope scoped proxy, merchantId + keyId available per request
- ApiKeyServiceImpl — full CRUD with BCrypt hashing, soft delete, key rotation with grace period
- AuditorAwareImpl — JPA audit trail reads from MerchantContext (keyId → merchantId → SYSTEM)
- All controllers updated — hardcoded UUID removed, MerchantContext.getMerchantId() used throughout

---

## 🔄 In Progress

- Order Creation Business Logic (OrderServiceImpl)
- Merchant Signup (save to DB, hash password)
- Refund API
- Settlement Engine

---

## 🎯 Planned Features

- Refund API
- Settlement Engine
- Webhook Event Handling
- Dead Letter Queue (DLQ)
- Kafka Integration (Event-Driven Processing)
- Redis Caching & Idempotency Keys
- Rate Limiting
- Merchant Dashboard APIs
- Docker Deployment
- CI/CD Pipeline
- Unit & Integration Testing
- Monitoring & Logging

---

## 🌐 API Endpoints

### Auth — `/v1/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/auth/signup` | Merchant registration |
| POST | `/v1/auth/login` | Login — returns JWT access token |

### API Key Management — `/v1/merchants/api-keys`

> Requires JWT Bearer token. MerchantId resolved from token via MerchantContext.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/merchants/api-keys` | Create a new API key |
| GET | `/v1/merchants/api-keys` | List all API keys for merchant |
| DELETE | `/v1/merchants/api-keys/{keyId}` | Delete an API key (soft disable) |
| POST | `/v1/merchants/api-keys/{keyId}/rotate` | Rotate an API key (24hr grace period) |

### Orders — `/v1/orders`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/orders` | Create a new order |

### Payments — `/v1/payments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/payments` | Initiate a payment for an order |
| POST | `/v1/payments/{paymentId}/capture` | Capture an authorized payment |

### Vault — `/v1/vault`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/vault/tokenize` | Tokenize a card (returns `tok_` token) |

---

## 📂 Module Structure

### Merchant Module
- Merchant Registration + Login (JWT)
- API Key Management — CRUD, soft delete, rotation with 24hr grace period
- JWT Authentication Filter + API Key Authentication Filter
- MerchantContext — request-scoped merchant identity
- AuditorAware — JPA audit trail per request
- User Management, Customer Management

### Payment Module
- Orders
- Payments (initiate, capture, resolveAuthorization)
- Bank Callback Simulator (ChaosMode)
- Refunds (planned)
- Webhook Configuration

### Operations Module
- Settlements
- Settlement Payments
- Webhook Events
- DLQ Events

### Vault Module
- Card Tokenization (`tok_` prefix tokens)
- AES-GCM Envelope Encryption (PAN → DEK → Master Key)
- Card Brand Detection (VISA / MASTERCARD / AMEX / RUPAY)
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
JWT Filter  (/v1/auth/**, /v1/merchants/**)   |   API Key Filter  (/v1/orders/**, /v1/payments/**, /v1/vault/**)
      ↓
MerchantContext (request-scoped: merchantId, keyId)
      ↓
Controller Layer      (REST endpoints, input validation)
      ↓
Service Layer         (Business logic, state machine, transaction management)
      ↓
Gateway / Processor   (Adapter + Strategy pattern per payment method)
      ↓
Repository Layer      (Spring Data JPA + AuditorAware)
      ↓
PostgreSQL Database
```

---

## 🔮 Future Enhancements

- Razorpay-style Checkout Flow
- Kafka Integration (Event-Driven Processing)
- Redis Caching & Idempotency Keys
- Rate Limiting
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
- Secure API Development (JWT + API Key)
- Transaction Processing & State Machines
- Event-Driven Systems
- Scalable Software Architecture

---

## 👨‍💻 Author

**Rajnish Kumar**

- GitHub: [github.com/rajnish74](https://github.com/rajnish74)
- LinkedIn: [linkedin.com/in/rajnish-kumar-74](https://www.linkedin.com/in/rajnish-kumar-74/)