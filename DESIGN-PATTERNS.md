# GoTrack Logistics — Design Patterns Analysis

> **Project**: GoTrack Logistics System (SE-2)
> **Architecture**: Microservices with Spring Boot, Spring Cloud Gateway, Netflix Eureka
> **Services**: auth-service, user-branch-service, inventory-service, core-logistic-finance, support-and-notifications-service, api-gateway, service-discovery
> **Generated**: 2026-05-10

---

## Executive Summary

| # | Pattern | Category | Service(s) | Evidence Level |
|---|---------|----------|------------|----------------|
| 1 | **Layered Architecture** | Architectural | All services | Strong — consistent Controller → Service → Repository across all services |
| 2 | **Repository Pattern** | Architectural | inventory-service, core-logistic-finance, user-branch-service, auth-service | Strong — `ProductRepository`, `PickupRepo`, `BranchRepository`, `AccountRepository`, etc. |
| 3 | **DTO Pattern** | Architectural | All services | Strong — `ProductDTO`, `BranchDTO`, `LoginRequest`, `CreateComplaint`, etc. |
| 4 | **Service Layer** | Architectural | All services | Strong — `ProductService`, `PickupService`, `AuthService`, `ComplaintService`, etc. |
| 5 | **MVC** | Architectural | All services | Strong — `@RestController` + service layer + domain entities |
| 6 | **Microservices** | Architectural | All | Strong — 7 separate services, Feign clients, Eureka discovery |
| 7 | **API Gateway** | Architectural | api-gateway | Strong — Spring Cloud Gateway with JWT validation |
| 8 | **Specification** | Structural | core-logistic-finance, inventory-service, user-branch-service | Strong — JPA `Specification` for dynamic queries |
| 9 | **State Machine** | Behavioral | core-logistic-finance, support-and-notifications-service | Strong — `PickupStatus`, `ShipmentStatus`, `ComplaintStatus` with transition rules |
| 10 | **Strategy Pattern** | Behavioral | support-and-notifications-service | Strong — `NotificationListener` switch-by-channel |
| 11 | **Adapter (Feign)** | Structural | inventory-service, core-logistic-finance, user-branch-service | Strong — `ProfileBranchClient`, `AuthClient`, `BranchClient` |
| 12 | **Template Method** | Behavioral | core-logistic-finance | Strong — `FinanceService.calculate()` with category branching |
| 13 | **Bridge Pattern** | Structural | user-branch-service | Strong — `ProfileMapper` interface + `ProfileMapperImp` implementation |
| 14 | **Facade Pattern** | Structural | core-logistic-finance, inventory-service | Strong — `ProfileBranchService`, `UserServices` wrapping Feign calls |
| 15 | **Singleton** | Creational | All services | Strong — Spring `@Component`, `@Service`, `@Configuration` beans |
| 16 | **Builder Pattern** | Creational | support-and-notifications-service | Strong — Lombok `@Builder` on `Complaint`, `Notifications` |
| 17 | **Factory Method** | Creational | core-logistic-finance | Moderate — `ReportPeriod.getStartDate()` factory method |
| 18 | **Chain of Responsibility** | Behavioral | auth-service | Strong — JWT filter → exception handlers → error responses |
| 19 | **Observer** | Behavioral | support-and-notifications-service | Strong — Spring events: `ComplaintSubmit`, `ComplaintStatusUpdateEvent`, `NotificationCreated` |
| 20 | **Wrapper / Decorator** | Structural | auth-service | Strong — `ContentCachingRequestWrapper`, `ContentCachingResponseWrapper` in gateway |
| 21 | **Dependency Injection (via @Bean)** | Architectural | All config classes | Strong — `ApplicationConfig`, `SecurityConfig`, `MapperConfig`, `AsyncConfig` |
| 22 | **AOP Logging Aspect** | Structural | All services | Strong — `LoggingAspect` / `LoggingAOP` in every service |
| 23 | **Custom Exception Hierarchy** | Behavioral | All services | Strong — `NotFoundException`, `ConflictException`, `ForbiddenException`, etc. |

---

## 1. Architectural Patterns

### 1.1 Layered Architecture

**Category**: Architectural
**Used in**: All services

The GoTrack system follows a strict three-layer architecture consistently across every microservice:

- **Presentation Layer**: `@RestController` classes handling HTTP requests/responses
- **Business Logic Layer**: Service classes implementing domain logic
- **Data Access Layer**: Repository interfaces extending `JpaRepository`/`MongoRepository`

