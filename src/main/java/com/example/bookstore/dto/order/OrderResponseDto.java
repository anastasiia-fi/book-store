package com.example.bookstore.dto.order;

import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponseDto(
        LocalDateTime orderDate,
        BigDecimal total,
        String status,
        Set<OrderItemResponseDto> orderItems
) {
}
