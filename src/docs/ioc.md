# Inversion of Control (IoC)
- IoC is one of the core principles of Spring
- Normally, I create and manage objects myself
- With IoC, Spring takes control of creating and managing objects
- The control is inverted (reversed) from me to Spring

---

## Without IoC

### Example

```java
public class StudentService {

    private StudentDao studentDao = new StudentDaoImpl();
}
```

- StudentService creates its own dependency
- StudentService decides which implementation to use
- StudentService manages the object's lifecycle

### takeaway:
- I am in control
- Every class creates its own dependencies
- Classes become tightly coupled

---

## With IoC

### Example

```java
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
}
```
- StudentService does not create StudentRepository
- Spring creates StudentRepository
- Spring provides StudentRepository to StudentService
- StudentService only focuses on business logic

### takeaway:
- Spring is now in control
- Spring manages object creation
- Spring manages object wiring

---

## What Does "Control" Mean?

- Creating objects
- Managing object lifecycle
- Managing dependencies
- Connecting objects together

Without Spring:

```java
StudentDao dao = new StudentDaoImpl();

StudentService service = new StudentService(dao);
```

I am responsible for everything.

With Spring:

```java
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
}
```

Spring handles everything behind the scenes.

---

## IoC Container

### Realization:
- The IoC Container is the part of Spring that manages objects
- It creates objects (Beans)
- It stores Beans
- It injects dependencies
- It manages the Bean lifecycle

### takeaway:
- The IoC Container is the "brain" of Spring
- Most Spring features happen because of the IoC Container

---

## Startup Lifecycle (Simplified)

### Step 1

Spring Boot starts.

```java
@SpringBootApplication
public class MonetraApplication {
}
```

### Step 2

Spring scans packages.

Examples:

```java
@Service
public class TransactionService {
}
```

```java
@Repository
public interface AccountRepository
        extends JpaRepository<Account, UUID> {
}
```

### Step 3

Spring creates Beans.

Examples:

- TransactionService Bean
- AccountRepository Bean
- UserRepository Bean

### Step 4

Spring resolves dependencies.

Example:

```java
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
}
```

Spring sees:

```java
AccountRepository
```

and

```java
UserRepository
```

already exist as Beans.

### Step 5

Spring injects them into TransactionService.

### takeaway:
- Spring scans
- Spring creates Beans
- Spring connects Beans
- Spring manages Beans

---

## Bean
- A Bean is simply an object managed by Spring

Example:

```java
@Service
public class TransactionService {
}
```

TransactionService becomes a Spring Bean.

Example:

```java
@Repository
public interface UserRepository
        extends JpaRepository<User, UUID> {
}
```

UserRepository becomes a Spring Bean.

### My takeaway:
- Every Bean is an object
- Not every object is a Bean
- A Bean is an object managed by Spring

---

## Relationship Between IoC and DI
- IoC is the principle
- Dependency Injection is one way Spring implements IoC

---

## Final Realization

Without Spring:

```java
StudentDao dao = new StudentDaoImpl();

StudentService service = new StudentService(dao);
```

I create and connect everything.

With Spring:

```java
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
}
```

Spring creates everything and connects everything.

This is Inversion of Control.