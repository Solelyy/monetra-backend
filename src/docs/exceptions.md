## Exceptions in Java

### What is an Exception?

An **exception** is an event that occurs during program execution that disrupts the normal flow of instructions.

When an exception occurs, Java creates an **Exception Object** containing information about the error, then looks for code that can handle it.

Without proper handling, the program may terminate unexpectedly.

Example:

```java
int result = 10 / 0;
```

Output:

```
Exception in thread "main" java.lang.ArithmeticException: / by zero
```

---

## Why Exceptions Exist

Exceptions help developers:

- Detect runtime problems
- Prevent application crashes
- Handle unexpected situations gracefully
- Separate normal business logic from error handling
- Provide meaningful error messages

Without exceptions:

```java
if(errorOccurred){
    // stop everything
}
```

Code would become messy and difficult to maintain.

---

## Exception Hierarchy

Everything starts from the `Throwable` class.

```text
Throwable
│
├── Error
│
└── Exception
     │
     ├── RuntimeException
     │
     └── Checked Exceptions
```

---

## Throwable

`Throwable` is the root class for everything that can be thrown using:

```java
throw new SomeException();
```

Java has two major categories under `Throwable`:

1. Error
2. Exception

---

## Error

Errors represent serious problems that applications generally should not handle.

These usually indicate JVM or system failures.

Examples:

```java
OutOfMemoryError
StackOverflowError
VirtualMachineError
```

Example:

```java
public void recurse() {
    recurse();
}
```

Output:

```text
StackOverflowError
```

Normally, you do not catch Errors.

Bad practice:

```java
catch (Error e) {
    // usually don't do this
}
```

---

## Exception

Exceptions represent conditions that an application can reasonably handle.

Examples:

```java
IOException
SQLException
NullPointerException
IllegalArgumentException
```

Exceptions are divided into:

1. Checked Exceptions
2. Unchecked Exceptions

---

# Checked Exceptions

## What are Checked Exceptions?

Checked exceptions are exceptions that Java forces you to handle.

The compiler checks them during compilation.

If not handled:

```java
FileReader reader = new FileReader("file.txt");
```

Compilation error:

```text
Unhandled exception: FileNotFoundException
```

---

## Common Checked Exceptions

### IOException

Occurs during input/output operations.

```java
FileReader reader = new FileReader("file.txt");
```

Possible exception:

```java
IOException
```

---

### FileNotFoundException

Occurs when a file does not exist.

```java
FileReader reader = new FileReader("missing.txt");
```

---

### SQLException

Occurs during database operations.

```java
connection.prepareStatement(sql);
```

Can throw:

```java
SQLException
```

---

### ClassNotFoundException

Occurs when Java cannot find a class.

```java
Class.forName("com.mysql.Driver");
```

---

## Handling Checked Exceptions

### Try-Catch

```java
try {
    FileReader reader = new FileReader("file.txt");
} catch (FileNotFoundException e) {
    System.out.println(e.getMessage());
}
```

---

### Throws

```java
public void readFile() throws FileNotFoundException {
    FileReader reader = new FileReader("file.txt");
}
```

Responsibility is passed to the caller.

---

# Unchecked Exceptions

## What are Unchecked Exceptions?

Unchecked exceptions are not checked by the compiler.

They occur due to programming mistakes.

They extend:

```java
RuntimeException
```

Java does not force you to handle them.

---

## RuntimeException

Parent class of most programming-related errors.

Examples:

```java
NullPointerException
IllegalArgumentException
ArithmeticException
IndexOutOfBoundsException
```

---

## Common Runtime Exceptions

### NullPointerException (NPE)

Occurs when calling a method on a null reference.

```java
String name = null;

name.length();
```

Output:

```text
NullPointerException
```

---

### ArithmeticException

Occurs during illegal arithmetic operations.

```java
int result = 10 / 0;
```

Output:

```text
ArithmeticException
```

---

### ArrayIndexOutOfBoundsException

Occurs when accessing invalid array indexes.

```java
int[] nums = {1, 2, 3};

System.out.println(nums[5]);
```

Output:

```text
ArrayIndexOutOfBoundsException
```

---

### StringIndexOutOfBoundsException

```java
String name = "Java";

char c = name.charAt(10);
```

Output:

```text
StringIndexOutOfBoundsException
```

---

### NumberFormatException

Occurs when converting invalid strings to numbers.

```java
Integer.parseInt("abc");
```

Output:

```text
NumberFormatException
```

---

### IllegalArgumentException

Occurs when an invalid argument is passed.

```java
public void setAge(int age) {
    if(age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}
```

---

### IllegalStateException

Occurs when an object's state is inappropriate.

```java
if(!account.isActive()) {
    throw new IllegalStateException("Account inactive");
}
```

---

### ClassCastException

Occurs when casting incompatible objects.

```java
Object obj = "Hello";

Integer num = (Integer) obj;
```

Output:

```text
ClassCastException
```

---

### UnsupportedOperationException

Occurs when an operation is not supported.

```java
List<String> list = List.of("A", "B");

list.add("C");
```

Output:

```text
UnsupportedOperationException
```

---

# Checked vs Unchecked Exceptions

| Feature | Checked Exception | Unchecked Exception |
|----------|----------|----------|
| Compiler checks | Yes | No |
| Must handle | Yes | No |
| Extends | Exception | RuntimeException |
| Usually caused by | External factors | Programming mistakes |
| Example | IOException | NullPointerException |

Examples:

Checked:

```java
FileReader reader = new FileReader("file.txt");
```

Unchecked:

