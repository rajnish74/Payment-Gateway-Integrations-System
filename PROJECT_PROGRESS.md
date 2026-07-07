# Project Progress — Payment Gateway Integration System

## Current Status: 🚧 Under Development

---

## Phase 1: Project Setup & Foundation ✅ Complete

- Spring Boot Project Initialization
- PostgreSQL Configuration (`application.yaml`)
- Maven Setup
- Package Structure Design (Domain-Driven: merchant, payment, operations, vault, common)
- Common Enums (BusinessType, Environment, MerchantStatus, OrderStatus, PaymentActor, PaymentEvent, PaymentMethod, PaymentStatus, RefundStatus, SettlementStatus, UserRole, WebhookEventStatus)
- Common Entity (Money)
- Global Exception Handling (DuplicateResourceException, ResourceNotFoundException, GlobalExceptionHandler, ErrorResponse)
- DTO Layer Setup
- Database Indexes added on all entity tables
- JPA Auditing enabled (`@EnableJpaAuditing`) — createdAt & updatedAt auto-managed
- MapStruct integrated — Merchant & Payment domain mappers created

---

## Phase 2: Merchant Module ✅ Complete

### Entities
- Merchant (with indexes + JPA auditing)
- AppUser (with indexes + JPA auditing)
- ApiKey (with indexes + JPA auditing)
- Customer (with indexes + JPA auditing)

### Repositories
- MerchantRepository
- AppUserRepository
- ApiKeyRepository

### DTOs
- Request: MerchantSignupRequest, CreateApiKeyRequest
- Response: MerchantResponse, ApiKeyResponse, ApiKeyCreateResponse

### MapStruct Mappers
- MerchantMapper (Merchant ↔ MerchantResponse)
- ApiKeyMapper (ApiKey ↔ ApiKeyResponse, ApiKeyCreateResponse)

### Controllers
- AuthController (signup endpoint — structure ready)
- ApiKeyController (full CRUD)

### Services
- AuthService / AuthServiceImpl (structure ready)
- ApiKeyService / ApiKeyServiceImpl ✅ fully implemented

### What's working
- Create API Key → `POST /api/keys`
- Get API Key → `GET /api/keys/{id}`
- Update API Key → `PUT /api/keys/{id}`
- Delete API Key → `DELETE /api/keys/{id}`

---

## Phase 3: Payment Module 🔄 In Progress

### Entities Created
- OrderRecord
- Payments
- Refund
- MerchantWebhookConfig
- PaymentTransitionLog

### Repositories Created
- OrderRepository
- PaymentRepository

### Controllers
- OrderController (structure ready)
- PaymentController ✅ — `POST /v1/payments` (initiate payment)

### DTOs
- Request: CreateOrderRequest, PaymentInitRequest (orderId, method, methodDetails)
- Response: OrderResponse, PaymentResponse

### MapStruct Mappers
- OrderMapper (OrderRecord ↔ OrderResponse)
- PaymentMapper (Payments ↔ PaymentResponse) ✅

### Services
- OrderService / OrderServiceImpl (structure ready)
- PaymentService / PaymentServiceImpl ✅ fully implemented
  - Order status validation (CREATED / ATTEMPTED only)
  - Order attempt counter increment
  - Payment entity creation & persistence
  - Routes to PaymentGatewayRouter
  - Handles PaymentResult (Pending / Failure) via sealed interface pattern
  - BusinessRuleViolationException for invalid order state

### Gateway Layer (Adapter Pattern) ✅
- `PaymentAdapter` interface — `initiate(PaymentRequest)`
- `CardPaymentAdapter` — Card payments
- `NetBankingAdapter` — Net banking
- `UPIPaymentAdapter` — UPI payments
- `PaymentGatewayRouter` — Routes by PaymentMethod to correct adapter
- `PaymentAdapterConfig` — Registers adapters as Spring beans (Map<PaymentMethod, PaymentAdapter>)
- `PaymentRequest` record — paymentId, orderId, merchantId, amount, method, methodDetails
- `PaymentResult` sealed interface — Pending(registrationRef) | Failure(errorCode, errorDescription)

### Processor Layer (Strategy Pattern) ✅
- `PaymentProcessor` interface — `charge(PaymentProcessorRequest)`
- `CardPaymentProcessor` — Card strategy
- `NetBankingPaymentProcessor` — Net banking strategy
- `UPIPaymentProcessor` — UPI strategy
- `PaymentProcessorRouter` — Routes by PaymentMethod to correct processor
- `PaymentProcessorConfig` — Registers processors as Spring beans (Map<PaymentMethod, PaymentProcessor>)
- `PaymentProcessorRequest` record — method, amount, methodDetails
- `PaymentProcessorResponse` sealed interface — Pending | Success(processorRef, bankRef) | Failure

### State Machine ✅
- `PaymentStateMachine` — pure transition table (Map<Transition, PaymentStatus>)
  - CREATED → AUTHORIZING → AUTHORIZED → CAPTURING → CAPTURED → SETTLED
  - CAPTURED/SETTLED → PARTIALLY_REFUNDED → REFUNDED
  - AUTHORIZED → AUTH_EXPIRED (capture timeout)
  - CREATED/AUTHORIZING → CANCELLED
- `PaymentTransitionLogService` — applies transition, persists log entry with actor & timestamp
- `PaymentTransitionLogRepository` — JPA repository for audit log
- `InvalidStateTransitionException` — thrown on illegal state+event combo

### Design Patterns Used
- **Strategy Pattern** — PaymentProcessor per method (Card/UPI/NetBanking)
- **Adapter Pattern** — PaymentAdapter per gateway
- **Router Pattern** — PaymentGatewayRouter & PaymentProcessorRouter dispatch by PaymentMethod
- **Sealed Interface** — PaymentResult & PaymentProcessorResponse (exhaustive switch)
- **State Machine Pattern** — PaymentStateMachine with immutable transition table

