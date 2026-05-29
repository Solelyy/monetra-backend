### General key takeaways:

- @Entity = database table
- @Table = explicit table mapping
- @Column = column rules + mapping
- @Id = primary key
- @GeneratedValue = auto increment
- @GeneratedValue(strategy = GenerationType.IDENTITY)
  - database generates id automatically
- @PrePersist = before insert
- @PreUpdate = before update
- LocalDateTime = used for timestamps
- Lombok removes getters/setters boilerplate