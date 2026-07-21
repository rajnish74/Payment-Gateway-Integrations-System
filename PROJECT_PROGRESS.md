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
- `CardPaymentAdapter` ✅ fully wired — extracts token from methodDetails → calls VaultService.charge() → maps ProcessorResponse to PaymentResult

### Bank Callback Simulator ✅ Complete
- `BankCallbackSimulator` — `@Scheduled` polling AUTHORIZING payments older than 1s
- `simulateCallback()` — fully implemented
  - Reads per-method config (minDelay, maxDelay, successRate)
  - Calculates `dueAt` from payment createdAt + method delay (SLOW mode doubles delay)
  - ChaosMode switch — SUCCESS/FAILURE/TIMEOUT/NORMAL/SLOW all handled
  - `shouldApproved()` — deterministic bucket via `paymentId.hashCode() % 100`
- `resolve()` — calls `paymentService.resolveAuthorization()` with approve/reject
- `SimulatorConfig` — `configfor(PaymentMethod)` lookup with default fallback
- `resolveAuthorization()` in PaymentServiceImpl ✅ fully implemented
  - Validates AUTHORIZING status before processing
  - approve=true → AUTHORIZE_SUCCESS → auto-capture → CAPTURE_REQUEST → CAPTURE_SUCCESS/FAILURE
  - approve=false → AUTHORIZE_FAILURE, stores errorCode + errorDescription
  - OrderRecord status updated to PAID on successful capture
  - Persists both payments + orderRecord

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

## Phase 5: Vault Module ✅ Mostly Complete

### Entities
- VaultCard (brand, lastFourDigits, bin, encryptedPan, encryptedDek, expiryMonth, expiryYear, cardholderName)
- CardToken (token, vaultCard, customer, merchant, revokedAt)

### Repositories
- VaultCardRepository
- CardTokenRepository — `findByTokenAndRevokedAtIsNull(token)`

### DTOs
- Request: TokenizeRequest (pan, cvv, expiryMonth, expiryYear, customerId, cardholderName)
- Response: TokenizeResponse (token, lastFour, brand, expiryMonth, expiryYear)

### Controller
- VaultController — `POST /tokenize`

### Service
- VaultService interface — `tokenize()`, `charge()`
- VaultServiceImpl ✅ fully implemented
  - PAN last-4 & BIN extraction
  - Card brand detection (VISA/MASTERCARD/AMEX/RUPAY)
  - Per-card DEK (Data Encryption Key) generation (AES-256)
  - PAN encrypted with DEK (AES-GCM)
  - DEK encrypted with master key (AES-GCM)
  - Token generated with `tok_` prefix + random Base64
  - charge() — decrypts DEK → decrypts PAN → routes to PaymentProcessorRouter → zeroes PAN bytes in finally block

### Encryption (VaultEncryption config)
- Master key loaded from `vault.master-key` (application.yaml)
- `panEncrypt(dek)` — static helper, AES-GCM with secure random IV
- `dekEncrypt()` — Spring Bean, AES-GCM with master key
- Double-layer encryption: PAN → DEK → Master Key (envelope encryption)

### Validation
- `@ExpiryYear` — custom annotation on TokenizeRequest
- `ExpiryYearValidator` — validates expiryMonth + expiryYear not before current YearMonth
- PAN validated with `@LuhnCheck` (Luhn algorithm) + regex (13-19 digits)
- CVV validated with regex (3-4 digits)

### Pending
- Token revocation endpoint
- Card listing per customer/merchant

---

## Phase 6: Security & Cross-Cutting Concerns ✅ Complete

### JWT Authentication ✅ Implemented
- JwtUtils — HMAC-SHA, generateAccessToken(email, merchantId, role) + verifyAccessToken()
- MerchantUserDetailsService — UserDetailsService loading AppUser by email
- WebSecurityConfig — dual chain (JWT routes + API Key routes), stateless, CSRF disabled
- AuthenticationManager — DaoAuthenticationProvider + BCryptPasswordEncoder
- LoginRequest + LoginResponse records
- AuthServiceImpl.login() — authenticate → load user → generate JWT → return token
- POST /v1/auth/login added to AuthController
- GlobalExceptionHandler — MethodArgumentNotValidException → HTTP 400 VALIDATION_FAILED with field errors

