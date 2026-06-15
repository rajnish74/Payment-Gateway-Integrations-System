# Payment Gateway Integration System

A Spring Boot-based Payment Gateway Integration System inspired by Razorpay architecture. This project is being developed to understand how large-scale payment systems work, including merchant onboarding, API key management, payment processing, settlements, refunds, webhooks, and card tokenization.

---

## 🚀 Tech Stack

* Java 21
* Spring Boot 3
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok
* Git & GitHub

---

## 📌 Project Status

🚧 **Currently Under Development**

### ✅ Completed

* Project Setup
* Package Structure Design
* Database Entity Design
* DTO Layer
* Repository Layer
* Controller Layer
* Service Layer Structure
* Merchant Registration API Structure
* API Key Management Module Structure
* Global Exception Handling
* API Key CRUD Operations

### 🔄 In Progress

* Authentication Module
* Payment Module
* Business Logic Implementation
* Database Relationships & Validations

### 🎯 Planned Features

* JWT Authentication
* Customer Management
* Order Management
* Payment Processing
* Refund Processing
* Settlement Engine
* Webhook Event Handling
* Dead Letter Queue (DLQ)
* Merchant Dashboard APIs
* Payment Analytics

---

## 📂 Current Module Structure

### Merchant Module

* Merchant Registration
* API Key Management
* User Management

### Payment Module

* Orders
* Payments
* Refunds
* Webhook Configuration

### Operations Module

* Settlements
* Settlement Payments
* Webhook Events
* DLQ Events

### Vault Module

* Card Vault
* Card Token Management

---

## 🗄️ Database Entities

### Merchant Domain

* Merchant
* AppUser
* ApiKey
* Customer

### Payment Domain

* OrderRecord
* Payments
* Refund
* MerchantWebhookConfig
* PaymentTransitionLog

### Operations Domain

* Settlement
* SettlementPayment
* SettlementPaymentId
* WebhookEvent
* DLQEvent

### Vault Domain

* VaultCard
* CardToken

---

## 🏗️ Project Architecture

```text
Controller Layer
        ↓
Service Layer
        ↓
Repository Layer
        ↓
PostgreSQL Database
```

---

## 🔮 Future Enhancements

* Razorpay-style Checkout Flow
* API Key Rotation
* Rate Limiting
* Event-Driven Processing
* Kafka Integration
* Redis Caching
* Monitoring & Logging
* Docker Deployment
* CI/CD Pipeline
* Unit & Integration Testing

---

## 📖 Learning Objectives

This project is being built to gain hands-on experience with:

* Payment Gateway Architecture
* Spring Boot Backend Development
* Database Design & Relationships
* Secure API Development
* Transaction Processing
* Event-Driven Systems
* Scalable Software Architecture

---

## 👨‍💻 Author

**Rajnish Kumar**

GitHub: https://github.com/rajnish74
LinkedIn: https://www.linkedin.com/in/rajnish-kumar-74/