**Why**: Separation of concerns ensures each layer has a single responsibility, making services independently deployable and testable — essential for a microservices architecture.

**Code Example** — `user-branch-service` (`BranchController.java`):
```java
// Line 23-25: Presentation Layer
@RestController
@Validated
public class BranchController {
    private BranchService branchService;
    private BranchMapper<BranchEntity, BranchDTO> branchMapper;

    // Line 36-43: Controller delegates to Service layer
    @PostMapping(path = "/api/branches")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody BranchDTO branch, ...) {
        BranchEntity branchEntity = branchMapper.mapFrom(branch);        // DTO → Entity
        BranchEntity savedBranchEntity = branchService.createBranch(branchEntity); // Service call
        return ResponseEntity.ok(branchMapper.mapTo(savedBranchEntity)); // Entity → DTO
    }
}
```

**Code Example** — `inventory-service` (`ProductController.java` / `ProductService.java`):
```java
// Service layer (ProductService.java, lines 33-64)
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserServices userServices;

    public ProductResponseDTO createProduct(ProductDTO productDto) {
        // Business logic: validation, authorization, persistence
        if (productRepository.existsByBaseSku(productDto.getBaseSku())) {
            throw new ConflictException("baseSku already exists");
        }
        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        return ProductResponseDTO.builder()...build();
    }
}
```

---

### 1.2 Repository Pattern

**Category**: Architectural
**Used in**: All services with JPA/MongoDB

Every service uses Spring Data repositories as an abstraction over the persistence layer:

| Service | Repository | Database |
|---------|-----------|----------|
| auth-service | `AccountRepository`, `RefreshTokenRepo` | MongoDB |
| user-branch-service | `BranchRepository`, `ProfileRepository` | PostgreSQL |
| inventory-service | `ProductRepository`, `InventoryRepository` | MySQL |
| core-logistic-finance | `PickupRepo`, `ShipmentRepo`, `TransactionRepo`, `WalletRepo` | MySQL |
| support-and-notifications-service | `ComplaintRepo`, `NotificationRepo` | MongoDB |

**Code Example** — `inventory-service` (`ProductRepository.java`):
```java
// Repository interface — abstracts database operations
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByBaseSku(String baseSku);
    boolean existsByNameAndMerchantId(String name, Long merchantId);
    Optional<Page<Product>> findByMerchantId(Long merchantId, Pageable pageable);
}
```

**Code Example** — `auth-service` with custom repository (`AccountRepositoryCustom.java` + `AccountRepositoryImpl.java`):
```java
// Custom query interface (AccountRepositoryCustom.java)
public interface AccountRepositoryCustom {
    Page<AccountResponse> findAllAccounts(ListAccountsRequest request, Pageable pageable);
}

// Custom query implementation (AccountRepositoryImpl.java)
public class AccountRepositoryImpl implements AccountRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public Page<AccountResponse> findAllAccounts(ListAccountsRequest request, Pageable pageable) {
        // Custom SQL for paginated account listing
    }
}
```

---

### 1.3 DTO (Data Transfer Object) Pattern

**Category**: Architectural
**Used in**: All services

Every service uses DTOs to decouple API contracts from domain entities. Naming conventions:
- `*DTO` — input DTOs
- `*ResponseDTO` — output DTOs
- `*RequestDTO` — request DTOs

**Code Example** — `user-branch-service` (`BranchDTO.java`):
```java
// DTO separating API contract from domain entity
public class BranchDTO {
    private Long id;
    private String name;
    private String location;
    private String phone;
}
```

**Code Example** — `inventory-service` (`ApiResponse<T>`):
```java
// Generic response wrapper — used across all services
@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
```

**Code Example** — `user-branch-service` (`PageResponse<T>`):
```java
// Pagination wrapper — consistent response format
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
```

---

### 1.4 Service Layer Pattern

**Category**: Architectural
**Used in**: All services

Each microservice encapsulates its domain logic behind service classes. Services use dependency injection and are managed as Spring beans.

**Code Example** — `auth-service` (`AuthService.java`):
```java
@Service
public class AuthService {
    @Autowired AccountRepository userRepository;
    @Autowired JwtService jwtService;
    @Autowired AuthenticationManager authenticationManager;
    @Autowired RefreshTokenService refreshTokenService;

    public AuthResponse login(LoginRequest request) {
        // Business logic: lookup, validate credentials, generate tokens
        Account user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password"));
        // ... authentication and token generation
        String token = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new AuthResponse(token, user.getRole().name(), user.getId(), refreshToken);
    }
}
```

