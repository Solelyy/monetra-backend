May 28, 2026 

I generated the SpringBoot project using:
https://start.spring.io/

Build Tool: Maven\
SpringBoot version: 3.5.15\
Java version: 21

### Dependencies
- Lombok
  - reduces boilerplate for getter, setter, constructor, etc
- Spring Web:
  - create REST APIs
  - receive HTTP requests
  - return JSON responses
- Spring Data JPA
  - handles database operations 
  - ORM (Object Relational Mapping)
  - repositories
  - instead of writing raw SQL queries
- Spring Security
  - for auth, authorization, route security, etc
  - without it, anyone can access the apis
- PostgreSQL Driver
  - connect to the database
- Validation
  - gives validations
  - helps prevent invalid inputs