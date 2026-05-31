## Spring Security Fundamentals

### Overview

Spring Security is a framework that provides security features for Spring applications.

Primary responsibilities:

- Authentication
- Authorization
- Session Management
- Password Encryption
- CSRF Protection
- Security Headers
- Request Filtering

Without Spring Security:

```text
Request
   ↓
Controller
```

With Spring Security:

```text
Request
   ↓
Spring Security Filters
   ↓
Controller
```

Every incoming request passes through a chain of security filters before reaching application endpoints.

---

## Authentication vs Authorization

These concepts are often confused but serve different purposes.

### Authentication

Authentication answers:

> Who is making this request?

Example:

```text
Email: jessa@gmail.com
Password: password123
```

The system verifies whether the credentials belong to a valid user.

Result:

```text
Authenticated = true
```

Authentication is concerned with identity verification.

---

### Authorization

Authorization answers:

> What actions is the authenticated user allowed to perform?

Example:

```text
User: Jessa
Role: ADMIN
```

Permissions:

```text
Create Users
Delete Users
Manage Accounts
```

Example:

```text
User: Mark
Role: CASHIER
```

Permissions:

```text
View Products
Process Sales
```

Authorization is concerned with access control and permissions.

---

## Security Filter Chain

The Security Filter Chain is the core of Spring Security.

A filter is code that executes before a request reaches a controller.

Example:

```text
Request
   ↓
Filter 1
   ↓
Filter 2
   ↓
Filter 3
   ↓
Controller
```

Spring Security contains many built-in filters.

Examples:

```text
Authentication Filter
Authorization Filter
CSRF Filter
Session Management Filter
Logout Filter
```

Custom filters can also be inserted into the chain.

Example:

```java
.addFilterBefore(
    jwtAuthenticationFilter,
    UsernamePasswordAuthenticationFilter.class
)
```

This places the custom JWT filter before Spring Security's username/password authentication filter.

---

## SecurityContext

SecurityContext stores information about the currently authenticated user during the lifetime of a request.

Location:

```java
SecurityContextHolder.getContext()
```

Purpose:

```text
Stores the current Authentication object
```

Retrieving the authenticated user:

```java
Authentication authentication =
    SecurityContextHolder
        .getContext()
        .getAuthentication();
```

Common contents:

- Principal (UserDetails)
- Authorities
- Roles
- Authentication Status

---

## Authentication Object

After successful authentication, Spring Security creates an Authentication object.

Purpose:

```text
Represents the authenticated user
```

Contains:

- Username
- Authorities
- Roles
- Authentication Status

Example:

```text
Email: jessa@gmail.com
Role: CLIENT
Authenticated: true
```

Stored in:

```java
SecurityContext
```

---

## UserDetails

Spring Security represents users through the UserDetails interface.

Example:

```java
public class CustomUserDetails
        implements UserDetails
```

Purpose:

```text
Provides user information in a format understood by Spring Security.
```

Typical contents:

- Username
- Password
- Authorities
- Roles

---

## UserDetailsService

UserDetailsService is responsible for loading user information.

Example:

```java
public class CustomUserDetailsService
        implements UserDetailsService
```

Important method:

```java
loadUserByUsername()
```

Example implementation:

```java
User user =
    userRepository
        .findByEmail(email)
```

Returns:

```java
CustomUserDetails
```

Spring Security uses this service whenever user information must be loaded.

---

## Password Encoding

Passwords should never be stored as plain text.

Bad:

```text
password123
```

Good:

```text
$2a$10$...
```

Spring Security provides:

```java
PasswordEncoder
```

Common implementation:

```java
BCryptPasswordEncoder
```

---

### Registration Flow

```text
Raw Password
      ↓
PasswordEncoder.encode()
      ↓
Stored Hash
```

Example:

```java
passwordEncoder.encode(password)
```

---

### Login Verification

```text
User Password
       ↓
PasswordEncoder.matches()
       ↓
True / False
```

Example:

```java
passwordEncoder.matches(
    rawPassword,
    storedHash
)
```

---

## Session-Based Authentication

Traditional authentication uses sessions.

Flow:

```text
Login
   ↓
Session Created
   ↓
Session ID Generated
   ↓
Browser Stores Session ID
```

Example:

```text
JSESSIONID=123456
```

Server storage:

```text
Session ID: 123456
User: Jessa
```

---

### Subsequent Requests

```text
Browser
   ↓
JSESSIONID=123456
   ↓
Server
   ↓
Lookup Session
   ↓
Authenticated
```

The server remembers the authenticated user using the session.

---

## Stateless Authentication

JWT authentication is typically stateless.

Characteristics:

- No server-side session storage
- No JSESSIONID
- No session lookup
- Every request contains authentication information

Meaning:

```text
The server does not remember previous requests.
```

Every request must prove identity independently.

---

## JWT Authentication

JWT stands for:

```text
JSON Web Token
```

Purpose:

```text
Provides stateless authentication.
```

Common contents:

- User ID
- Email
- Roles
- Expiration Date
- Claims

Example:

```text
eyJhbGciOi...
```

---

### Login Flow

```text
Login Request
      ↓
Verify Credentials
      ↓
Generate JWT
      ↓
Return JWT
```

---

### Request Flow

```text
Request
   ↓
JWT Filter
   ↓
Validate JWT
   ↓
Extract User Information
   ↓
Create Authentication Object
   ↓
Store Authentication in SecurityContext
   ↓
Controller
```

