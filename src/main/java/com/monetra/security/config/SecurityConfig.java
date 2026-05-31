package com.monetra.security.config;

import com.monetra.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. Disable CSRF (not needed for JWT cookies)
                .csrf(csrf -> csrf.disable())

                // 2. CORS config for frontend
                .cors(cors -> cors.configurationSource(request -> {

                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(List.of(
                            "http://localhost:5173",
                            "https://monetra-mu.vercel.app"
                    ));

                    config.setAllowedMethods(List.of(
                            "GET",
                            "POST",
                            "PUT",
                            "DELETE"
                    ));

                    config.setAllowedHeaders(List.of("*"));

                    // allow cookies (JWT HttpOnly cookie)
                    config.setAllowCredentials(true);

                    return config;
                }))

                // 3. Session disabled
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Route protection rules
                .authorizeHttpRequests(auth -> auth
                        // public endpoints (auth)
                        .requestMatchers("/api/auth/**").permitAll()

                        // everything else requires authentication
                        .anyRequest().authenticated()
                )

                // 5. Disable default Spring login form
                .formLogin(form -> form.disable())

                // 6. Disable HTTP Basic auth
                .httpBasic(httpBasic -> httpBasic.disable())

                // 7. Add JWT filter before Spring authentication filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}