package dao.book.spec;

import dao.SpecificationProvider;
import java.util.Arrays;
import model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
  @Override
  public String getKey() {
    return "author";
  }

  public Specification<Book> getSpecification(String[] params) {
    return (root, query, criteriaBuilder) -> root.get("author").in(Arrays.stream(params).toArray());
  }
}
