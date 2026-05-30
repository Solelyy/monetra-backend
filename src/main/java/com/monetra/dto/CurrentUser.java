package com.monetra.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CurrentUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