```java
String name = null;
name.length();
```

---

# Throwing Exceptions

You can create and throw exceptions manually.

```java
throw new IllegalArgumentException("Invalid age");
```

Example:

```java
public void withdraw(double amount) {

    if(amount <= 0) {
        throw new IllegalArgumentException(
            "Amount must be positive"
        );
    }
}
```

---

# The throw Keyword

Used to explicitly create an exception.

```java
throw new RuntimeException("Something went wrong");
```

Only throws one exception object.

---

# The throws Keyword

Used in method signatures.

```java
public void readFile()
        throws IOException {
}
```

Indicates the method may throw an exception.

---

# Try-Catch Block

Basic structure:

```java
try {

    // risky code

} catch(Exception e) {

    // handling code

}
```

Example:

```java
try {
    int result = 10 / 0;
}
catch(ArithmeticException e) {
    System.out.println("Cannot divide by zero");
}
```

---

# Multiple Catch Blocks

```java
try {

    riskyOperation();

}
catch(IOException e) {

}
catch(SQLException e) {

}
catch(Exception e) {

}
```

Most specific exceptions should come first.

Bad:

```java
catch(Exception e)
catch(IOException e)
```

Compilation error.

---

# Finally Block

Always executes regardless of whether an exception occurs.

```java
try {

}
catch(Exception e) {

}
finally {

}
```

Example:

```java
FileReader reader = null;

try {

    reader = new FileReader("file.txt");

}
catch(Exception e) {

}
finally {

    if(reader != null) {
        reader.close();
    }

}
```

Commonly used for cleanup.

---

# Try-With-Resources

Modern way to automatically close resources.

```java
try(FileReader reader =
        new FileReader("file.txt")) {

}
```

Java automatically closes:

- Files
- Streams
- Database connections
- Sockets

Preferred over manual cleanup.

---

# Exception Propagation

Exceptions travel up the call stack until handled.

Example:

```java
public void methodA() {
    methodB();
}

public void methodB() {
    methodC();
}

public void methodC() {
    throw new RuntimeException();
}
```

Flow:

```text
methodC()
    ↓
methodB()
    ↓
methodA()
    ↓
Main Method
```

If nobody handles it, the application crashes.

---

# Custom Exceptions

You can create your own exception classes.

Example:

```java
public class InsufficientBalanceException
        extends RuntimeException {

    public InsufficientBalanceException(
            String message) {
        super(message);
    }
}
```

Usage:

```java
if(balance < amount) {
    throw new InsufficientBalanceException(
        "Not enough balance"
    );
}
```

---

# Custom Checked Exception

```java
public class InvalidAccountException
        extends Exception {

    public InvalidAccountException(
            String message) {
        super(message);
    }
}
```

Usage:

```java
public void validateAccount()
        throws InvalidAccountException {

}
```

---

# Exception Handling Best Practices

## Catch Specific Exceptions

Good:

```java
catch(FileNotFoundException e)
```

Bad:

```java
catch(Exception e)
```

---

## Don't Swallow Exceptions

Bad:

```java
catch(Exception e) {
}
```

The error disappears completely.

---

## Provide Meaningful Messages

Good:

```java
throw new IllegalArgumentException(
    "Amount must be greater than zero"
);
```

Bad:

```java
throw new IllegalArgumentException();
```

---

## Use RuntimeException for Programming Errors

Examples:

- Invalid arguments
- Invalid state
- Null values
- Business rule violations

```java
throw new IllegalArgumentException();
```

---

## Use Checked Exceptions for Recoverable Situations

Examples:

- Missing files
- Database failures
- Network failures

```java
throws IOException
```

---

## Log Exceptions Properly

Bad:

```java
catch(Exception e) {
    System.out.println("Error");
}
```

Good:

```java
catch(Exception e) {
    logger.error("Failed operation", e);
}
```

---

# Exceptions Commonly Seen in Spring Boot

### MethodArgumentNotValidException

Validation failure.

```java
@NotBlank
private String email;
```

---

### BadCredentialsException

Incorrect username/password.

```java
authenticationManager.authenticate(...)
```

---

### AccessDeniedException

User lacks required permissions.

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

### UsernameNotFoundException

User not found during authentication.

```java
loadUserByUsername()
```

---

### DataIntegrityViolationException

Database constraint violations.

Examples:

- Duplicate email
- Foreign key violations
- Unique constraint violations

---

### EntityNotFoundException

Requested entity does not exist.

Example:

```java
Client client = repository.findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException()
        );
```

---

# Spring Boot Exception Flow

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database

Exception Occurs
    ↑
Repository
    ↑
Service
    ↑
Controller
    ↑
GlobalExceptionHandler
    ↑
HTTP Response
```

Example:

```java
throw new ClientNotFoundException(
    "Client not found"
);
```

Handled by:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
        ClientNotFoundException.class
    )
    public ResponseEntity<?> handle(
            ClientNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
```

Response:

```json
{
    "message": "Client not found"
}
```

---

## Summary

- `Throwable` is the root class.
- `Error` represents JVM/system failures.
- `Exception` represents recoverable application problems.
- Checked exceptions must be handled or declared.
- Unchecked exceptions extend `RuntimeException`.
- `throw` creates an exception.
- `throws` declares possible exceptions.
- `try-catch-finally` handles exceptions.
- `try-with-resources` automatically closes resources.
- Exceptions propagate up the call stack.
- Custom exceptions represent domain-specific problems.
- Spring Boot commonly handles exceptions using `@RestControllerAdvice`.
- Global exception handlers convert exceptions into proper HTTP responses.