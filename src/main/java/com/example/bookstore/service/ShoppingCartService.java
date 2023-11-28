package com.example.bookstore.service;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {

    public CartItemDto save(Authentication authentication, CartItemRequestDto cartItemRequestDto);

    public ShoppingCartDto getCart(Authentication authentication, Pageable pageable);

    public CartItemDto updateItem(Long id, CartItemUpdateDto cartItemUpdateDto);

    void deleteById(Long id);
}
