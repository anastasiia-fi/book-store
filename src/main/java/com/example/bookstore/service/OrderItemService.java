package com.example.bookstore.service;

import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {

    List<OrderItemResponseDto> getAllOrderItems(Long id, Pageable pageable);

    OrderItemResponseDto getOrderItemById(Long orderItemId);
}
