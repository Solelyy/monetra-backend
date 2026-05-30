package com.monetra.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthResponse {
    private Long id;
    private String email;
    private String message;
}
