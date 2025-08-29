package com.pranay.validation.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {
    private String username;

    private String houseNumber;

    private String password;

    @Builder.Default
    private String role = "ROLE_USER";
}
