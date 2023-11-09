package com.example.bookstore.dto.user;

import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDto(
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        String shippingAddress
) {
}
