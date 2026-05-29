package com.monetra.auth.controller;

import com.monetra.auth.dto.AuthResponse;
import com.monetra.auth.dto.LoginRequest;
import com.monetra.auth.dto.RegisterRequest;
import com.monetra.auth.service.AuthService;
import com.monetra.security.jwt.JwtService;
import com.monetra.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        User user = authService.registerUser(request);

        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setMessage("User created successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        // 1. Validate credentials
        User user = authService.authenticateUser(
                request.getEmail(),
                request.getPassword()
        );

        // 2. Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        // 3. Create HttpOnly cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //make it true on deployment
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day

        response.addCookie(cookie);

        // 4. Response (no sensitive data)
        AuthResponse authResponse = new AuthResponse();
        authResponse.setId(user.getId());
        authResponse.setEmail(user.getEmail());
        authResponse.setMessage("Login successful");

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete cookie

        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Logged out successfully");

        return ResponseEntity.ok(authResponse);
    }
}