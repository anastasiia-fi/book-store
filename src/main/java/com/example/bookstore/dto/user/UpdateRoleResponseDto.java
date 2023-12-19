package com.example.bookstore.dto.user;

import java.util.Set;

public record UpdateRoleResponseDto(
        Long id,
        Set<String> roles
) {
}
