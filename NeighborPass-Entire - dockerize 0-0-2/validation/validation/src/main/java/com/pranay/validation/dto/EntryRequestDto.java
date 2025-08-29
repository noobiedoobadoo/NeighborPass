package com.pranay.validation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EntryRequestDto {

    private String name;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotBlank(message = "House number is required")
    @Pattern(regexp = "^\\d{3}$", message = "House number must be exactly 3 digits")
    private String houseNumber;
}
