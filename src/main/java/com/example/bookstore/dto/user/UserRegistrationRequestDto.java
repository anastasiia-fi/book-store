package com.example.bookstore.dto.user;

import com.example.bookstore.validator.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(message = "Password don`t match",
                field = {"password", "repeatPassword"})
public record UserRegistrationRequestDto(
        @NotBlank
        @Size(min = 8, max = 50)
        String email,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotBlank
        @Size(min = 8, max = 100)
        String repeatPassword,

        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String shippingAddress
) {
}
