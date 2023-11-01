package dao;

import model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
  String getKey();
  public Specification<Book> getSpecification(String[] params);
}
