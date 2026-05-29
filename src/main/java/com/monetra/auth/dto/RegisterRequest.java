package com.monetra.auth.dto;

import com.monetra.client.enums.Gender;
import com.monetra.client.enums.Suffix;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterRequest {
    private String email;
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;
    private Suffix suffix;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String mobileNumber;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String zipcode;
    private String region;
    private String country;

}