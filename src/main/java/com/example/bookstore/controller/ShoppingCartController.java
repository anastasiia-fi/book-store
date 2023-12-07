package com.example.bookstore.controller;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get user's shopping cart", description = "Get all items from "
            + "user's shopping cart, could be divided into pages")
    @GetMapping
    public ShoppingCartDto getCart(Authentication authentication, Pageable pageable) {
        return shoppingCartService.getCart(authentication, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add a book to shopping cart", description = "Add a book to shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CartItemDto addCartItem(Authentication authentication,
                            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.save(authentication, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update a quantity", description = "Update a quantity of books"
            + "in shopping cart")
    @PutMapping("cart-items/{cartItemId}")
    public CartItemDto updateItem(@PathVariable Long cartItemId,
                                  @Valid @RequestBody CartItemUpdateDto cartItemUpdateDto) {
        return shoppingCartService.updateItem(cartItemId, cartItemUpdateDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete a book from shopping cart", description = "Delete a book "
            + "from shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("cart-items//{cartItemId}")
    public void delete(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