### Also Completed (13 July 2026)
- JwtAuthenticationFilter — Bearer token extraction, claim verification, SecurityContext + MerchantContext set
- ApiKeyAuthenticationFilter — Basic Auth decode, BCrypt match, grace period fallback, MerchantContext set
- MerchantContext — @RequestScope scoped proxy, holds merchantId + keyId per request
- WebSecurityConfig — both filters wired into correct chains with @Order(1)/@Order(2)
- ApiKeyServiceImpl — createApiKey, listKeys, deleteApiKey (soft), rotateApiKey (24hr grace period)
- All controllers — hardcoded UUID replaced with MerchantContext.getMerchantId()
- AuditorAwareImpl — reads keyId → merchantId → SYSTEM for JPA audit trail

### Pending
- Unit & Integration tests

---

## Phase 7: Redis Caching & Rate Limiting ✅ Complete

### Redis Integration
- RedisConfig — StringRedisTemplate bean
- Spring Data Redis configured via application.yaml (env var support)

### API Key Cache (Redis-backed)
- ApiKeyCache interface + RedisApiKeyCache implementation
- ApiKeyCacheEntry record with isInGracePeriod() helper
- Cache-aside pattern in ApiKeyAuthenticationFilter — DB only on cache miss
- TTL: 5 minutes, key prefix: "api_key:"
- Graceful degradation — cache errors fall back to DB transparently

### Fixed Window Rate Limiter
- RateLimiter interface + FixedWindowRateLimiter (@ConditionalOnProperty)
- Redis INCR + EXPIRE atomic pattern for window counting
- RateLimitResult record — allowed/denied factory methods
- RateLimitException with retryAfterSeconds
- Per API key rate limiting ("apiKey:{keyId}")
- X-RateLimit-Limit + X-RateLimit-Remaining response headers
- Configurable via application.yaml per use-case

### Sliding Window Rate Limiter ✅ (13 July 2026 addition)
- `SlidingWindowRateLimiter` — Redis ZSet, ZREMRANGEBYSCORE + ZCARD + ZADD per request
  - Prunes expired members each request, no burst at window boundary
  - Computes retryAfter from oldest member score
  - @ConditionalOnProperty(`app.rate-limit.method=sliding`)
- `SlidingWindowLuaLimiter` — atomic Lua script version (sliding-lua)
  - Single round-trip: prune + count + add + expire in one atomic Lua execution
  - Returns `[allowed, remaining, oldestScore]` — retryAfter computed from oldestScore
  - Fail-open on Redis DataAccessException
  - @ConditionalOnProperty(`app.rate-limit.method=sliding-lua`)
- `TokenBucketRateLimiter` — continuous token refill via Lua script (bucket)
  - Capacity = maxRequests, refill rate = maxRequests / windowSeconds tokens/sec
  - State (tokens, ts) stored in Redis Hash — HMGET + HMSET + EXPIRE atomic in Lua
  - No burst at window edge — tokens trickle back continuously
  - Fail-open on Redis DataAccessException
  - @ConditionalOnProperty(`app.rate-limit.method=bucket`)
- All three strategies pluggable via single config property — zero code change to switch

### Idempotency Layer ✅ (13 July 2026 addition)
- `IdempotencyStore` interface — `setIfAbsent()`, `store()`, `get()`, `remove()`
- `RedisIdempotencyStore` — Redis-backed implementation
  - `setIfAbsent()` — atomic Redis SET NX with IN_PROGRESS sentinel + 30s TTL
  - `store()` — persists `{status}|{body}` string with 24hr TTL on success
  - `remove()` — clears placeholder on error/empty response so client can retry cleanly
  - Fail-open on DataAccessException
- `IdempotencyFilter` — OncePerRequestFilter, guards POST/PUT/PATCH
  - Scoped key: `{merchantId}:{X-Idempotency-Key}` (or raw key if no merchant context)
  - First request — claims key, wraps response in ContentCachingResponseWrapper
  - Duplicate request (IN_PROGRESS) → 409 IdempotencyConflictException
  - Duplicate request (COMPLETED) → replays stored status + body exactly
  - Error responses (4xx/5xx) → placeholder removed, client can safely retry
- `IdempotencyConflictException` — custom exception for 409 conflict
- `WebSecurityConfig` updated — IdempotencyFilter added after JWT + API Key filters in both chains

---

## Upcoming Tasks (Priority Order)

