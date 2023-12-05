package com.example.bookstore.service.impl;

import com.example.bookstore.dao.repository.BookRepository;
import com.example.bookstore.dao.repository.CartItemRepository;
import com.example.bookstore.dao.repository.ShoppingCartRepository;
import com.example.bookstore.dao.repository.UserRepository;
import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public CartItemDto save(Authentication authentication, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(
                getUser(authentication));

        CartItem cartItem = cartItemRepository
                .findCartItemByShoppingCartAndBook_Id(shoppingCart, cartItemRequestDto.bookId())
                .orElseGet(() -> createCartItem(cartItemRequestDto, shoppingCart));
        if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.quantity());
        }
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public ShoppingCartDto getCart(Authentication authentication, Pageable pageable) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartByUser(
                getUser(authentication)));

    }

    @Override
    public CartItemDto updateItem(Long id, CartItemUpdateDto cartItemUpdateDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book with id " + id));
        cartItem.setQuantity(cartItemUpdateDto.quantity());
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    private CartItem createCartItem(CartItemRequestDto cartItemRequestDto,
                                    ShoppingCart shoppingCart) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.findById(cartItemRequestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("No book with id "
                        + cartItemRequestDto.bookId())));
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(cartItemRequestDto.quantity());
        return cartItem;
    }

    public User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("No user with email " + email));
    }

    public void registerShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
