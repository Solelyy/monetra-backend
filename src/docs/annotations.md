## General Key Takeaways

---

## 1. JPA / Entity Annotations

These annotations define how Java classes map to database tables.

- @Entity
    - marks the class as a database table
    - managed by JPA/Hibernate

- @Table(name = "table_name")
    - explicitly sets DB table name
    - optional (defaults to class name)

- @Id
    - defines primary key

- @GeneratedValue
    - defines ID generation strategy

  Example:
  ```java
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  ```
    - database auto-increments the ID

- @Column
    - maps field to DB column
    - allows constraints:
        - nullable
        - unique
        - length

  ```java
  @Column(nullable = false, unique = true)
  ```

- @Enumerated
    - defines how enum is stored

  ```java
  @Enumerated(EnumType.STRING)
  ```

    - STRING = readable in DB
    - ORDINAL = numeric (not recommended)

---

## 2. Relationship Annotations (VERY IMPORTANT)

- @OneToOne
    - one-to-one relationship

- @OneToMany
    - one parent → many children

- @ManyToOne
    - many children → one parent
    - most commonly used

- @ManyToMany
    - many-to-many relationship

- @JoinColumn
    - defines foreign key column

  ```java
  @JoinColumn(name = "client_id")
  ```

- mappedBy
    - defines inverse relationship side
    - prevents duplicate join tables

---

## 3. Spring Stereotype Annotations

- @Component
    - generic Spring-managed bean

- @Service
    - business logic layer

- @Repository
    - database layer
    - converts DB exceptions into Spring exceptions

- @Controller
    - returns views (MVC)

- @RestController
    - REST API controller
    - returns JSON automatically

---

## 4. Dependency Injection & Bean Management

- @Autowired
    - injects dependencies automatically
    - supports field, setter, constructor injection
    - constructor injection is preferred

- @RequiredArgsConstructor (Lombok)
    - generates constructor for:
        - final fields
        - @NonNull fields

  Example:
  ```java
  @RequiredArgsConstructor
  public class UserService {

      private final UserRepository userRepository;
      private final EmailService emailService;

      @NonNull
      private String version;
  }
  ```

- @Bean
    - defines Spring-managed object manually

  ```java
  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }
  ```

- @Configuration
    - class that defines beans

---

## 5. Web / REST API Annotations

- @RequestMapping
    - base URL for controller

- @GetMapping
    - GET request

- @PostMapping
    - POST request

- @PutMapping
    - PUT request

- @DeleteMapping
    - DELETE request

- @PatchMapping
    - partial update

---

### Request Binding

- @RequestBody
    - JSON → Java object

- @PathVariable
    - URL path variable

  ```
  /users/{id}
  ```

- @RequestParam
    - query parameters

  ```
  /users?role=ADMIN
  ```

---

## 6. Exception Handling

- @RestControllerAdvice
    - global exception handler
    - applies to all controllers
    - returns JSON responses

- @ExceptionHandler
    - handles specific exception type

Example:
```java
@ExceptionHandler(ClientNotFoundException.class)
public ResponseEntity<ApiError> handleClientNotFound(
    ClientNotFoundException ex
)
```

### Flow:
- service throws RuntimeException
- controller ignores it
- global handler catches it
- returns formatted response

---

## 7. Security Annotations

- @PreAuthorize
    - method-level security check
    - runs before method execution

Example:
```java
@PreAuthorize("isAuthenticated()")
```

Other examples:
```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAuthority('TRANSFER_MONEY')")
```

### Flow:
Request → Security Filter → PreAuthorize → Controller

---

## 8. Transaction Management

- @Transactional
    - wraps method in DB transaction

### Behavior:
- success → COMMIT
- RuntimeException → ROLLBACK

Example:
```java
@Transactional
public void transferMoney() {
    debitAccount();
    creditAccount();
}
```

### Purpose:
- prevents partial updates
- ensures data consistency

---

## 9. JPA Lifecycle Hooks

- @PrePersist
    - runs before insert

- @PreUpdate
    - runs before update

Used for:
- timestamps
- audit fields

---

## 10. Lombok Annotations

- @RequiredArgsConstructor
- @Getter
- @Setter
- @ToString
- @NoArgsConstructor
- @AllArgsConstructor
- @Builder

Example:
```java
ApiError.builder()
    .status(404)
    .message("Not found")
    .build();
```

---

## 11. Utility Annotations

- @Override
    - ensures correct method override

- @NonNull
    - prevents null values

---

## 12. Spring Boot Flow Model

### Request Flow
```
Controller → Service → Repository → Database
```

### Error Flow
```
Service throws exception → GlobalExceptionHandler → ResponseEntity
```

### Security Flow
```
Request → Filter → @PreAuthorize → Controller
```

### Transaction Flow
```
@Transactional method → DB ops → commit/rollback
```

---

## 13. Key Mental Model

Spring Boot is:

> A framework that removes boilerplate and manages everything using annotations.

Annotations are:
- metadata
- instructions to Spring
- runtime triggers

---

## 14. DTO Validation Annotations

These validate incoming request data before reaching service layer.

---

### Core Validation

- @Valid
    - triggers validation on DTO

- @NotNull
    - field must not be null

- @NotBlank
    - not null, not empty, not whitespace

- @NotEmpty
    - not null, not empty (strings/collections)

- @Email
    - validates email format

- @Size(min, max)
    - validates length

- @Min / @Max
    - numeric boundaries

- @Pattern
    - regex validation

---

### Example DTO

```java
public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Pattern(regexp = "^[0-9]{11}$")
    private String mobileNumber;
}
```

---

### Validation Flow

```
Request → DTO Mapping → @Valid → Service
```

If invalid:
```
MethodArgumentNotValidException → GlobalExceptionHandler
```

---

### Key Principle

DTO validation is the **first gate** before business logic.

Service assumes:
> "Data is already valid"