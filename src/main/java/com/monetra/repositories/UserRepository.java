package com.monetra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.monetra.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
