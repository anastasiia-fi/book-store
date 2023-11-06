package com.example.bookstore.dao;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    public Specification<Book> getSpecification(String[] params);
}
