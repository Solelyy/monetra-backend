package com.monetra.controllers;

import com.monetra.dto.response.AuthResponse;
import com.monetra.dto.response.CurrentUser;
import com.monetra.dto.request.LoginRequest;
import com.monetra.dto.request.RegisterRequest;
import com.monetra.services.AuthService;
import com.monetra.security.jwt.JwtService;
import com.monetra.models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
            @RequestBody LoginRequest request
    ) {
        User user = authService.authenticateUser(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtService.generateToken(user.getEmail());

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setId(user.getId());
        authResponse.setEmail(user.getEmail());
        authResponse.setMessage("Login successful");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //make this true in deployed
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete cookie

        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Logged out successfully");

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CurrentUser> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}