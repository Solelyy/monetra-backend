## SOLID Principles

SOLID is a set of five object-oriented design principles that help create software that is maintainable, flexible, scalable, and easy to test.

---

### S — Single Responsibility Principle (SRP)

A class should have only one reason to change.

A class should focus on a single responsibility or concern. When a class handles multiple responsibilities, changes in one area can affect unrelated functionality, making the code harder to maintain and test.

#### Bad Example

```java
public class UserService {

    public void registerUser() {
        // Register user
    }

    public void sendEmail() {
        // Send email
    }

    public void generateReport() {
        // Generate report
    }
}
```

#### Good Example

```java
public class UserService {

    public void registerUser() {
    }
}
```

```java
public class EmailService {

    public void sendEmail() {
    }
}
```

```java
public class ReportService {

    public void generateReport() {
    }
}
```

#### Benefits

- Easier maintenance
- Easier testing
- Better readability
- Reduced complexity

---

### O — Open/Closed Principle (OCP)

Software entities should be open for extension but closed for modification.

Existing code should remain stable while allowing new functionality to be added. New behavior should be introduced through extension rather than modifying proven code.

#### Bad Example

```java
public class PaymentService {

    public void process(String type) {

        if(type.equals("GCASH")) {
            // Process GCash
        }

        if(type.equals("PAYPAL")) {
            // Process PayPal
        }
    }
}
```

Adding a new payment method requires modifying the existing class.

#### Good Example

```java
public interface PaymentMethod {

    void process();
}
```

```java
public class GCashPayment implements PaymentMethod {

    @Override
    public void process() {
        System.out.println("Processing GCash");
    }
}
```

```java
public class PayPalPayment implements PaymentMethod {

    @Override
    public void process() {
        System.out.println("Processing PayPal");
    }
}
```

```java
public class PaymentService {

    public void process(PaymentMethod paymentMethod) {
        paymentMethod.process();
    }
}
```

#### Benefits

- Easier feature additions
- Reduced risk of breaking existing code
- Better extensibility
- Improved maintainability

---

### L — Liskov Substitution Principle (LSP)

Subtypes must be substitutable for their base types.

A child class should be able to replace its parent class without changing the correctness of the program. If replacing a parent with a child breaks behavior, the inheritance hierarchy is incorrect.

#### Bad Example

```java
public class Bird {

    public void fly() {
    }
}
```

```java
public class Penguin extends Bird {

    @Override
    public void fly() {
        throw new UnsupportedOperationException();
    }
}
```

A penguin is a bird, but it cannot fly.

#### Good Example

```java
public abstract class Bird {
}
```

```java
public interface Flyable {

    void fly();
}
```

```java
public class Eagle extends Bird implements Flyable {

    @Override
    public void fly() {
    }
}
```

```java
public class Penguin extends Bird {
}
```

#### Benefits

- Predictable inheritance
- Better polymorphism
- Fewer runtime surprises
- More reliable code reuse

---

### I — Interface Segregation Principle (ISP)

Clients should not be forced to depend on methods they do not use.

Large interfaces should be split into smaller, focused interfaces so that implementing classes only need to support behaviors that are relevant to them.

#### Bad Example

```java
public interface Worker {

    void work();

    void eat();

    void sleep();
}
```

```java
public class RobotWorker implements Worker {

    @Override
    public void work() {
    }

    @Override
    public void eat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sleep() {
        throw new UnsupportedOperationException();
    }
}
```

#### Good Example

```java
public interface Workable {

    void work();
}
```

```java
public interface Eatable {

    void eat();
}
```

```java
public interface Sleepable {

    void sleep();
}
```

```java
public class HumanWorker
        implements Workable, Eatable, Sleepable {
}
```

```java
public class RobotWorker
        implements Workable {
}
```

#### Benefits

- Smaller and focused interfaces
- Reduced coupling
- Improved flexibility
- Easier maintenance

---

### D — Dependency Inversion Principle (DIP)

High-level modules should not depend on low-level modules. Both should depend on abstractions.

The goal is to depend on interfaces rather than concrete implementations. This reduces coupling and makes systems easier to extend, replace, and test.

#### Bad Example

```java
public class EmailService {

    public void send() {
        System.out.println("Email sent");
    }
}
```

```java
public class NotificationService {

    private final EmailService emailService =
            new EmailService();

    public void notifyUser() {
        emailService.send();
    }
}
```

#### Good Example

```java
public interface MessageService {

    void send();
}
```

```java
public class EmailService implements MessageService {

    @Override
    public void send() {
        System.out.println("Email sent");
    }
}
```

```java
public class SMSService implements MessageService {

    @Override
    public void send() {
        System.out.println("SMS sent");
    }
}
```

```java
public class NotificationService {

    private final MessageService messageService;

    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void notifyUser() {
        messageService.send();
    }
}
```

#### Benefits

- Loose coupling
- Easier testing
- Easier implementation replacement
- Supports Dependency Injection
- Better scalability

---

### Summary

| Principle | Core Idea |
|------------|------------|
| SRP | One class, one responsibility |
| OCP | Extend behavior without modifying existing code |
| LSP | Child classes must be usable in place of parent classes |
| ISP | Prefer small, focused interfaces |
| DIP | Depend on abstractions, not implementations |

---

### Memory Trick

```text
S - Single Responsibility
O - Open/Closed
L - Liskov Substitution
I - Interface Segregation
D - Dependency Inversion
```

#### Quick Interpretation

```text
SRP -> One job
OCP -> Extend, don't modify
LSP -> Child behaves like parent
ISP -> Small interfaces
DIP -> Program against abstractions
```