1. Order Creation API full implementation (OrderServiceImpl business logic)
2. Complete Merchant Signup (AuthServiceImpl — save to DB, hash password)
3. Refund API
4. Settlement Engine + Webhook Dispatch
5. Operations Module (DLQ, SettlementPayment)
6. RateLimitFilter — wire RateLimiter into HTTP filter chain with response headers
7. Unit & Integration Testing

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

### 26 June 2026
- Vault Module fully implemented
  - VaultServiceImpl — tokenize() + charge()
  - Double-layer envelope encryption: PAN → DEK (AES-GCM) → Master Key (AES-GCM)
  - Per-card DEK generation (AES-256, 32 bytes)
  - Card brand detection from PAN prefix (VISA/MASTERCARD/AMEX/RUPAY)
  - Token generation with `tok_` prefix
  - charge() — DEK decryption → PAN decryption → processor routing → PAN zero-fill in finally
  - VaultEncryption config — master key from application.yaml
  - VaultController — `POST /tokenize`
  - VaultCardRepository + CardTokenRepository (findByTokenAndRevokedAtIsNull)
- Custom validation added
  - @ExpiryYear annotation + ExpiryYearValidator (YearMonth comparison)
  - @LuhnCheck on PAN field
  - CVV + PAN regex validation
- CardPaymentAdapter fully wired — token → VaultService.charge() → PaymentResult mapping
- Bank Callback Simulator structure added
  - BankCallbackSimulator — @Scheduled polling AUTHORIZING payments
  - SimulatorConfig — @ConfigurationProperties with per-method success rates
  - ChaosMode enum added
  - application.yaml updated with simulator + vault config

### 27 June 2026
- BankCallbackSimulator fully implemented
  - simulateCallback() — per-method delay calculation, ChaosMode switch (SUCCESS/FAILURE/TIMEOUT/NORMAL/SLOW)
  - shouldApproved() — deterministic success/failure via paymentId.hashCode() % 100 < successRate
  - dueAt() — delay = minDelay + hash-bucket offset; SLOW mode doubles delay
  - resolve() — calls paymentService.resolveAuthorization(approve/reject)
  - SimulatorConfig.configfor() — per-method lookup with MethodSimulatorConfig default fallback
- PaymentServiceImpl.resolveAuthorization() implemented
  - AUTHORIZING status guard (warns and skips if wrong state)
  - approve=true → AUTHORIZE_SUCCESS → auto-capture (CAPTURE_REQUEST → CAPTURE_SUCCESS/FAILURE)
  - approve=false → AUTHORIZE_FAILURE, stores errorCode + errorDescription
  - OrderRecord.orderStatus = PAID on successful capture
- Security module implemented
  - JwtUtils — HMAC-SHA generateAccessToken() + verifyAccessToken() (merchant_id + role claims, 100min expiry)
  - MerchantUserDetailsService — UserDetailsService loading AppUser by email
  - WebSecurityConfig — dual security chain (JWT routes + API Key routes), stateless, CSRF disabled
  - AuthenticationManager — DaoAuthenticationProvider + BCryptPasswordEncoder
  - LoginRequest record + LoginResponse record
  - AuthServiceImpl.login() — AuthenticationManager.authenticate() → load user → generate JWT
  - POST /v1/auth/login endpoint added to AuthController
  - GlobalExceptionHandler — MethodArgumentNotValidException handler (HTTP 400, VALIDATION_FAILED, field errors list)

### 13 July 2026
- JWT Authentication Filter implemented
  - JwtAuthenticationFilter — OncePerRequestFilter, extracts Bearer token, verifies via JwtUtils
  - Sets SecurityContext authentication with ROLE_{role} authority
  - Sets MerchantContext.merchantId from JWT claims
  - HandlerExceptionResolver delegates auth errors cleanly
  - JwtUtils — extractRole() + extractMerchantId() helper methods added
- API Key Authentication Filter implemented
  - ApiKeyAuthenticationFilter — OncePerRequestFilter, Basic Auth header decode (Base64 keyId:secret)
  - Looks up ApiKey by keyId → validates enabled status + BCrypt secret match
  - Grace period support — falls back to previousKeySecretHash if within gracePeriodExpiresAt
  - Sets SecurityContext + MerchantContext (merchantId + keyId)
  - HandlerExceptionResolver delegates auth errors cleanly
