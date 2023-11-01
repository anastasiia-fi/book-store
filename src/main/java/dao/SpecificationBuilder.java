package dao;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
  Specification<T> build(BookSearchParameters bookSearchParameters);
}
