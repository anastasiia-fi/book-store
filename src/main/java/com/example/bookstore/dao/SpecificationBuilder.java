package com.example.bookstore.dao;

import dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto bookSearchParameters);
}
