package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.OrderUpdateDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.service.OrderItemService;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order", description = "Create a new order with items "
            + "in shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponseDto create(
            @RequestBody OrderRequestDto orderRequestDto, Authentication authentication) {
        return orderService.create(orderRequestDto, authentication);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a list of all orders", description = "Get all orders"
            + "could be divided into pages")
    @GetMapping
    public List<OrderDto> getAll(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a status", description = "Update a status of an order")
    @PatchMapping ("/{orderId}")
    public OrderResponseDto update(
            @PathVariable Long orderId, @RequestBody OrderUpdateDto orderUpdateDto) {
        return orderService.update(orderId, orderUpdateDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a list of all order items", description = "Get all order items"
            + "could be divided into pages")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getAllOrderItems(
            @PathVariable Long orderId, Pageable pageable) {
        return orderItemService.getAllOrderItems(orderId, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get an order item by id", description = "Get a specific"
            + " order item by id, could be divided into pages")
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemResponseDto getOrderItemById(
            @PathVariable Long orderId, @PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }
}