- MerchantContext — @RequestScope bean (scoped proxy) holding merchantId + keyId per request
- WebSecurityConfig — dual chain fully wired with filters
  - JWT chain (@Order 1) — /v1/auth/**, /v1/merchants/**, /v1/admin/** — JwtAuthenticationFilter added
  - API Key chain (@Order 2) — /v1/orders/**, /v1/payments/**, /v1/vault/** — ApiKeyAuthenticationFilter added
- ApiKeyServiceImpl fully implemented
  - createApiKey() — generates rzp_{env}_{random} keyId + random secret, BCrypt hash stored
  - getListMerchantApiKeys() — merchant-scoped list
  - deleteApiKey() — soft delete (enabled=false), merchant ownership validated
  - rotateApiKey() — new secret generated, old hash saved as previousKeySecretHash, 24hr grace period set
- Controllers updated — hardcoded UUID removed, now uses MerchantContext.getMerchantId()
  - OrderController, PaymentController, VaultController, ApiKeyController all MerchantContext wired
- ApiKeyController — endpoint changed from /v1/merchants/{merchantId}/api-keys to /v1/merchants/api-keys (merchantId from context)
- AuditorAwareImpl — reads keyId from MerchantContext for JPA audit trail, falls back to merchant_id, then SYSTEM

### 21 July 2026
- Redis integration added
  - RedisConfig — StringRedisTemplate bean wired with RedisConnectionFactory
  - application.yaml — Redis host/port/password via env vars (REDIS_HOST, REDIS_PORT, REDIS_PASSWORD)
  - Spring Data Redis dependency added
- API Key Cache (Redis-backed) implemented
  - ApiKeyCache interface — get(), put(), evict()
  - ApiKeyCacheEntry record — keyId, keySecretHash, previousKeySecretHash, gracePeriodExpiresAt, merchantId, environment, enabled
    - isInGracePeriod() helper method on record
  - RedisApiKeyCache — StringRedisTemplate + Jackson ObjectMapper, TTL: 5 minutes, prefix: "api_key:"
    - get() — deserializes JSON from Redis, returns Optional.empty() on miss or error
    - put() — serializes to JSON, sets with 5min TTL
    - evict() — deletes key from Redis
  - ApiKeyAuthenticationFilter updated — cache-first lookup (apiKeyCache.get()), falls back to DB + cache on miss (loadAndCache())
    - Direct DB call (findByKeyId) replaced with cache-aside pattern
- Rate Limiting implemented
  - RateLimiter interface — check(key, maxRequests, windowSeconds) → RateLimitResult
  - RateLimitResult record — isAllowed, remaining, retryAfterSeconds + static allowed()/denied() factory methods
  - FixedWindowRateLimiter — @ConditionalOnProperty(app.rate-limit.method=fixed)
    - Redis INCR + EXPIRE pattern for atomic fixed window counting
    - First request sets TTL, subsequent requests increment
    - Returns remaining count on allow, retryAfter TTL on deny
  - RateLimitException — RuntimeException with retryAfterSeconds + remaining fields
  - ApiKeyAuthenticationFilter — rate limit check after API key validation
    - Key: "apiKey:{keyId}" — per-key rate limiting
    - X-RateLimit-Limit header added to response
    - X-RateLimit-Remaining header added to response
    - RateLimitException thrown on exceed → handled by HandlerExceptionResolver
  - application.yaml — app.rate-limit.method=fixed, api-key.requests-per-minute: 2 (testing value)
  - @Value("${app.rate-limit.use-case.api-key.requests-per-minute:60}") — configurable per use case

### 21 July 2026
- Three rate limiter strategies implemented (all pluggable via app.rate-limit.method config):
  - SlidingWindowRateLimiter — Redis ZSet, non-atomic (no burst at window boundary)
  - SlidingWindowLuaLimiter — atomic Lua version, single round-trip to Redis
  - TokenBucketRateLimiter — continuous token refill via Lua, no window-edge burst at all
  - All three fail-open on Redis DataAccessException
- Idempotency layer fully implemented:
  - IdempotencyStore interface + RedisIdempotencyStore (SET NX sentinel pattern)
  - IdempotencyFilter — POST/PUT/PATCH guarded, ContentCachingResponseWrapper for response replay
  - Scoped key: merchantId + X-Idempotency-Key header
  - IN_PROGRESS → 409 conflict, COMPLETED → exact replay (status + body), ERROR → placeholder removed
  - IdempotencyConflictException added to common exceptions
  - WebSecurityConfig updated — IdempotencyFilter wired after auth filters in both JWT + API Key chains