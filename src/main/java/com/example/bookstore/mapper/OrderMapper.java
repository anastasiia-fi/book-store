package com.example.bookstore.mapper;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl",
        uses = OrderItemMapper.class
)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    @Mapping(source = "user.id", target = "userId")
    OrderDto toOrderDto(Order order);
}
