package com.example.bookstore.dto.category;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequestDto(
        @NotNull
        String name,
        @NotNull
        String description
) {
}
