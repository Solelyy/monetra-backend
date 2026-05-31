package com.monetra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
public class MonetraBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonetraBackendApplication.class, args);
    }
}
