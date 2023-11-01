package dao;

import dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
  Specification<T> build(BookSearchParametersDto bookSearchParameters);
}