---

### 1.5 Microservices Architecture

**Category**: Architectural
**Evidence**: Entire project

The system consists of 7 independent services communicating via REST/Feign:

| Service | Port | Database | Responsibility |
|---------|------|----------|----------------|
| api-gateway | 8080 | — | Request routing, JWT validation, logging |
| service-discovery | 8761 | — | Netflix Eureka service registry |
| auth-service | 8081 | MongoDB | Account management, JWT auth |
| user-branch-service | 8082 | PostgreSQL | Branches, profiles, user management |
| inventory-service | 8083 | MySQL | Products, inventory items, pickups |
| core-logistic-finance | 9091 | MySQL | Shipments, pickups, transactions, wallets |
| support-and-notifications-service | — | MongoDB | Complaints, notifications |

---

### 1.6 API Gateway Pattern

**Category**: Architectural
**Used in**: api-gateway

The API Gateway centralizes cross-cutting concerns: JWT validation, request logging, header propagation, and routing.

**Code Example** — `api-gateway` (`RequestLoggingFilter.java`):
```java
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Generate trace ID for distributed tracing
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        // Wrap request/response for body logging
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 5000);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // Log with timing, mask sensitive data
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(wrappedRequest, wrappedResponse);
        long executionTime = System.currentTimeMillis() - startTime;
        // ...
    }
}
```

---

### 1.7 Dependency Injection via @Bean (Factory Pattern)

**Category**: Creational / Architectural
**Used in**: All config classes

Spring's `@Configuration` + `@Bean` is used extensively to create beans with custom initialization.

**Code Example** — `auth-service` (`ApplicationConfig.java`):
```java
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AccountRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

**Code Example** — `user-branch-service` (`MapperConfig.java`):
```java
@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
```

**Code Example** — `support-and-notifications-service` (`AsyncConfig.java`):
```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("event-thread-");
        executor.initialize();
        return executor;
    }
}
```

---

## 2. Creational Patterns

### 2.1 Singleton (Spring Container)

**Category**: Creational
**Used in**: All services

Spring's IoC container provides singleton-scoped beans by default. Every `@Component`, `@Service`, `@Controller`, `@Repository`, `@Configuration` bean is a singleton.

**Evidence**: All service classes are singletons managed by Spring:
- `AuthService` — singleton managing authentication flow
- `ComplaintService` — singleton handling complaint lifecycle
- `ProductService` — singleton managing product operations

**Code Example** — `auth-service` (`RefreshTokenService.java`):
```java
@Service  // ← Spring creates exactly one instance
public class RefreshTokenService {
    @Autowired RefreshTokenRepo repo;

    public String createRefreshToken(String email) {
        repo.deleteByEmail(email);
        String token = UUID.randomUUID().toString();  // Factory for unique tokens
        RefreshToken rt = new RefreshToken();
        rt.setToken(token);
        rt.setEmail(email);
        rt.setExpiryDate(new Date(System.currentTimeMillis() + 3600000L * 72));
        repo.save(rt);
        return token;
    }
}
```

---

### 2.2 Builder Pattern (Lombok @Builder)

**Category**: Creational
**Used in**: support-and-notifications-service

Lombok's `@Builder` generates fluent builders for complex entities.

**Code Example** — `support-and-notifications-service` (`Complaint.java`):
```java
@Getter
@Setter
@Builder    // ← Generated builder: Complaint.builder().profileId(...).status(...).build()
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Complaint")
public class Complaint {
    @Id
    private String id;
    private String profileId;
    private Long shipmentId;
    private String subject;
    private String content;
    private ComplaintStatus status;
    private String note;
    private String email;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
```

**Usage** — `ComplaintService.java` (line 53-61):
```java
Complaint complaint = Complaint.builder()
        .profileId(profileId)
        .email(email)
        .shipmentId(request.getShipmentId())
        .subject(request.getSubject())
        .content(request.getContent())
        .createdAt(Instant.now())
        .status(ComplaintStatus.PENDING)
        .build();
```

---

### 2.3 Factory Method (ReportPeriod)

**Category**: Creational
**Used in**: core-logistic-finance

The `ReportPeriod` enum acts as a factory, creating appropriate date ranges based on the selected period.

**Code Example** — `core-logistic-finance` (`ReportPeriod.java`):
```java
public enum ReportPeriod {
    DAILY, WEEKLY, MONTHLY, YEARLY;

