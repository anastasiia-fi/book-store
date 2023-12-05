package com.example.bookstore.service.impl;

import com.example.bookstore.dao.repository.OrderItemRepository;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemResponseDto> getAllOrderItems(Long id, Pageable pageable) {
        return orderItemRepository.findOrderItemsByOrder_Id(id).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemById(Long orderItemId) {
        return orderItemMapper.toDto(orderItemRepository.findById(orderItemId).orElseThrow(
                () -> new EntityNotFoundException("No order item with id " + orderItemId)
        ));
    }
}
