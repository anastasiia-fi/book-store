package dao.book.spec;

import dao.SpecificationProvider;
import java.util.Arrays;
import model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
  @Override
  public String getKey() {
    return "title";
  }

  public Specification<Book> getSpecification(String[] params) {
    return (root, query, criteriaBuilder) -> root.get("title").in(Arrays.stream(params).toArray());
  }
}
