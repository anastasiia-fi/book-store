package com.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemUpdateDto(
        @NotNull
        @Positive
        int quantity
) {
}
