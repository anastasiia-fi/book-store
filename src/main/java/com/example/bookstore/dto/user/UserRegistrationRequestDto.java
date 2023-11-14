package com.example.bookstore.dto.user;

import com.example.bookstore.validator.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@FieldMatch
public record UserRegistrationRequestDto(
        @NotBlank
        @Size(min = 4, max = 50)
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        String password,

        @NotBlank
        @Size(min = 6, max = 100)
        String repeatPassword,

        @NotNull
        String firstName,
        @NotNull
        String lastName,
        String shippingAddress
) {
}
