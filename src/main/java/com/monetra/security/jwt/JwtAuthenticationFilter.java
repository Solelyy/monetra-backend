package com.monetra.security.jwt;

import com.monetra.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;
        String email = null;

        log.info("JTW Filter Executed: {} {}", request.getMethod(), request.getRequestURI());

        // 1. Get JWT from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. If token exists, try to authenticate user
        if (token != null) {
            try {

                // 3. Extract email from JWT
                email = jwtService.extractEmail(token);

                // 4. Check if user is not already authenticated
                if (email != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null &&
                        jwtService.isTokenValid(token, email)) {

                    // 5. Load user from database
                    var userDetails = userDetailsService.loadUserByUsername(email);

                    // 6. Create authentication object
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 7. Attach request details (IP, session info, etc.)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 8. Set user as authenticated in Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("Current user: {}", SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getName());
                }

            } catch (Exception e) {
                // If anything fails, clear security context
                log.info("Caught the problem: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        // 9. Continue request chain
        filterChain.doFilter(request, response);
    }
}