package com.monetra.models;

import com.monetra.enums.Gender;
import com.monetra.enums.Suffix;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "middle_name", nullable = false, length = 50)
    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Suffix suffix;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "client")
    private Account account;

    @OneToOne(mappedBy = "client")
    private Address address;

    @OneToOne(mappedBy = "client")
    private ContactDetail contactDetail;
}
