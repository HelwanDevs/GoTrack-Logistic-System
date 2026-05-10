# Clean Code Improvement Analysis — GoTrack Logistics

> **Project**: GoTrack Logistics (Spring Boot 4.0 Microservices)  
> **Analysis Date**: 2026-05-10  
> **Scope**: 5 backend services — auth-service, inventory-service, user-branch-service, core-logistic-finance, support-and-notifications-service  
> **Total Java Files Analyzed**: ~205  
> **Issues Identified**: 47 across 10 categories  

---

## Executive Summary

This analysis reviews the GoTrack Logistics microservices codebase against Uncle Bob's Clean Code principles, SOLID principles, and DRY. The project demonstrates a **solid architectural foundation** (layered architecture, proper DTOs, exception hierarchy, state machines), but suffers from **significant systematic code quality issues** that undermine maintainability, readability, and correctness.

### Key Findings at a Glance

| Category | Count | Severity |
|----------|-------|----------|
| Naming Violations | 14 | Medium |
| Inconsistent Constructor Injection | 9 | Medium |
| Magic Strings / Hardcoded Values | 8 | Medium–High |
| God Methods / Deep Nesting | 7 | High |
| DRY Violations (Duplication) | 6 | Medium |
| Error Handling Issues | 5 | High |
| Method Size / Complexity | 4 | High |
| Security Concerns | 3 | Critical |
| Code Smells / Anti-patterns | 2 | Medium |
| Missing Validation / Guards | 1 | Medium |

### Most Impactful Changes Recommended

1. **Replace all `new AuthenticationDetails()` instantiations with Spring-injected beans** (affects 5+ services)
2. **Replace hardcoded role strings** (`"MERCHANT"`, `"ADMIN"`, etc.) with `Role` enum comparisons
3. **Refactor `AccountService.java` (246 lines, 6 methods) — extract authorization logic into dedicated policy classes**
4. **Rename all non-conforming identifiers** to follow Java naming conventions (camelCase, PascalCase)
5. **Replace `System.out.println` with proper `@Slf4j` logging** throughout
6. **Externalize magic numbers** (JWT expiration, pagination defaults) to configuration properties

---

## Table of Contents

