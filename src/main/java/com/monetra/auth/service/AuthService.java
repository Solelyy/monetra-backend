package com.monetra.auth.service;

import com.monetra.user.entity.User;
import com.monetra.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Register
    public User registerUser(String email, String password, String firstName, String lastName) {
        String hashedPassword = passwordEncoder.encode(password);

        User user= User.builder()
                .email(email)
                .password(hashedPassword)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        return userRepository.save(user);
    }

//    //Login
//    public boolean loginUser(String email, String password) {
//        Optional<User> user = userRepository.findByEmail(email);
//
//        if (user.isEmpty()) {
//            return false;
//        }
//
//        User userFound = user.get();
//
//        return passwordEncoder.matches(password, userFound.getPassword());
//    }
}