### Processor Implementations ✅
- `NetBankingPaymentProcessor` — simulation with BANK_CODE_FAIL trigger, returns Success/Failure
- `UPIPaymentProcessor` — simulation with `fail@axis` VPA trigger, returns Success/Failure
- `PaymentProcessorRequest` — factory methods `card()` and `nonCard()` for clean construction

### Adapter Implementations ✅
- `NetBankingAdapter` — full initiate() with processor routing + capture() stub
- `UPIPaymentAdapter` — full initiate() with processor routing + capture() stub
- `CardPaymentAdapter` — initiate() + capture() stubs (implementation pending)
- All adapters handle exceptions and return `PaymentResult.Failure` on error

### Capture Flow ✅
- `PaymentController` — `POST /v1/payments/{paymentId}/capture`
- `PaymentServiceImpl.capture()` — fetches payment, applies CAPTURE_REQUEST transition, calls gateway, applies CAPTURE_SUCCESS/CAPTURE_FAILURE transition, persists

### Pending
- Order creation business logic (amount validation, merchant lookup)
- CardPaymentProcessor actual logic
- CardPaymentAdapter actual logic
- Refund API
- Webhook config endpoints

---

## Phase 4: Operations Module ⏳ Pending

### Entities Created
- Settlement
- SettlementPayment
- SettlementPaymentId
- WebhookEvent
- DLQEvent

### Pending
- Settlement processing logic
- Webhook event dispatch
- DLQ retry mechanism
- All controllers, services, repositories

---

## Phase 5: Vault Module ⏳ Pending

### Entities Created
- VaultCard
- CardToken

### Pending
- Card tokenization logic
- Token encryption / secure storage
- All controllers, services, repositories

---

## Phase 6: Cross-Cutting Concerns ⏳ Pending

- JWT Authentication (filter, token generation, validation)
- API Key authentication middleware
- Rate limiting
- Request/response logging
- Unit tests
- Integration tests

---

## Upcoming Tasks (Priority Order)

1. Order Creation API full implementation (OrderServiceImpl business logic)
2. Complete Merchant Signup (AuthServiceImpl — save to DB)
3. Implement JWT token generation and filter
4. Card/UPI/NetBanking actual processor logic
5. Refund API
6. Settlement processing

---

## Daily Development Log

### 14 June 2026
- GitHub Repository Setup
- Initial Commit
- README Documentation
- Entity Layer Design (all domains)
- Repository Layer Setup
- Controller Layer Setup

### 15 June 2026
- Implemented ApiKeyController — full CRUD (Create, Get, Update, Delete)
- Enhanced API Key Management Module (service + repository wired)
- OrderController structure created
- Order DTOs (CreateOrderRequest, OrderResponse)
- OrderService and OrderServiceImpl skeleton

### 23 June 2026
- ApiKeyServiceImpl fully implemented (all CRUD business logic complete)
- Database indexes added on entity tables (Merchant, AppUser, ApiKey, Customer, OrderRecord)
- JPA Auditing enabled via `@EnableJpaAuditing` — createdAt & updatedAt auto-populated
- MapStruct integrated — mapper interfaces created for Merchant domain and Payment domain
- Security not added yet — will be implemented with JWT in upcoming phase

### 24 June 2026
- PaymentController implemented — `POST /v1/payments` (initiate payment endpoint)
- PaymentServiceImpl fully implemented
  - Order status validation before payment (only CREATED/ATTEMPTED allowed)
  - Order attempt counter auto-increment
  - Payment entity creation and DB persistence
  - Integrated with PaymentGatewayRouter
  - Result handling via sealed interface switch (Pending → store ref, Failure → store error)
  - Added BusinessRuleViolationException for invalid order state
- Gateway Layer implemented (Adapter Pattern)
  - PaymentAdapter interface
  - CardPaymentAdapter, NetBankingAdapter, UPIPaymentAdapter (structure ready)
  - PaymentGatewayRouter — dispatches by PaymentMethod
  - PaymentAdapterConfig — registers adapters as Spring beans
  - PaymentRequest & PaymentResult (sealed) records
- Processor Layer implemented (Strategy Pattern)
  - PaymentProcessor interface
  - CardPaymentProcessor, NetBankingPaymentProcessor, UPIPaymentProcessor (structure ready)
  - PaymentProcessorRouter — dispatches by PaymentMethod
  - PaymentProcessorConfig — registers processors as Spring beans
  - PaymentProcessorRequest & PaymentProcessorResponse (sealed) records

### 25 June 2026
- Payment State Machine implemented
  - PaymentStateMachine — full transition table (14 transitions covering complete payment lifecycle)
  - PaymentTransitionLogService — applies transition + persists audit log with actor & timestamp
  - PaymentTransitionLogRepository added
  - InvalidStateTransitionException added to common exceptions
- Capture flow implemented
  - `POST /v1/payments/{paymentId}/capture` endpoint added to PaymentController
  - PaymentServiceImpl.capture() — state transition (CAPTURE_REQUEST → SUCCESS/FAILURE), gateway call, DB persist
- Processor implementations added
  - NetBankingPaymentProcessor — simulation logic (BANK_CODE_FAIL trigger)
  - UPIPaymentProcessor — simulation logic (fail@axis VPA trigger)
  - PaymentProcessorRequest — factory methods card() and nonCard() added
- Adapter implementations added
  - NetBankingAdapter — full initiate() with processor routing, capture() stub
  - UPIPaymentAdapter — full initiate() with processor routing, capture() stub
  - All adapters have try-catch with PaymentResult.Failure fallback