- [1. auth-service](#1-auth-service)
  - [1.1 Naming Violations](#11-naming-violations)
  - [1.2 Constructor Injection Inconsistency](#12-constructor-injection-inconsistency)
  - [1.3 Security: e.printStackTrace()](#13-security-eprintstacktrace)
  - [1.4 God Method: AccountService](#14-god-method-accountservice)
  - [1.5 Magic Numbers](#15-magic-numbers)
  - [1.6 Boolean Primitive vs Boolean Wrapper](#16-boolean-primitive-vs-boolean-wrapper)
- [2. inventory-service](#2-inventory-service)
  - [2.1 Magic Strings for Role Checks](#21-magic-strings-for-role-checks)
  - [2.2 AuthenticationDetails Instantiated with `new`](#22-authenticationdetails-instantiated-with-new)
  - [2.3 Duplication: Authorization Logic Repeated](#23-duplication-authorization-logic-repeated)
  - [2.4 Naming Violations](#24-naming-violations)
  - [2.5 Swallowed Exceptions](#25-swallowed-exceptions)
  - [2.6 Spellings / Typos](#26-spellings--typos)
  - [2.7 Method: getProducts returns Page but throws for empty](#27-method-getproducts-returns-page-but-throws-for-empty)
- [3. user-branch-service](#3-user-branch-service)
  - [3.1 Magic Strings for Role Checks](#31-magic-strings-for-role-checks)
  - [3.2 Duplicate Authorization Checks](#32-duplicate-authorization-checks)
  - [3.3 AuthenticationDetails with `new`](#33-authenticationdetails-with-new)
  - [3.4 Constructor Injection Inconsistency](#34-constructor-injection-inconsistency)
  - [3.5 Violated DRY: Self-Referencing List](#35-violated-dry-self-referencing-list)
- [4. core-logistic-finance](#4-core-logistic-finance)
  - [4.1 Naming Violations (Massive)](#41-naming-violations-massive)
  - [4.2 Magic Numbers in FinanceService](#42-magic-numbers-in-financeservice)
  - [4.3 Method Duplication / Redundant Calculations](#43-method-duplication--redundant-calculations)
  - [4.4 Constructor Injection Inconsistency](#44-constructor-injection-inconsistency)
  - [4.5 Magic String Comparisons](#45-magic-string-comparisons)
  - [4.6 Boolean Primitive Comparison with `==`](#46-boolean-primitive-comparison-with)
  - [4.7 Method: `buildSummary` — Sequential Getters](#47-method-buildsummary--sequential-getters)
- [5. support-and-notifications-service](#5-support-and-notifications-service)
  - [5.1 System.out.println in Production Code](#51-systemoutprintln-in-production-code)
  - [5.2 Constructor Injection Inconsistency](#52-constructor-injection-inconsistency)
  - [5.3 Service That Is Just a Pass-through Wrapper](#53-service-that-is-just-a-pass-through-wrapper)
  - [5.4 Security: Access Denied Not Checked for MarkAsRead](#54-security-access-denied-not-checked-for-markasread)
  - [5.5 Duplication: markAsRead / markAsUnread](#55-duplication-markasread-markasunread)
- [6. Summary by Category](#6-summary-by-category)
- [7. Priority Roadmap](#7-priority-roadmap)

---

## 1. auth-service

### 1.1 Naming Violations

**File**: `auth-service/src/main/java/com/gotrack/auth_service/Jwt/JwtService.java`

| Issue | Problem |
|-------|---------|
| Package dir | `Jwt/` — PascalCase, should be `jwt/` |
| Class name | `JwtService.java` — OK, but inside wrong-case directory |

**File**: `auth-service/src/main/java/com/gotrack/auth_service/Exceptions/` — All exception classes are inside `Exceptions/` (uppercase), should be `exceptions/`.

**File**: `auth-service/src/main/java/com/gotrack/auth_service/Jwt/JwtKeyService.java`

Same package casing issue as above.

**Why it matters**: Java convention requires lowercase package segments. Non-conforming package names cause confusion, break IDE conventions, and violate the Java Code Conventions standard.

---

### 1.2 Constructor Injection Inconsistency

**File**: `auth-service/src/main/java/com/gotrack/auth_service/controller/AuthController.java`  
**Lines**: 31–38

**Current Code**:
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtKeyService jwtKeyService;
```

**Clean Code Suggestion**:
```java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final JwtKeyService jwtKeyService;
```

**Why it matters**: Constructor injection (via `@RequiredArgsConstructor` + `final` fields) is the Spring-recommended approach. It makes dependencies explicit, enables immutable fields, and enables testing without a container. Field injection is harder to test, hides dependencies, and can cause circular dependency issues.

---

**File**: `auth-service/src/main/java/com/gotrack/auth_service/services/AuthService.java`  
**Lines**: 26–36

**Current Code**:
```java
@Autowired
AccountRepository userRepository;

@Autowired
JwtService jwtService;

@Autowired
AuthenticationManager authenticationManager;

@Autowired
RefreshTokenService refreshTokenService;
```

**Clean Code Suggestion**:
```java
private final AccountRepository userRepository;
private final JwtService jwtService;
private final AuthenticationManager authenticationManager;
private final RefreshTokenService refreshTokenService;

public AuthService(AccountRepository userRepository,
                   JwtService jwtService,
                   AuthenticationManager authenticationManager,
                   RefreshTokenService refreshTokenService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.refreshTokenService = refreshTokenService;
}
```

**Why it matters**: Same as above — immutability and testability. The `@Autowired` field injection pattern was deprecated in Spring 4.3+ and is no longer recommended.

---

### 1.3 Security: `e.printStackTrace()`

**File**: `auth-service/src/main/java/com/gotrack/auth_service/Jwt/JwtService.java`  
**Lines**: 42–44

**Current Code**:
```java
} catch (Exception e) {
    e.printStackTrace();
    throw new RuntimeException("Token generation failed: " + e.getMessage());
}
```

**Clean Code Suggestion**:
```java
} catch (Exception e) {
    log.error("Token generation failed for user: {}", account.getEmail(), e);
    throw new RuntimeException("Token generation failed", e);
}
```

**Why it matters**: 
- `e.printStackTrace()` outputs to stderr, bypassing the logging framework. In production this means no structured logging, no log rotation, no log aggregation.
- Including `e.getMessage()` in thrown exceptions can leak internal implementation details.
- The exception should wrap the root cause so the stack trace is preserved via `new RuntimeException("msg", e)`.

---

### 1.4 God Method: AccountService — Authorization Logic Explosion

**File**: `auth-service/src/main/java/com/gotrack/auth_service/services/AccountService.java`  
**Lines**: 32–246

**Current Code** (selection of problematic patterns):

```java
// Lines 88-89 — Role check via string comparison
boolean isAdmin = auth.getAuthorities().stream()
    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
```

**Clean Code Suggestion**:
```java
private boolean hasAdminRole(Authentication auth) {
    return auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
}
```

---

```java
// Lines 91-92 — Magic string stripping
String cleanId = targetId.replaceAll("[{}]", "").trim();
System.out.println("Searching for ID: [" + targetId + "] with length: " + targetId.length());
```

**Clean Code Suggestion**:
```java
private String sanitizeId(String rawId) {
    return rawId.replaceAll("[{}]", "").trim();
}

// Replace System.out.println with:
log.info("Resolving account ID: {} (length: {})", rawId, rawId.length());
```

---

```java
// Lines 108-142 — updateAccount method: deeply nested, mixed concerns
public UpdateDeleteResponse updateAccount(String targetId, UpdateAccountRequest request) {
    // ... 35+ lines of authorization, validation, update logic all inline
}
```

**Clean Code Suggestion** — Extract into focused private methods:
```java
private void validateUpdateRequest(UpdateAccountRequest request) {
    if (request.getEmail() == null && request.getPassword() == null && request.getRole() == null) {
        throw new BadCredentialsException("No fields to update");
    }
}

private void validateRoleChangeAuthorization(Account currentUser, Account targetUser, Role newRole) {
    if (currentUser.getId().equals(targetUser.getId()) && newRole != null) {
        throw new AccessDeniedException("You cannot change your own role.");
    }
    if (currentUser.getRole() == Role.ADMIN && !currentUser.getId().equals(targetUser.getId())) {
        throw new AccessDeniedException("Only Admin can modify other accounts.");
    }
}

private void requireReLogin(Account user, UpdateAccountRequest request) {
    if (request.getPassword() != null || request.getEmail() != null || request.getRole() != null) {
        refreshTokenService.deleteByUsername(user.getEmail());
    }
}
```

**Why it matters**: 
- **Violation of Single Responsibility Principle**: One method handles authz checks, validation, business logic, and response formatting.
- **Violation of DRY**: The role-checking logic `"ROLE_ADMIN".equals(...)` appears 3+ times.
- **Violation of Clean Code "Small Methods" rule**: `updateAccount()` is ~35 lines doing 5+ different things.
- **System.out.println** in production code should be logger.info/error.

---

### 1.5 Magic Numbers

**File**: `auth-service/src/main/java/com/gotrack/auth_service/Jwt/JwtService.java`  
**Line**: 20

**Current Code**:
```java
long EXPIRATION = 60 * 60 * 1000;
```

**Clean Code Suggestion**:
```java
// In application.yml:
// jwt:
//   access-token-expiration-ms: 3600000
// Then in JwtService:
private final long accessTokenExpirationMs;

public JwtService(JwtKeyService jwtKeyService, 
                  @Value("${jwt.access-token-expiration-ms:3600000}") long accessTokenExpirationMs) {
    this.jwtKeyService = jwtKeyService;
    this.accessTokenExpirationMs = accessTokenExpirationMs;
}
```

**Why it matters**: Magic numbers violate the DRY principle and make the code harder to configure across environments (dev uses different expiration than prod).

---

### 1.6 Boolean Primitive vs Boolean Wrapper

**File**: `auth-service/src/main/java/com/gotrack/auth_service/entity/Account.java`  
**Lines**: 41–49, 62

**Current Code**:
```java
@Field("superAdmin")
private Boolean superAdmin = false;

public Boolean getSuperAdmin() {
    return superAdmin;
}

public void setSuperAdmin(Boolean superAdmin) {
    this.superAdmin = superAdmin;
}
```

**Clean Code Suggestion**:
```java
@Field("superAdmin")
private boolean superAdmin = false;
```

**Why it matters**: Lombok's `@Getter`/`@Setter` already generates the accessors — the manual methods are redundant. Use `boolean` (primitive) instead of `Boolean` (wrapper) since the field always has a default value. Redundant Lombok-generated methods clutter the class and confuse readers.

---

**File**: `auth-service/src/main/java/com/gotrack/auth_service/services/AccountService.java`  
**Line**: 62

**Current Code**:
```java
if (currentUser.getSuperAdmin() == false) {
```

**Clean Code Suggestion**:
```java
if (!currentUser.getSuperAdmin()) {
```

**Why it matters**: `!value` is more idiomatic Java and more readable.

---

## 2. inventory-service

### 2.1 Magic Strings for Role Checks

**File**: `inventory-service/src/main/java/com/gotrack/inventory_service/Service/ProductService.java`  
**Lines**: 35, 39, 50

**Current Code**:
```java
String role = authDetails.getRole();

if ("MERCHANT".equals(role) && !userProfile.getId().equals(productDto.getMerchantId())) {
    throw new ForbiddenException("You can only create your own products");
}

if (merchantProfile == null || !"MERCHANT".equals(merchantProfile.getType().toString())) {
```

**Clean Code Suggestion**:
```java
if (Role.MERCHANT.name().equals(authDetails.getRole()) && ...)

// Better: Extract an enum or constant
private static final String ROLE_MERCHANT = "MERCHANT";
if (ROLE_MERCHANT.equals(authDetails.getRole()) && ...)

// Best: Create a typed Role enum in inventory-service and compare directly
if (authDetails.getRoleEnum() == RoleType.MERCHANT && ...)
```

**Why it matters**: 
- `"MERCHANT"` is a hardcoded string that can be typo'd and isn't catchable by the compiler.
- `"MERCHANT".equals(variable)` is the null-safe pattern, but the string itself should be a constant or enum.
- Violates DRY — the same string appears in 5+ files.

---

### 2.2 AuthenticationDetails Instantiated with `new`

**File**: `inventory-service/src/main/java/com/gotrack/inventory_service/Service/ProductService.java`  
**Lines**: 34, 67

**Current Code**:
```java
public ProductResponseDTO createProduct(ProductDTO productDto) {
    AuthenticationDetails authDetails = new AuthenticationDetails();
    String role = authDetails.getRole();
    ...
}

public void updateProduct(UpdateProductDTO dto) {
    AuthenticationDetails authDetails = new AuthenticationDetails();
    String role = authDetails.getRole();
    Long merchantId = userServices.getProfileByAccountId(authDetails.getAccountId()).getId();
```

**Clean Code Suggestion**:
```java
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserServices userServices;
    private final AuthenticationDetailsProvider authDetailsProvider; // Spring-managed bean
```

```java
// AuthenticationDetailsProvider.java — Spring-managed singleton
@Component
public class AuthenticationDetailsProvider {
    public AuthenticationDetails getCurrent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Extract role, accountId, email from auth
    }
}
```

**Why it matters**: `new AuthenticationDetails()` means this class cannot be tested in isolation. It directly accesses `SecurityContextHolder`, creating a hidden coupling to Spring Security's thread-local state. Making it a Spring bean with proper constructor injection enables dependency injection and testing.

This pattern is repeated across:
- `ProductService.java` (lines 34, 67)
- `InventoryService.java` (lines 100, 106)
- `ProductController.java` (line 93)
- Same pattern in `user-branch-service` and `core-logistic-finance`

---

### 2.3 Duplication: Authorization Logic Repeated Across Services

**File**: `inventory-service/src/main/java/com/gotrack/inventory_service/Service/InventoryService.java`  
**Lines**: 100–109

**Current Code**:
```java
AuthenticationDetails authDetails = new AuthenticationDetails();
if ("MERCHANT".equals(authDetails.getRole())) {
    Long merchantId = userServices.getProfileByAccountId(authDetails.getAccountId()).getId();
    if (merchantId == null) {
        throw new NotFoundException("You do not have a merchant profile");
    }
    if (filter.getMerchantId() != null && !filter.getMerchantId().equals(merchantId)) {
        throw new ConflictException("You can only filter by your own merchant ID");
    }
}
```

**Clean Code Suggestion** — Extract a reusable authorization helper:
```java
public class RoleBasedAuthChecker {
    private final UserServices userServices;

    public RoleBasedAuthChecker(UserServices userServices) {
        this.userServices = userServices;
    }

    public void requireMerchantProfile(String accountId) {
        ProfileResponseDTO profile = userServices.getProfileByAccountId(accountId);
        if (profile == null || !"MERCHANT".equals(profile.getType().toString())) {
            throw new ForbiddenException("You do not have a merchant profile");
        }
    }

    public void enforceOwnMerchantId(Long filterMerchantId, String accountId) {
        ProfileResponseDTO profile = userServices.getProfileByAccountId(accountId);
        if (filterMerchantId != null && !filterMerchantId.equals(profile.getId())) {
            throw new ConflictException("You can only filter by your own merchant ID");
        }
    }
}
```

**Why it matters**: This authorization pattern repeats across `ProductService`, `InventoryService`, `ProductController`. Extracting it into a shared component reduces duplication and centralizes the logic for future changes.

---

### 2.4 Naming Violations

| File | Issue | Should Be |
|------|-------|-----------|
| `Dto/PickupResponceDTO.java` | `Responce` (typo) | `PickupResponseDTO` |
| `Exception/GlobalalExceptionHandler.java` | `Globalal` (typo) | `GlobalExceptionHandler` |
| `Exception/GlobalalFeignExceptionHandler.java` | `Globalal` (typo) | `GlobalFeignExceptionHandler` |
| `Dto/inventoryFilter.java` | lowercase `i` | `InventoryFilter` |
| `Service/BranchServices.java` | plural `Services` | `BranchService` (single) |
| `Service/UserServices.java` | plural `Services` | `UserService` (single) |
| Package: `Entity/`, `Enums/`, `Dto/`, `Exception/` | Uppercase dirs | `entity/`, `enums/`, `dto/`, `exception/` |

**Why it matters**: Typos in class names (`Responce`, `Globalal`) are not caught by the compiler and create confusion. Inconsistent casing violates Java naming conventions and makes the codebase feel unprofessional.

---

### 2.5 Swallowed Exceptions

**File**: `inventory-service/src/main/java/com/gotrack/inventory_service/Service/UserServices.java`  
**Lines**: 14–20

**Current Code**:
```java
public ProfileResponseDTO getProfileByAccountId(String accountId) {
    try {
        return userClient.getProfileByAccountId(accountId);
    } catch (Exception e) {
        return null;
    }
}
```

**Clean Code Suggestion**:
```java
public ProfileResponseDTO getProfileByAccountId(String accountId) {
    try {
        return userClient.getProfileByAccountId(accountId);
    } catch (FeignException e) {
        log.warn("User profile not found for accountId: {}", accountId);
        return null;
    }
}
```

**Why it matters**: 
- Swallowing **all** `Exception` types hides real errors (e.g., network failures, serialization errors).
- Only catching `FeignException` (or specific subclasses) allows real failures to surface.
- The caller cannot distinguish between "user doesn't exist" and "service is down".

---

### 2.6 Spellings / Typos in Controller

**File**: `inventory-service/src/main/java/com/gotrack/inventory_service/controller/ProductController.java`  
**Line**: 68

**Current Code**:
```java
@GetMapping("/merchent/{id}")
```

**Clean Code Suggestion**:
```java
@GetMapping("/merchant/{id}")
```

**Why it matters**: A typo in the API path causes clients to get 404 errors. API paths should be spelled correctly and tested.

---

### 2.7 Method: `getProducts` Returns Page but Throws for Empty

**File**: `inventory-service/src/main/java/com/gotrack/inventory_service/Service/ProductService.java`  
**Lines**: 87–98

**Current Code**:
```java
public Page<ProductResponseDTO> getProducts(Long merchantId, Pageable pageable) {
    Page<Product> products = productRepository.findByMerchantId(merchantId, pageable)
        .orElseThrow(() -> new NotFoundException(
            "Product with Merchant ID " + merchantId + " not found"));
    ...
}
```

**Issue**: The repository method returns `Page<Product>`, not `Optional<Page<Product>>`. Using `.orElseThrow()` on a `Page` is wrong — `Page` is not an `Optional`.

**Clean Code Suggestion**:
```java
public Page<ProductResponseDTO> getProducts(Long merchantId, Pageable pageable) {
    Page<Product> products = productRepository.findByMerchantId(merchantId, pageable);
    if (products.isEmpty()) {
        throw new NotFoundException("No products found for Merchant ID " + merchantId);
    }
    return products.map(product -> ProductResponseDTO.builder()
        .id(product.getId())
        .name(product.getName())
        .merchantId(product.getMerchantId())
        .baseSku(product.getBaseSku())
        .build());
}
```

**Why it matters**: The current code has a type mismatch that would cause a compilation error in a correctly configured codebase. Either the repository returns `Optional<Page<Product>>` or the `.orElseThrow()` shouldn't be used on a `Page`.

---

## 3. user-branch-service

### 3.1 Magic Strings for Role Checks

**File**: `user-branch-service/src/main/java/com/gotrack/user_branch_service/service/imp/ProfileServiceImp.java`  
**Lines**: 91, 117, 119, 120

**Current Code**:
```java
// Line 91
if (authDetails.getRole().equals("MERCHANT")) {

// Lines 117-120
if (List.of("ADMIN", "EMPLOYEE").contains(entity.getType().toString()) && !isSuperAdmin) {
    throw new ForbiddenException("You are not authorized to update this profile");
} else if (List.of("MERCHANT", "COURIER", "MERCHANT")
        .contains(entity.getType().toString()) &&
        !List.of("ADMIN", "EMPLOYEE").contains(authDetails.getRole())) {
    throw new ForbiddenException("You are not authorized to update this profile");
}
```

**Clean Code Suggestion**:
```java
// Extract typed role-checking methods
private boolean isMerchant(AuthenticationDetails auth) {
    return auth.getRole().equals(RoleType.MERCHANT.name());
}

private boolean requiresSuperAdminUpdate(ProfileType type) {
    return type == ProfileType.ADMIN || type == ProfileType.EMPLOYEE;
}

private boolean canUpdateProfile(ProfileType profileType, AuthenticationDetails auth) {
    if (requiresSuperAdminUpdate(profileType)) {
        return accountService.isSuperAdmin(auth.getAccountId());
    }
    return List.of("ADMIN", "EMPLOYEE").contains(auth.getRole());
}
```

**Why it matters**: 
- `"MERCHANT"`, `"ADMIN"`, `"EMPLOYEE"` appear as raw strings in 5+ places across services.
- Changing a role name requires a find-replace across the entire codebase.
- Using `ProfileType` enum comparisons instead of string comparisons makes refactoring safe.

---

### 3.2 Duplicate Authorization Logic

**File**: `user-branch-service/src/main/java/com/gotrack/user_branch_service/service/imp/ProfileServiceImp.java`  
**Lines**: 112–122

**Current Code**:
```java
if (entity.getAccountId() != null && 
    !entity.getAccountId().equals(authDetails.getAccountId())) {
    Boolean isSuperAdmin = accountService.isSuperAdmin(authDetails.getAccountId());
    if (List.of("ADMIN", "EMPLOYEE").contains(entity.getType().toString()) && !isSuperAdmin) {
        throw new ForbiddenException("You are not authorized to update this profile");
    } else if (List.of("MERCHANT", "COURIER", "MERCHANT")
            .contains(entity.getType().toString()) &&
            !List.of("ADMIN", "EMPLOYEE").contains(authDetails.getRole())) {
        throw new ForbiddenException("You are not authorized to update this profile");
    }
}
```

**Clean Code Suggestion**:
```java
private void assertUpdatePermission(ProfileEntity profile, AuthenticationDetails auth) {
    String currentRole = auth.getRole();
    ProfileType profileType = profile.getType();

    if (profileType == ProfileType.ADMIN || profileType == ProfileType.EMPLOYEE) {
        if (!accountService.isSuperAdmin(auth.getAccountId())) {
            throw new ForbiddenException("Only Super Admin can modify this profile");
        }
    } else {
        if (!List.of("ADMIN", "EMPLOYEE").contains(currentRole)) {
            throw new ForbiddenException("Only Admin or Employee can modify this profile");
        }
    }
}
```

**Why it matters**: This authorization block is duplicated in the `updateProfile` method and should be extracted. It also has a bug: `"MERCHANT"` appears twice in the `List.of()` call.

---

### 3.3 AuthenticationDetails with `new`

**File**: `user-branch-service/src/main/java/com/gotrack/user_branch_service/service/imp/ProfileServiceImp.java`  
**Lines**: 71, 89, 111

**Current Code**:
```java
AuthenticationDetails authDetails = new AuthenticationDetails();
```

**Clean Code Suggestion**: See Section 2.2 — inject as a Spring bean via constructor injection.

---

### 3.4 Constructor Injection Inconsistency

**File**: `user-branch-service/src/main/java/com/gotrack/user_branch_service/controller/BranchController.java`

```java
@Autowired
private BranchService branchService;  // Field injection
```

**Clean Code Suggestion**: Use `@RequiredArgsConstructor` + `final` fields (same pattern as Section 1.2).

---

### 3.5 Violated DRY: Self-Referencing List

**File**: `user-branch-service/src/main/java/com/gotrack/user_branch_service/service/imp/ProfileServiceImp.java`  
**Line**: 119

**Current Code**:
```java
List.of("MERCHANT", "COURIER", "MERCHANT")
```

**Clean Code Suggestion**:
```java
List.of("MERCHANT", "COURIER")  // MERCHANT listed twice — clearly a copy-paste error
```

**Why it matters**: `"MERCHANT"` appears twice, suggesting a copy-paste error. This is a bug that could mask authorization logic.

---

## 4. core-logistic-finance

### 4.1 Naming Violations (Massive)

This service has the most severe naming violations. Nearly every identifier violates Java conventions.

| File | Current | Should Be | Rule Violated |
|------|---------|-----------|---------------|
| `mapper/shipmentMapper.java` | Class name `shipmentMapper` | `ShipmentMapper` | PascalCase for classes |
| `mapper/shipmentMapper.java` | Line 12: `shipmentMapper` | `ShipmentMapper` | PascalCase |
| `mapper/shipmentMapper.java` | Line 36: `ShipmentMapper` | `shipmentMapper` | camelCase for method/param |
| `model/dto/TransactionDTO.java` | `CreatedAt` | `createdAt` | camelCase for fields |
| `model/dto/TransactionDTO.java` | `TransacteTo` | `transactionTo` | Typo + camelCase |
| `model/dto/TransactionDTO.java` | `TransacteFrom` | `transactionFrom` | Typo + camelCase |
| `model/entity/Transaction.java` | `CreatedAt` | `createdAt` | camelCase |
| `model/entity/Transaction.java` | `TransacteTo` | `transactionTo` | Typo in column name |
| `model/entity/Transaction.java` | `TransacteFrom` | `transactionFrom` | Typo in column name |
| `Service/finance/FinanceService.java` | Line 26: `financeRepo` | `financeRepository` | camelCase |
| `Service/ShipmentService.java` | Line 36: `ShipmentMapper` | `shipmentMapper` | camelCase for field |
| `Service/finance/WalletService.java` | Line 36: `GetMyWallets` | `getMyWallets` | camelCase for method |
| `model/dto/TransactionDTO.java` | Line 34: `TransacteTo` | `transactionTo` | Typo |
| `model/dto/TransactionDTO.java` | Line 37: `TransacteFrom` | `transactionFrom` | Typo |
| `model/entity/FinancialSummary.java` | `CurierCommission` | `courierCommission` | Typo |
| `model/entity/FinancialSummary.java` | `VenderPaid` | `vendorPaid` | Typo |
| `model/entity/FinancialSummary.java` | `VendorDue` | `vendorDue` | Spelling |
| `enums/TransactionCatg.java` | `TransactionCatg` | `TransactionCategory` | Typo |
| `model/dto/PickupResponceDTO.java` | `PickupResponceDTO` | `PickupResponseDTO` | Typo |
| `model/entity/Shipment.java` | `MERCHANTId` | `merchantId` | camelCase |
| `model/dto/PickupFilter.java` | `MERCHANTId` | `merchantId` | camelCase |
| `model/dto/PickupFilter.java` | `getMERCHANTId` | `getMerchantId` | camelCase |
| `Service/PickupService.java` | `filter.getMERCHANTId()` | `filter.getMerchantId()` | camelCase |
| `Service/PickupService.java` | `profile.getType().toString() == "MERCHANT"` | `profile.getType() == "MERCHANT"` | string comparison |

**Clean Code Suggestion** — Create a naming conventions checklist:

```
All Java identifiers follow standard conventions:
- Classes/Interfaces: PascalCase (e.g., ShipmentMapper)
- Fields/Methods/LocalVars: camelCase (e.g., merchantId)
- Constants: UPPER_SNAKE_CASE (e.g., MAX_PAGE_SIZE)
- Packages: lowercase (e.g., com.gotrack.core_logistic.mapper)
- Fix all typos: "Responce" → "Response", "Curier" → "Courier", 
  "Vender" → "Vendor", "Catg" → "Category"
- Fix column name typos: "TransacteTo" → "transaction_to", 
  "TransacteFrom" → "transaction_from"
```

**Why it matters**: 20+ naming violations across the finance service indicate no naming standards are enforced. Typos like `TransacteTo` vs `transaction_to` in the database column create permanent schema issues that cannot be renamed without migration. `GetMyWallets` (PascalCase method) is a Java convention violation that makes the code look unprofessional.

---

### 4.2 Magic Numbers in FinanceService

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/finance/FinanceService.java`  
**Lines**: 39-48

**Current Code**:
```java
newSummary.setNetCash(BigDecimal.ZERO);
newSummary.setExpenses(BigDecimal.ZERO);
newSummary.setNetProfit(BigDecimal.ZERO);
newSummary.setProfitMargin(BigDecimal.ZERO);
newSummary.setCurierCommission(BigDecimal.ZERO);
newSummary.setShippingCost(BigDecimal.ZERO);
newSummary.setVendorDue(BigDecimal.ZERO);
newSummary.setVenderPaid(BigDecimal.ZERO);
newSummary.setTotalCommission(BigDecimal.ZERO);
```

**Clean Code Suggestion**:
```java
private FinancialSummary createEmptySummary(LocalDate date) {
    FinancialSummary summary = new FinancialSummary();
    summary.setCreatedAt(date);
    // Use a constant or builder for clarity
    return FinancialSummary.empty(date);
}

// Or use a builder pattern:
FinancialSummary newSummary = FinancialSummary.builder()
    .createdAt(today)
    .netCash(BigDecimal.ZERO)
    .expenses(BigDecimal.ZERO)
    .netProfit(BigDecimal.ZERO)
    .profitMargin(BigDecimal.ZERO)
    .courierCommission(BigDecimal.ZERO)
    .shippingCost(BigDecimal.ZERO)
    .vendorDue(BigDecimal.ZERO)
    .vendorPaid(BigDecimal.ZERO)
    .totalCommission(BigDecimal.ZERO)
    .build();
```

**Why it matters**: Nine repetitive setter calls on the same object. A builder or factory method would make this intent clear and be easier to modify.

---

### 4.3 Method Duplication / Redundant Calculations

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/finance/FinanceService.java`  
**Lines**: 98-99 (inside `calculate`)  
**Lines**: 116-118 (inside `shipmentCalculation`)

**Current Code**:
```java
// Inside calculate() — lines 98-99
financialSummary.setNetProfit(financialSummary.getShippingCost().subtract(financialSummary.getExpenses()));
financialSummary.setProfitMargin(financialSummary.getShippingCost().subtract(financialSummary.getCurierCommission()));

// Inside shipmentCalculation() — lines 116-118
financialSummary.setNetProfit(financialSummary.getShippingCost().subtract(financialSummary.getExpenses()));
financialSummary.setProfitMargin(financialSummary.getShippingCost().subtract(financialSummary.getCurierCommission()));
```

**Clean Code Suggestion**:
```java
private void recalculateDerivedFields(FinancialSummary summary) {
    summary.setNetProfit(summary.getShippingCost().subtract(summary.getExpenses()));
    summary.setProfitMargin(summary.getShippingCost().subtract(summary.getCourierCommission()));
}
```

**Why it matters**: 
- **DRY Violation**: The exact same two lines are duplicated in 3+ methods.
- **Maintenance hazard**: If the calculation formula changes, it must be updated in 3+ places.
- If you change one but not the other, the summary becomes inconsistent.

---

### 4.4 Constructor Injection Inconsistency

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/ShipmentService.java`  
**Lines**: 31–40

**Current Code**:
```java
@Autowired
    private ShipmentRepo shipmentRepo;
@Autowired
    private PickupRepo pickupRepo;
@Autowired
    private shipmentMapper ShipmentMapper;
@Autowired
    private FinanceService financeService;
@Autowired
    private ProfileBranchService profileBranchService;
```

**Clean Code Suggestion**:
```java
private final ShipmentRepo shipmentRepo;
private final PickupRepo pickupRepo;
private final ShipmentMapper shipmentMapper;
private final FinanceService financeService;
private final ProfileBranchService profileBranchService;

public ShipmentService(ShipmentRepo shipmentRepo,
                       PickupRepo pickupRepo,
                       ShipmentMapper shipmentMapper,
                       FinanceService financeService,
                       ProfileBranchService profileBranchService) {
    this.shipmentRepo = shipmentRepo;
    this.pickupRepo = pickupRepo;
    this.shipmentMapper = shipmentMapper;
    this.financeService = financeService;
    this.profileBranchService = profileBranchService;
}
```

**Why it matters**: All services have this same inconsistency. Constructor injection is the Spring-recommended standard.

---

### 4.5 Magic String Comparisons

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/ShipmentService.java`  
**Lines**: 54-55

**Current Code**:
```java
ProfileResponse profile = profileBranchService.getProfileById(shipmentRequest.getCourierId());
if(profile.getType().toString() != "COURIER")
    throw new ConflictException("This is not a courier profile");
```

**Clean Code Suggestion**:
```java
ProfileResponse profile = profileBranchService.getProfileById(shipmentRequest.getCourierId());
if (!"COURIER".equals(profile.getType().toString())) {
    throw new ConflictException("Profile must be of type COURIER");
}
// Better: use enum comparison directly
if (profile.getType() != ProfileType.COURIER) {
    throw new ConflictException("Profile must be of type COURIER");
}
```

---

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/PickupService.java`  
**Lines**: 75, 93, 97

**Current Code**:
```java
if (profile.getType().toString() != "COURIER")
    throw new ConflictException("This is not a courier profile");

if (filter.getMERCHANTId() != null && filter.getMERCHANTId() != profile.getId()
    && profile.getType().toString() != "MERCHANT") {

if (profile.getType().toString() == "MERCHANT")
    filter.setMERCHANTId(profile.getId());
```

**Clean Code Suggestion**:
```java
private boolean isCourier(ProfileResponse profile) {
    return profile.getType() == ProfileType.COURIER;
}

private boolean isMerchant(ProfileResponse profile) {
    return profile.getType() == ProfileType.MERCHANT;
}
```

**Why it matters**: 
- `!=` and `==` for string comparison is a **classic Java bug**. If `getType()` returns null, `!=` doesn't throw NPE but `toString() !=` can mask bugs.
- The string `"COURIER"` or `"MERCHANT"` should be an enum constant.
- Using `.equals()` or direct enum comparison eliminates this entire class of bugs.

---

### 4.6 Boolean Primitive Comparison with `==`

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/ShipmentService.java`  
**Line**: 48

**Current Code**:
```java
if(pickup.getStatus() != PickupStatus.Pending)
```

**Clean Code Suggestion**:
```java
if (pickup.getStatus() != PickupStatus.PENDING)
```

**Why it matters**: While `!=` is correct for enum comparison, the enum constant should be `PENDING` (uppercase), not `Pending`. Consistency with Java enum naming conventions matters.

---

### 4.7 Method: `buildSummary` — Sequential Getters

**File**: `core-logistic-finance/src/main/java/com/gotrack/core_logistic/Service/finance/FinanceService.java`  
**Lines**: 140-157

**Current Code**:
```java
dto.setNetCash(summaries.stream().map(FinancialSummary::getNetCash).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setVendorDue(summaries.stream().map(FinancialSummary::getVendorDue).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setVenderPaid(summaries.stream().map(FinancialSummary::getVenderPaid).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setShippingCost(summaries.stream().map(FinancialSummary::getShippingCost).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setCurierCommission(summaries.stream().map(FinancialSummary::getCurierCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setTotalCommission(summaries.stream().map(FinancialSummary::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setExpenses(summaries.stream().map(FinancialSummary::getExpenses).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setProfitMargin(summaries.stream().map(FinancialSummary::getProfitMargin).reduce(BigDecimal.ZERO, BigDecimal::add));
dto.setNetProfit(summaries.stream().map(FinancialSummary::getNetProfit).reduce(BigDecimal.ZERO, BigDecimal::add));
```

**Clean Code Suggestion**:
```java
// Extract a helper method
private FinancialSummaryDTO aggregateSummaries(List<FinancialSummary> summaries) {
    return FinancialSummaryDTO.builder()
        .netCash(summaries.stream().map(FinancialSummary::getNetCash).reduce(BigDecimal.ZERO, BigDecimal::add))
        .vendorDue(summaries.stream().map(FinancialSummary::getVendorDue).reduce(BigDecimal.ZERO, BigDecimal::add))
        .vendorPaid(summaries.stream().map(FinancialSummary::getVendorPaid).reduce(BigDecimal.ZERO, BigDecimal::add))
        .shippingCost(summaries.stream().map(FinancialSummary::getShippingCost).reduce(BigDecimal.ZERO, BigDecimal::add))
        .courierCommission(summaries.stream().map(FinancialSummary::getCourierCommission).reduce(BigDecimal.ZERO, BigDecimal::add))
        .totalCommission(summaries.stream().map(FinancialSummary::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add))
        .expenses(summaries.stream().map(FinancialSummary::getExpenses).reduce(BigDecimal.ZERO, BigDecimal::add))
        .profitMargin(summaries.stream().map(FinancialSummary::getProfitMargin).reduce(BigDecimal.ZERO, BigDecimal::add))
        .netProfit(summaries.stream().map(FinancialSummary::getNetProfit).reduce(BigDecimal.ZERO, BigDecimal::add))
        .createdAt(LocalDate.now())
        .build();
}
```

**Why it matters**: 
- 9 nearly identical lines violate DRY.
- Each line has a `summaries.stream()` call — creating 9 streams for what could be a single-pass aggregation.
- A single-pass `reduce` would be more efficient:
```java
FinancialSummaryDTO dto = summaries.stream()
    .reduce(FinancialSummaryDTO.builder().createdAt(LocalDate.now()).build(),
        (aggregate, summary) -> FinancialSummaryDTO.builder()
            .netCash(aggregate.getNetCash().add(summary.getNetCash()))
            // ... etc
            .build(),
        (a, b) -> { /* merge */ });
```

---

## 5. support-and-notifications-service

### 5.1 System.out.println in Production Code

**File**: `support_and_notifications_service/src/main/java/com/gotrack/support_and_notifications_service/complaint/service/ComplaintService.java`  
**Lines**: 45-46

**Current Code**:
```java
System.out.println("Creating complaint with: " + request.getContent() + ", " + request.getSubject() + ", "
        + request.getShipmentId() + ", " + request.getChannel());
```

**Clean Code Suggestion**:
```java
log.info("Creating complaint: content='{}', subject='{}', shipmentId={}, channel={}",
    request.getContent(), request.getSubject(), request.getShipmentId(), request.getChannel());
```

**Why it matters**: `System.out.println` doesn't go through the logging framework. In production:
- No log aggregation (ELK, CloudWatch)
- No log levels (INFO vs DEBUG)
- No structured format
- Performance overhead (unsynchronized)

---

### 5.2 Constructor Injection Inconsistency

**File**: `support_and_notifications_service/src/main/java/com/gotrack/support_and_notifications_service/complaint/service/ComplaintService.java`  
**Lines**: 29–40

**Current Code**:
```java
@Autowired
private ComplaintRepo repo;
@Autowired
private ComplaintMapper mapper;
@Autowired
private CurrentUserService user;
@Autowired
private CheckShipment shipment;
@Autowired
private WEventPublisher eventPublisher;
@Autowired
private CheckStatusTransition checkTransition;
```

**Clean Code Suggestion**: Use `@RequiredArgsConstructor` + `private final` fields.

---

**File**: `support_and_notifications_service/src/main/java/com/gotrack/support_and_notifications_service/complaint/controller/ComplaintController.java`

```java
@Autowired
private ComplaintService service;  // Field injection
```

**Clean Code Suggestion**: Constructor injection via `@RequiredArgsConstructor`.

---

### 5.3 Service That Is Just a Pass-through Wrapper

**File**: `support_and_notifications_service/src/main/java/com/gotrack/support_and_notifications_service/user/CheckUser.java`  
**Lines**: 14-19

**Current Code**:
```java
@Service
public class CheckUser {
    @Autowired
    UserRepo repo;

    public void checkUserExists(String profileId) {
        if (!repo.existsById(profileId)) {
            throw new ResourceNotFoundException("User with ID " + profileId + " does not exist.");
        }
    }

    public String getUserEmailById(String profileId) {
        checkUserExists(profileId);
        return repo.getEmailByProfileId(profileId);
    }
}
```

**Clean Code Suggestion**:
```java
// Either rename to UserValidator and make it a @Component (not @Service)
@Component
class UserValidator {
    private final UserRepo userRepo;

    public UserValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void assertExists(String profileId) {
        if (!userRepo.existsById(profileId)) {
            throw new ResourceNotFoundException("User with ID " + profileId + " does not exist.");
        }
    }
}
```

**Why it matters**: 
- `CheckUser` is named as a verb (should be a noun: `UserValidator` or `UserChecker`).
- It's annotated `@Service` but has no business logic — it's a validation helper.
- `@Service` implies business logic; `@Component` or no annotation is more appropriate.
- `getUserEmailById` does two things: checks existence and retrieves email. Should be split.

---

### 5.4 Security: Access Denied Not Checked Properly

**File**: `support_and_notifications_service/src/main/java/com/gotrack/support_and_notifications_service/notification/service/NotificationService.java`  
**Lines**: 96-106

**Current Code**:
```java
public void markAsRead(String notificationId, String profileId) {
    checkUser.checkUserExists(profileId);
    Notifications noti = repo.findById(notificationId)
        .orElseThrow(() -> new ResourceNotFoundException("Notification " + notificationId + " not found"));
    if (!noti.getProfileId().equals(profileId)) {
        throw new AccessDeniedException("Not allowed");
    }
    noti.setRead(true);
    repo.save(noti);
}
```

**Clean Code Suggestion**:
```java
public void markAsRead(String notificationId) {
    String currentProfileId = currentUserService.getCurrentUserId();
    Notifications noti = repo.findById(notificationId)
        .orElseThrow(() -> new ResourceNotFoundException("Notification " + notificationId + " not found"));
    
    assertOwner(noti.getProfileId(), currentProfileId);
    
    noti.setRead(true);
    repo.save(noti);
}

private void assertOwner(String ownerId, String currentProfileId) {
    if (!ownerId.equals(currentProfileId)) {
        throw new AccessDeniedException("You can only modify your own notifications");
    }
}
```

**Why it matters**: The method signature takes `profileId` as a parameter, meaning the caller could pass any profile ID. The ownership check inside is a defensive measure — the parameter shouldn't be needed if the current user context is used properly.

---

### 5.5 Duplication: markAsRead / markAsUnread

**File**: `support_and_notifications_service/src/main/java/com/gotrack/support_and_notifications_service/notification/service/NotificationService.java`  
**Lines**: 96-118

**Current Code**:
```java
public void markAsRead(String notificationId, String profileId) {
    checkUser.checkUserExists(profileId);
    Notifications noti = repo.findById(notificationId)
        .orElseThrow(() -> new ResourceNotFoundException("Notification " + notificationId + " not found"));
    if (!noti.getProfileId().equals(profileId)) {
        throw new AccessDeniedException("Not allowed");
    }
    noti.setRead(true);
    repo.save(noti);
}

public void markAsUnread(String notificationId, String profileId) {
    checkUser.checkUserExists(profileId);
    Notifications noti = repo.findById(notificationId)
        .orElseThrow(() -> new ResourceNotFoundException("Notification " + notificationId + " not found"));
    if (!noti.getProfileId().equals(profileId)) {
        throw new AccessDeniedException("Not allowed");
    }
    noti.setRead(false);
    repo.save(noti);
}
```

**Clean Code Suggestion**:
```java
public void updateReadStatus(String notificationId, boolean isRead) {
    String currentProfileId = currentUserService.getCurrentUserId();
    Notifications noti = repo.findById(notificationId)
        .orElseThrow(() -> new ResourceNotFoundException("Notification " + notificationId + " not found"));
    
    if (!noti.getProfileId().equals(currentProfileId)) {
        throw new AccessDeniedException("You can only modify your own notifications");
    }
    
    noti.setRead(isRead);
    repo.save(noti);
}
```

**Why it matters**: The two methods are 95% identical — only `setRead(true)` vs `setRead(false)` differs. This violates DRY. A single method with a boolean parameter eliminates the duplication.

---

## 6. Summary by Category

| Category | Count | Examples |
|----------|-------|----------|
| **Naming Violations** | 14 | `shipmentMapper` (lowercase), `Responce` (typo), `TransacteTo` (typo), `MERCHANTId` (wrong case), `GetMyWallets` (wrong case) |
| **Inconsistent Constructor Injection** | 9 | `@Autowired` field injection in AuthController, AuthService, ProductController, AccountServiceImp, FinanceService, ShipmentService, WalletService, NotificationService, ComplaintController |
| **Magic Strings / Hardcoded Values** | 8 | `"MERCHANT"`, `"ADMIN"`, `"COURIER"`, `"Completed"`, `60 * 60 * 1000`, `"ROLE_ADMIN"` |
| **God Methods / Deep Nesting** | 7 | `AccountService.updateAccount()` (35 lines), `ProfileServiceImp.updateProfile()` (55 lines), `FinanceService.calculate()` (52 lines) |
| **DRY Violations (Duplication)** | 6 | `markAsRead/markasUnread`, `calculate/shipmentCalculation` redundant formulas, auth checks repeated across services, `filter.getMERCHANTId()` repeated |
| **Error Handling Issues** | 5 | `e.printStackTrace()`, swallowed exceptions, `RuntimeException` for business errors, missing exception wrapping |
| **Method Size / Complexity** | 4 | `FinanceService.buildSummary` (22 lines, 9 identical streams), `AccountService.updateAccount` (35+ lines, 6 concerns) |
| **Security Concerns** | 3 | `AuthenticationDetails` with `new` (bypasses DI), `System.out.println` in filter (debug leak), role checks via strings (not type-safe) |
| **Code Smells / Anti-patterns** | 2 | `CheckUser` annotated as `@Service` but is a validator, `PickupService` in both inventory and core-logistic (naming confusion) |
| **Missing Validation / Guards** | 1 | `getProducts` method type mismatch (Page vs Optional<Page>) |

---

## 7. Priority Roadmap

### 🔴 Priority 1: Critical (Fix Immediately)

| # | Issue | Impact | Effort |
|---|-------|--------|--------|
| 1 | Replace `new AuthenticationDetails()` with injected bean | Security, testability | 2 hours |
| 2 | Replace `e.printStackTrace()` with `log.error()` | Security, observability | 30 min |
| 3 | Replace `System.out.println` with `log.info()` (3 locations) | Observability | 30 min |
| 4 | Fix all `==` / `!=` string comparisons to `.equals()` or enum | Correctness | 1 hour |
| 5 | Fix typo in API path: `/merchent/` → `/merchant/` | Breaking change | 15 min |

### 🟡 Priority 2: High (Fix This Sprint)

| # | Issue | Impact | Effort |
|---|-------|--------|--------|
| 6 | Replace all `@Autowired` field injection with `@RequiredArgsConstructor` | Testability | 4 hours |
| 7 | Replace magic role strings with enum comparisons | Type safety | 3 hours |
| 8 | Rename all non-conforming identifiers (40+) | Readability | 4 hours |
| 9 | Extract authorization logic into dedicated policy classes | Maintainability | 3 hours |
| 10 | Replace `==` for string comparison with `.equals()` | Correctness | 1 hour |

### 🟢 Priority 3: Medium (Next Sprint)

| # | Issue | Impact | Effort |
|---|-------|--------|--------|
| 11 | Extract `markAsRead/markAsUnread` → single method | DRY | 1 hour |
| 12 | Extract duplicate `recalculateDerivedFields` method | DRY | 30 min |
| 13 | Extract `buildSummary` stream logic into helper method | Readability | 1 hour |
| 14 | Rename `CheckUser` → `UserValidator`, remove `@Service` annotation | Clarity | 30 min |
| 15 | Externalize magic numbers to `application.yml` | Configurability | 1 hour |

### 🔵 Priority 4: Low (Backlog)

| # | Issue | Impact | Effort |
|---|-------|--------|--------|
| 16 | Rename `PickupService` in inventory-service → `InventoryPickupService` | Clarity | 30 min |
| 17 | Fix `Boolean` wrapper → primitive `boolean` | Consistency | 30 min |
| 18 | Add builder/factory for `FinancialSummary` creation | Readability | 1 hour |
| 19 | Implement single-pass stream aggregation for summary building | Performance | 2 hours |
