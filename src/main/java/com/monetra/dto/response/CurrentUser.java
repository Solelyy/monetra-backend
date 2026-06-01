package com.monetra.dto.response;

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
