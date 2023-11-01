package dao.book;

import dao.SpecificationBuilder;
import dao.SpecificationProviderManager;
import dto.BookSearchParametersDto;
import lombok.RequiredArgsConstructor;
import model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
  private SpecificationProviderManager<Book> bookSpecificationProviderManager;
  @Override
  public Specification<Book> build(BookSearchParametersDto bookSearchParameters) {
    Specification<Book> spec = Specification.where(null);
    if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
      spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("author")
          .getSpecification(bookSearchParameters.authors()));
    }
    if (bookSearchParameters.titles() != null && bookSearchParameters.titles().length > 0) {
      spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("title")
          .getSpecification(bookSearchParameters.titles()));
    }
    return spec;
  }
}