    // Factory method: creates appropriate start date for each period
    public LocalDate getStartDate(LocalDate now) {
        return switch (this) {
            case DAILY  -> now;
            case WEEKLY -> now.minusWeeks(1);
            case MONTHLY-> now.minusMonths(1);
            case YEARLY -> now.minusYears(1);
            default     -> now;
        };
    }
}
```

**Usage** — `FinanceService.java` (line 126-132):
```java
public FinancialSummaryDTO buildSummary(ReportPeriod period) {
    LocalDate now = LocalDate.now();
    LocalDate start = period.getStartDate(now);  // Factory method delegates date calculation
    List<FinancialSummary> summaries = financeRepo.findByCreatedAtBetween(start, now);
    // ...
}
```

---

## 3. Structural Patterns

### 3.1 Specification Pattern (Dynamic Query Composition)

**Category**: Structural
**Used in**: core-logistic-finance, inventory-service, user-branch-service

JPA `Specification` pattern enables composing dynamic, type-safe queries from filter objects.

**Code Example** — `core-logistic-finance` (`PickupSpecification.java`):
```java
public class PickupSpecification {
    public static Specification<Pickup> filterPickups(PickupFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }
            if (filter.getMERCHANTId() != null) {
                predicates.add(cb.equal(root.get("MERCHANTId"), filter.getMERCHANTId()));
            }
            if (filter.getCourierId() != null) {
                predicates.add(cb.equal(root.get("courierId"), filter.getCourierId()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(
                    cb.lower(root.get("status").as(String.class)),
                    filter.getStatus().toLowerCase()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

**Usage** — `PickupService.java` (line 100-102):
```java
Specification<Pickup> spec = PickupSpecification.filterPickups(filter);
return pickupRepository.findAll(spec, pageable)
        .map(pickupMapper::toDTO);
```

---

### 3.2 Bridge Pattern (Mapper Interface + Implementation)

**Category**: Structural
**Used in**: user-branch-service

The mapper interface is decoupled from its implementation, allowing different mapping strategies.

**Code Example** — `user-branch-service` (`ProfileMapper.java`):
```java
// Interface — abstraction
public interface ProfileMapper<E, D> {
    ProfileResponseDTO toDto(ProfileEntity entity);
    ProfileEntity toEntity(ProfileResponseDTO dto);
}
```

**Code Example** — `user-branch-service` (`ProfileMapperImp.java`):
```java
// Implementation — concrete strategy
@Component
public class ProfileMapperImp implements ProfileMapper<ProfileEntity, ProfileResponseDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProfileResponseDTO toDto(ProfileEntity entity) {
        return modelMapper.map(entity, ProfileResponseDTO.class);
    }

    @Override
    public ProfileEntity toEntity(ProfileResponseDTO dto) {
        return modelMapper.map(dto, ProfileEntity.class);
    }
}
```

---

### 3.3 Adapter Pattern (Feign Clients)

**Category**: Structural
**Used in**: inventory-service, core-logistic-finance, user-branch-service

Spring Cloud Feign adapts HTTP service endpoints into typed Java interfaces.

**Code Example** — `user-branch-service` (`AuthClient.java`):
```java
@FeignClient(name = "auth-service")  // ← Adapters HTTP endpoints into typed interface
public interface AuthClient {
    @GetMapping("/api/auth/accounts/{id}")
    Map<String, Object> getAccountById(@PathVariable("id") String id);

    @GetMapping("/api/auth/accounts/super-admin/{id}")
    Boolean isSuperAdmin(@PathVariable("id") String id);
}
```

**Code Example** — `core-logistic-finance` (`ProfileBranchClient.java`):
```java
@FeignClient(name = "user-branch-service")
public interface ProfileBranchClient {
    @GetMapping("/api/users/profiles/account/{accountId}")
    ProfileResponse getProfileByAccountId(@PathVariable("accountId") String accountId);

    @GetMapping("/api/users/profiles/{id}")
    ProfileResponse getProfileById(@PathVariable("id") Long Id);
}
```

---

### 3.4 Facade Pattern (Service Wrapper)

**Category**: Structural
**Used in**: inventory-service, core-logistic-finance

Wrapper services simplify complex subsystem calls by providing a unified interface.

**Code Example** — `core-logistic-finance` (`ProfileBranchService.java`):
```java
@Service
public class ProfileBranchService {
    @Autowired
    private ProfileBranchClient profileBranchClient;  // Feign client (adapter)

    // Facade: hides Feign client complexity behind simple methods
    public ProfileResponse getProfileByAccountId(String id) {
        return profileBranchClient.getProfileByAccountId(id);
    }

    public ProfileResponse getProfileById(Long id) {
        return profileBranchClient.getProfileById(id);
    }
}
```

**Usage** — `PickupService.java` (line 36-37):
```java
AuthenticationDetails authDetails = new AuthenticationDetails();
String accountId = authDetails.getAccountId();
ProfileResponse profile = profileBranchService.getProfileByAccountId(accountId);
```

---

### 3.5 Wrapper Pattern (Content Caching in Gateway)

**Category**: Structural
**Used in**: api-gateway

Spring's `ContentCachingRequestWrapper` and `ContentCachingResponseWrapper` wrap original HTTP wrappers to enable body replay.

**Code Example** — `api-gateway` (`RequestLoggingFilter.java`, lines 44-47):
```java
ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 5000);
ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
// Wrappers allow reading request/response body while forwarding the original request
filterChain.doFilter(wrappedRequest, wrappedResponse);
```

---

### 3.6 AOP Logging Aspect (Aspect-Oriented Structural)

**Category**: Structural
**Used in**: All services

Every service includes a `LoggingAspect` that uses AOP to crosscut logging concerns across controller, service, and repository layers.

**Code Example** — `inventory-service` (`LoggingAspect.java`):
```java
@Aspect
public class LoggingAspect {
    private static final String BASE_PACKAGE = "com.gotrack";

    // Pointcut: matches all controller, service, repository beans
    @Pointcut("within(com.gotrack..*) && !within(com.gotrack..config..*)")
    public void app() {}

    @Around("controller() || service() || repository()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("[EXECUTION TIME] IN {}.{}() | args = ( {} ) | execution time = {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()),
                    (endTime - startTime));
            return result;
        } catch (Throwable ex) {
            // Same logging but with exception details
        }
    }

    @AfterThrowing(pointcut = "app()", throwing = "ex")
    public void logException(JoinPoint jpoint, Throwable ex) {
        log.error("[EXCEPTION] IN {}.{}() | cause={} | message={}",
                jpoint.getTarget().getClass().getSimpleName(),
                jpoint.getSignature().getName(),
                ex.getCause(), ex.getMessage());
    }
}
```

---

## 4. Behavioral Patterns

### 4.1 State Machine Pattern (Pickup Status Lifecycle)

**Category**: Behavioral
**Used in**: core-logistic-finance, support-and-notifications-service

Enum-based state machines enforce valid state transitions with business rules.

**Code Example** — `core-logistic-finance` (`PickupStatus.java`):
```java
public enum PickupStatus {
    Pending, Accepted, Completed, CurierAssigned, Cancelled;

    // State machine: only valid transitions are allowed
    public boolean canTransitionTo(PickupStatus newStatus) {
        return switch (this) {
            case Pending         -> newStatus == Accepted || newStatus == Cancelled;
            case Accepted        -> newStatus == Completed || newStatus == CurierAssigned || newStatus == Cancelled;
            case CurierAssigned  -> newStatus == Completed || newStatus == Cancelled;
            default              -> false;  // Terminal states (Completed, Cancelled) reject all transitions
        };
    }
}
```

**Usage** — `PickupService.java` (line 53-60):
```java
PickupStatus currentStatus = existingPickup.getStatus();
PickupStatus newStatus = pickupRequest.getStatus();

if (!currentStatus.canTransitionTo(newStatus)) {
    throw new ConflictException(
        "Pickup cannot be transitioned from " + currentStatus + " to " + newStatus);
}
```

**Code Example** — `core-logistic-finance` (`ShipmentStatus.java`):
```java
public enum ShipmentStatus {
    PendingPickup, InTransitToWarehouse, ArrivedAtWarehouse,
    OutForDelivery, InTransitToMERCHANT, DELIVERED;

    public boolean canTransitionToS(ShipmentStatus newStatus) {
        return switch (this) {
            case PendingPickup         -> newStatus == InTransitToWarehouse;
            case InTransitToWarehouse  -> newStatus == ArrivedAtWarehouse;
            case ArrivedAtWarehouse    -> newStatus == OutForDelivery;
            case OutForDelivery        -> newStatus == InTransitToMERCHANT;
            case InTransitToMERCHANT  -> newStatus == DELIVERED;
            default                    -> false;
        };
    }
}
```

**Code Example** — `support-and-notifications-service` (`ComplaintStatus.java` + `CheckStatusTransition.java`):
```java
@Component
public class CheckStatusTransition {
    private static final Map<ComplaintStatus, Set<ComplaintStatus>> transitions = Map.of(
        ComplaintStatus.PENDING,       Set.of(ComplaintStatus.IN_PROGRESS),
        ComplaintStatus.IN_PROGRESS,   Set.of(ComplaintStatus.RESOLVED),
        ComplaintStatus.RESOLVED,      Set.of(ComplaintStatus.COMPENSATED),
        ComplaintStatus.COMPENSATED,   Set.of()  // Terminal state
    );

    public void checkTransition(ComplaintStatus current, ComplaintStatus next) {
        if (!transitions.getOrDefault(current, Set.of()).contains(next)) {
            throw new InvalidStatusTransitionException(
                "Cannot transition from " + current + " to " + next);
        }
    }
}
```

---

### 4.2 Observer Pattern (Spring Events)

**Category**: Behavioral
**Used in**: support-and-notifications-service

Spring's event-driven architecture enables decoupled notification delivery via `ApplicationEventPublisher`.

**Event Publication** — `ComplaintService.java` (lines 64-66):
```java
// Publishes events when complaints are created or status-updated
eventPublisher.publish(new ComplaintSubmit(
    saved.getId(), profileId, email, saved.getSubject(), saved.getShipmentId(), request.getChannel()));
```

**Event Listener** — `NotificationListener.java`:
```java
@Component
public class NotificationListener {
    @Autowired JavaMailSender mailSender;

    @Async
    @EventListener
    public void handleNotificationCreatedEvent(NotificationCreated event) {
        switch (event.getChannel()) {
            case SMS:    sendsms(event);   break;
            case EMAIL:  sendemail(event); break;
            case IN_APP: sendinapp(event); break;
            case WHATSAPP: sendwhatsapp(event); break;
            default: log.warn("Unknown channel: {}", event.getChannel());
        }
    }
}
```

**Event Wrapper** — `WEventPublisher.java`:
```java
@Component
public class WEventPublisher {
    @Autowired private ApplicationEventPublisher publisher;

    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
```

---

### 4.3 Strategy Pattern (Notification Channel Selection)

**Category**: Behavioral
**Used in**: support-and-notifications-service

The notification system selects the delivery strategy (SMS, EMAIL, IN_APP, WHATSAPP) based on the event's channel.

**Code Example** — `NotificationListener.java` (lines 32-47):
```java
@Async
@EventListener
public void handleNotificationCreatedEvent(NotificationCreated event) {
    switch (event.getChannel()) {
        case SMS:      sendsms(event);     break;   // Strategy: send via SMS
        case EMAIL:    sendemail(event);   break;   // Strategy: send via SMTP
        case IN_APP:   sendinapp(event);   break;   // Strategy: store in DB
        case WHATSAPP: sendwhatsapp(event);break;   // Strategy: send via WhatsApp API
        default:       log.warn("Unknown channel: {}", event.getChannel());
    }
}
```

Each strategy is a separate private method, and new notification channels can be added by:
1. Adding a new enum value to `NotificationChannel`
2. Adding a new `case` branch

---

### 4.4 Template Method Pattern (Financial Calculation)

**Category**: Behavioral
**Used in**: core-logistic-finance

The `FinanceService.calculate()` method defines the template for financial calculation, with different branches handling each transaction category.

**Code Example** — `core-logistic-finance` (`FinanceService.java`, lines 54-105):
```java
public void calculate(TransactionDTO transaction) {
    FinancialSummary financialSummary = getTodaySummary();  // ← Template step 1: load context

    if (transaction.getType().getCatg() == TransactionCatg.REVENUE) {
        financialSummary.setNetCash(financialSummary.getNetCash().add(transaction.getAmount()));
    }
    else if (transaction.getType().getCatg() == TransactionCatg.EXPENSE) {
        financialSummary.setExpenses(financialSummary.getExpenses().add(transaction.getAmount()));
        financialSummary.setNetCash(financialSummary.getNetCash().subtract(transaction.getAmount()));
    }
    // ... additional category-specific branches
    // ← Template step N: persist result
    financialSummary.setNetProfit(...);
    financialSummary.setProfitMargin(...);
    financeRepo.save(financialSummary);
}
```

The structure defines a fixed algorithm skeleton (load summary → process by category → recalculate → save) with category-specific variation at the branching points.

---

### 4.5 Chain of Responsibility (Exception Handling)

**Category**: Behavioral
**Used in**: All services

Each service uses `@RestControllerAdvice` with multiple `@ExceptionHandler` methods that form a chain — Spring routes exceptions to the most specific handler first.

**Code Example** — `inventory-service` (`GlobalalExceptionHandler.java`):
```java
@RestControllerAdvice
public class GlobalalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)  // ← Handler 1: validation errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException e) {
        // Returns 400 with field-level errors
    }

    @ExceptionHandler(ConflictException.class)               // ← Handler 2: duplicate resources
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleConflict(ConflictException e) {
        // Returns 409
    }

    @ExceptionHandler(ForbiddenException.class)              // ← Handler 3: permission denied
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleForbidden(ForbiddenException ex) {
        // Returns 403
    }

    @ExceptionHandler(NotFoundException.class)               // ← Handler 4: resource missing
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(NotFoundException ex) {
        // Returns 404
    }
}
```

The same pattern repeats in:
- `auth-service` (`GlobalExceptionHandler.java`)
- `user-branch-service` (`GlobalExceptionHandler.java`)
- `core-logistic-finance` (`GlobalExceptionHandler.java`)
- `support-and-notifications-service` (`GlobalExceptionHandler.java`)

---

### 4.6 Custom Exception Hierarchy

**Category**: Behavioral
**Used in**: All services

Each service defines a hierarchy of domain-specific exceptions extending `RuntimeException`, providing clear semantic error types.

**Code Example** — `inventory-service`:
```java
public class NotFoundException   extends RuntimeException { public NotFoundException(String m) { super(m); } }
public class ConflictException   extends RuntimeException { public ConflictException(String m) { super(m); } }
public class ForbiddenException  extends RuntimeException { public ForbiddenException(String m) { super(m); } }
```

**Code Example** — `auth-service` (more granular hierarchy):
```java
public class RefreshTokenExpiredException   extends RuntimeException { }
public class InvalidTokenException          extends RuntimeException { }
public class EmailAlreadyExistsException    extends RuntimeException { }
public class AccountNotFoundException       extends RuntimeException { }
public class AccountInactiveException       extends RuntimeException { }
```

**Code Example** — `support-and-notifications-service`:
```java
public class ResourceNotFoundException       extends RuntimeException { }
public class InvalidStatusTransitionException extends RuntimeException { }
public class BadRequestException            extends RuntimeException { }
public class ForbiddenActionException       extends RuntimeException { }
public class AccessDeniedException          extends RuntimeException { }
```

---

## 5. Service-by-Service Pattern Breakdown

### 5.1 auth-service (Port 8081)

| Pattern | Evidence |
|---------|----------|
| Layered Architecture | `AuthController` → `AuthService` → `AccountRepository` |
| Repository | `AccountRepository`, `RefreshTokenRepo` |
| Service Layer | `AuthService`, `AccountService`, `RefreshTokenService` |
| Singleton (Spring) | All `@Service` classes |
| Builder | DTOs use Lombok `@Builder`, `@AllArgsConstructor` |
| Custom Exception Hierarchy | 5 domain exceptions |
| AOP Logging | `LoggingAspect` |
| JWT as Token-Based Auth | `JwtService` + `JwtKeyService` |

---

### 5.2 user-branch-service (Port 8082)

| Pattern | Evidence |
|---------|----------|
| Layered Architecture | `BranchController` → `BranchService` → `BranchRepository` |
| Repository | `BranchRepository`, `ProfileRepository` |
| Service Layer | `BranchService` (interface) + `BranchServiceImp` (implementation) |
| Bridge Pattern | `ProfileMapper` interface + `ProfileMapperImp` impl |
| DTO | `BranchDTO`, `ProfileRequestDTO`, `ProfileResponseDTO` |
| PageResponse wrapper | `PageResponse<T>` |
| ApiResponse wrapper | `ApiResponse` |
| Specification | Inline Specification in `BranchServiceImp.search()` |
| Feign Client | `AuthClient` |
| AOP Logging | `LoggingAspect` |
| Custom Exception Hierarchy | `NotFoundException`, `ConflictException`, `BadRequestException` |

---

### 5.3 inventory-service (Port 8083)

| Pattern | Evidence |
|---------|----------|
| Layered Architecture | Controllers → Services → Repositories |
| Repository | `ProductRepository`, `InventoryRepository` |
| Service Layer | `ProductService`, `InventoryService`, `BranchServices`, `UserServices` |
| Specification | `inventorySpecifications.filterInventory()` |
| Adapter (Feign) | `BranchClient`, `UserClient` |
| Facade | `BranchServices`, `UserServices` — wrap Feign calls |
| MapStruct Mapper | `ProductMapper` (`@Mapper(componentModel = "spring")`) |
| DTO | `ProductDTO`, `ProductResponseDTO`, `ApiResponse<T>`, `InventoryItemRequest` |
| Custom Exception Hierarchy | `NotFoundException`, `ConflictException`, `ForbiddenException` |
| AOP Logging | `LoggingAspect` |

---

### 5.4 core-logistic-finance (Port 9091)

| Pattern | Evidence |
|---------|----------|
| Layered Architecture | Controllers → Services → Repositories |
| Repository | `PickupRepo`, `ShipmentRepo`, `TransactionRepo`, `WalletRepo` |
| Service Layer | `ShipmentService`, `PickupService`, `FinanceService`, `WalletService` |
| State Machine | `PickupStatus.canTransitionTo()`, `ShipmentStatus.canTransitionToS()` |
| Template Method | `FinanceService.calculate()` — category branching algorithm |
| Factory Method | `ReportPeriod.getStartDate()` |
| Specification | `PickupSpecification`, `ShipmentSpecification`, `TransactionSpecification` |
| Adapter (Feign) | `ProfileBranchClient` |
| Facade | `ProfileBranchService` |
| AOP Logging | `LoggingAspect` |
| Custom Exception Hierarchy | `ResourceNotFoundException`, `ConflictException` |

---

### 5.5 support-and-notifications-service

| Pattern | Evidence |
|---------|----------|
| Layered Architecture | Controllers → Services → Repositories |
| Observer (Events) | `ComplaintSubmit`, `ComplaintStatusUpdateEvent`, `NotificationCreated` |
| State Machine | `ComplaintStatus` + `CheckStatusTransition` |
| Strategy (Channel) | `NotificationListener` switch-by-channel |
| Builder | `Complaint.builder()`, `Notifications.builder()` |
| DTO | `CreateComplaint`, `UpdateComplaintStatus`, `ComplaintResponse` |
| Facade | `CurrentUserService`, `CheckUser`, `CheckShipment` |
| AOP Logging | `LoggingAOP` |
| Custom Exception Hierarchy | 5 domain exceptions |
| Async Execution | `@EnableAsync` + `ThreadPoolTaskExecutor` |

---

### 5.6 api-gateway

| Pattern | Evidence |
|---------|----------|
| Filter Chain | `OncePerRequestFilter` (request logging, JWT validation) |
| Wrapper | `ContentCachingRequestWrapper`, `ContentCachingResponseWrapper` |
| Singleton | All `@Component` beans |
| Configuration Properties | `@ConfigurationProperties` on `JwtKeyConfig` |

---

## Summary of Findings

The GoTrack Logistics System demonstrates a **mature microservices architecture** with strong adherence to established design patterns:

1. **Architectural patterns** dominate — Layered Architecture, Repository, DTO, Service Layer, and Microservices form the backbone
2. **State Machine** is the most prominent behavioral pattern — used across 3 services for lifecycle management
3. **Observer/Event-driven** pattern is used for decoupling notification delivery in the support service
4. **Bridge + Adapter** patterns are used together — mapper interfaces (Bridge) and Feign clients (Adapter) provide clean abstractions
5. **Facade** pattern wraps cross-service dependencies, reducing coupling between microservices
6. **Strategy** pattern handles multi-channel notification delivery
7. **Template Method** is used in financial calculations
8. **Chain of Responsibility** is implemented via Spring's `@RestControllerAdvice` exception handling
9. **Builder** pattern (via Lombok) constructs complex domain entities
10. **Factory Method** is used in `ReportPeriod` for date range generation

The codebase consistently applies patterns across services, following a clear and repeatable architecture that supports independent deployment, testing, and evolution of each microservice.
