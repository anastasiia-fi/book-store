package com.example.bookstore.service.impl;

import com.example.bookstore.dao.repository.OrderRepository;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.OrderUpdateDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public OrderResponseDto create(OrderRequestDto orderRequestDto, Authentication authentication) {
        User user = shoppingCartService.getPrincipal(authentication);
        Order order = createOrder(user, orderRequestDto);
        setOrderItems(user, order);
        setTotalPrice(order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable) {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderResponseDto update(Long id, OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No order with id " + id)
        );
        order.setStatus(orderUpdateDto.status());
        return orderMapper.toDto(order);
    }

    private Order createOrder(User user, OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.shippingAddress());
        order.setTotal(BigDecimal.ZERO);
        orderRepository.save(order);
        return order;
    }

    private void setOrderItems(User user, Order order) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(orderItemMapper::toOrderItem)
                .collect(Collectors.toSet());

        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);
        shoppingCart.getCartItems().clear();
    }

    private void setTotalPrice(Order order) {
        BigDecimal totalPriceForItem = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(totalPriceForItem);
    }
}
