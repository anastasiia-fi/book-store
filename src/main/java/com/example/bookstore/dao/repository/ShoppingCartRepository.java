package com.example.bookstore.dao.repository;

import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findShoppingCartByUser(User user);
}
