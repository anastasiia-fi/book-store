package com.example.bookstore.dao.repository;

import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByShoppingCartAndBook_Id(ShoppingCart shoppingCart, Long id);
}
