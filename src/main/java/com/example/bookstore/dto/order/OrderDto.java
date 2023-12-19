package com.example.bookstore.dto.order;

import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemResponseDto> orderItems
) {
}