---

## CORS

CORS stands for:

```text
Cross-Origin Resource Sharing
```

CORS is a browser security mechanism.

---

### What is an Origin?

An origin consists of:

```text
Protocol
Host
Port
```

Example:

```text
http://localhost:5173
```

Different origin:

```text
http://localhost:8080
```

Even though both use localhost, the ports are different.

Result:

```text
Different Origin
```

---

### Why CORS Exists

Frontend:

```text
http://localhost:5173
```

Backend:

```text
http://localhost:8080
```

Browser behavior:

```text
Different Origin
        ↓
Blocked by Browser
```

By default, browsers prevent websites from communicating with different origins.

---

### Allowing Cross-Origin Requests

Example:

```java
config.setAllowedOrigins(
    List.of("http://localhost:5173")
);
```

Meaning:

```text
Requests from localhost:5173 are allowed.
```

---

## Preflight Requests

Before certain requests, browsers send a preflight request.

Common methods:

```text
PUT
PATCH
DELETE
```

Preflight request:

```http
OPTIONS /api/accounts
```

Purpose:

```text
Checks whether the actual request is allowed.
```

Browser asks:

```text
Allowed Origin?
Allowed Methods?
Allowed Headers?
```

If allowed:

```text
Actual Request Sent
```

---

## Credentials

Credentials include:

- Cookies
- Authorization Headers
- Client Certificates

When using JWT cookies:

```java
config.setAllowCredentials(true);
```

Frontend:

```javascript
fetch(url, {
  credentials: "include"
});
```

Without these settings:

```text
Cookies are not sent.
```

---

## CSRF

CSRF stands for:

```text
Cross-Site Request Forgery
```

A security vulnerability that exploits automatically sent cookies.

---

### Example Scenario

User logs into:

```text
bank.com
```

Browser stores:

```text
Session Cookie
```

User visits:

```text
evil-site.com
```

Malicious website sends:

```http
POST /transfer-money
```

Browser automatically attaches:

```text
Session Cookie
```

Bank receives:

```text
Valid Session Cookie
```

Bank assumes the request came from the authenticated user.

---

### Why CSRF Exists

CSRF exists because browsers automatically send cookies with requests.

Especially:

```text
Session Cookies
```

---

### CSRF Protection

Spring Security generates a CSRF token.

Request flow:

```text
Request
   ↓
CSRF Token Included
   ↓
Server Validation
   ↓
Allowed
```

Without a valid token:

```text
403 Forbidden
```

---

## Why JWT Applications Often Disable CSRF

Example:

```java
.csrf(csrf -> csrf.disable())
```

Common reasons:

```text
No server-side session
Authentication handled by JWT
Stateless architecture
```

Common stack:

```text
React + Spring Boot
Vue + Spring Boot
Angular + Spring Boot
```

---

## SessionCreationPolicy

Controls how Spring Security manages sessions.

---

### ALWAYS

Always creates a session.

```java
SessionCreationPolicy.ALWAYS
```

---

### IF_REQUIRED

Creates a session only when necessary.

```java
SessionCreationPolicy.IF_REQUIRED
```

Default behavior.

---

### NEVER

Does not create sessions but can use existing ones.

```java
SessionCreationPolicy.NEVER
```

---

### STATELESS

Most common for JWT authentication.

```java
SessionCreationPolicy.STATELESS
```

Meaning:

```text
No Session Creation
No Session Usage
Every Request Authenticates Independently
```

---

## HTTP Basic Authentication

Legacy authentication mechanism.

Example:

```http
Authorization:
Basic dXNlcjpwYXNz
```

Contents:

```text
username:password
```

Encoded using Base64.

Usually disabled in JWT-based applications.

Example:

```java
.httpBasic(httpBasic -> httpBasic.disable())
```

---

## Form Login

Spring Security can automatically generate a login page.

Enabled:

```java
.formLogin()
```

Generated endpoint:

```text
/login
```

For frontend frameworks such as React, form login is typically disabled.

Example:

```java
.formLogin(form -> form.disable())
```

Reason:

```text
Authentication is handled by the frontend application.
```

---

## Typical JWT Authentication Flow

Login:

```text
User
   ↓
AuthController
   ↓
AuthService
   ↓
Verify Credentials
   ↓
Generate JWT
   ↓
Store JWT Cookie
   ↓
Response
```

Subsequent Requests:

```text
Request
     ↓
JWT Cookie
     ↓
JwtAuthenticationFilter
     ↓
Validate JWT
     ↓
Load User
     ↓
Create Authentication
     ↓
Store in SecurityContext
     ↓
Controller
     ↓
Service
```

Retrieving the authenticated user:

```java
SecurityContextHolder
    .getContext()
    .getAuthentication();
```

---

## Key Takeaways

### Authentication

```text
Who is making the request?
```

Verifies identity.

---

### Authorization

```text
What actions are allowed?
```

Controls permissions.

---

### Security Filter Chain

```text
Request
 ↓
Filters
 ↓
Controller
```

Core component of Spring Security.

---

### SecurityContext

Stores information about the currently authenticated user.

---

### JWT

Provides stateless authentication.

---

### CORS

Allows controlled communication between different origins.

---

### CSRF

Protects against forged requests that exploit automatically sent cookies.

---

### STATELESS

No sessions are created or used.

Every request authenticates independently.

---

### PasswordEncoder

Passwords should always be hashed.

Recommended implementation:

```java
BCryptPasswordEncoder
```