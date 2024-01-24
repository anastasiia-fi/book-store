package com.example.bookstore.mapper;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.model.CartItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);
}
