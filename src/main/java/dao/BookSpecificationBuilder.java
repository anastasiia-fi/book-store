package dao;

import model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
  @Override
  public Specification<Book> build(BookSearchParameters bookSearchParameters) {
    return null;
  }
}
