package com.example.bookstore.service;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.OrderUpdateDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponseDto create(OrderRequestDto orderRequestDto, Authentication authentication);

    List<OrderDto> getAll(Pageable pageable);

    OrderResponseDto update(Long id, OrderUpdateDto orderUpdateDto);
}
