package com.monetra.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
