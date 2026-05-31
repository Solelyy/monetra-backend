package com.monetra.dto.request;

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
