package com.example.bookstore.dto.user;

public record UserDto(
        Long id,
        String email,
        String password,
        String firstName,
        String lastName,
        String shippingAddress
        ) {
}
