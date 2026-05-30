### Dependency Injection (DI)

### What is Dependency Injection

- Dependency Injection is a design pattern where dependencies are provided from outside the class
- The class does not create its own dependencies
- It only declares what it needs

---

### Why DI exists

Without DI:

```java
public class StudentService {

    private StudentRepository studentRepository = new StudentRepositoryImpl();
}
```

- The class is tightly coupled to a specific implementation
- Hard to test
- Hard to change implementations

With DI:

```java
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
}
```

- The class no longer creates dependencies
- Dependencies are provided externally
- Implementation can be swapped easily

---

### Dependency Injection in Pure Java

```java
public class Main {

    public static void main(String[] args) {

        StudentRepository repository = new StudentRepositoryImpl();

        StudentService service = new StudentService(repository);
    }
}
```

- The application manually wires objects
- Main class acts as the "wiring layer"
- This is still Dependency Injection, just manual

---

### Dependency Injection in Spring

```java
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
}
```

- Spring automatically provides the dependency
- No manual object creation
- No `new` keyword for dependencies

---

### How Spring Injection Works

- Spring scans classes at startup
- Spring creates objects marked as Beans
- Spring detects dependencies in constructors
- Spring injects matching Beans automatically

Example:

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
```

Spring generates the implementation at runtime and registers it as a Bean.

---

### Types of Dependency Injection

---

#### Constructor Injection (Recommended)

```java
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
}
```

or manually:

```java
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
}
```

- Dependencies are required at object creation
- Supports `final`
- Best for immutability and testing

---

#### Setter Injection

```java
@Service
public class StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
}
```

- Dependency is injected after object creation
- Allows optional dependencies
- Object can exist without dependency initially

---

#### Field Injection

```java
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
}
```

- Spring injects directly into the field
- Dependency is hidden
- Not test-friendly
- Not recommended in modern Spring

---

### @Autowired vs Constructor Injection

- `@Autowired` is not required when using constructor injection
- Spring automatically uses the constructor if there is only one
- `@RequiredArgsConstructor` removes the need to manually write the constructor

---

### @RequiredArgsConstructor

```java
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
}
```

- Generates constructor automatically
- Includes only `final` and `@NonNull` fields
- Works perfectly with Spring constructor injection

---

### Comparison of Injection Types

| Type | Recommended | Notes |
|------|------------|------|
| Constructor Injection | Yes | Best practice |
| Setter Injection | Sometimes | Optional dependencies |
| Field Injection | No | Avoid in modern Spring |

---

### Key Idea

- Dependency Injection = providing dependencies from outside the class
- Spring automates the process using the IoC Container
- Constructor Injection is the most preferred approach in modern Spring Boot

#### Realization:
- Spring creates dependencies and provides them to classes
- Classes should not create their own dependencies
- Makes testing easier
- Reduces coupling

#### Constructor Injection (Recommended)

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}
```

##### Notes
- Uses constructor
- Supports `final`
- Easy to test
- Recommended by Spring
- Most common in modern Spring Boot

#### Setter Injection

```java
@Autowired
public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
}
```

##### Notes
- Used for optional dependencies
- Dependency can change after object creation

#### Field Injection

```java
@Autowired
private UserRepository userRepository;
```

##### Notes
- Dependency is hidden
- Harder to test
- Avoid in modern Spring Boot

#### takeaway:
- `@RequiredArgsConstructor` creates the constructor
- Spring performs the injection
- Lombok is not doing the injection itself