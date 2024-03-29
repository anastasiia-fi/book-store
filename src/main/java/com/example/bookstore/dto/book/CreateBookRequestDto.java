package com.example.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;

public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        String isbn,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        String description,
        String coverImage,
        Set<Long> categoryIds) {
}
