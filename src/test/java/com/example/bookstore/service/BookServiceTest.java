package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dao.repository.BookRepository;
import com.example.bookstore.dao.repository.CategoryRepository;
import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.mapper.impl.BookMapperImpl;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Find book by valid id
            """)
    public void findById_WithValidId_ShouldReturnValidBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Example book");
        book.setAuthor("Author A");
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.findById(bookId);

        assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("""
            Find book by invalid id
            """)
    public void findById_WithInvalidId_ShouldThrowException() {
        Long bookId = -1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findById(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find book with id " + bookId);
    }

    @Test
    @DisplayName("""
            Delete book by valid id
            """)
    public void deleteById_WithValidId_ShouldDeleteBook() {
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Create book with valid data
            """)
    public void createBook_WithValidData_ShouldCreateValidBook() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto(
                "Test book",
                "Test Author",
                "0123456789",
                BigDecimal.valueOf(100),
                "Description",
                "Cover Image",
                Set.of(1L)
        );
        Category category = new Category();
        category.setId(1L);
        Book book = new Book();
        book.setTitle(createBookRequestDto.title());
        book.setAuthor(createBookRequestDto.author());
        book.setIsbn(createBookRequestDto.isbn());
        book.setPrice(createBookRequestDto.price());
        book.setDescription(createBookRequestDto.description());
        book.setCoverImage(createBookRequestDto.coverImage());
        book.setCategories(Set.of(category));

        when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.save(createBookRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("title", "Test book")
                .hasFieldOrPropertyWithValue("author", "Test Author")
                .hasFieldOrPropertyWithValue("isbn", "0123456789")
                .hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(100))
                .hasFieldOrPropertyWithValue("description", "Description")
                .hasFieldOrPropertyWithValue("coverImage", "Cover Image");
    }

    @Test
    @DisplayName("""
            Find books by category id
            """)
    public void findBookByCategoryId_WithValidData_ShouldReturnValidBooks() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds(
                1L,
                "Test book",
                "Test Author",
                "0123456789",
                BigDecimal.valueOf(100),
                "Description",
                "Cover Image"
        );

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test book");
        book.setAuthor("Test Author");
        book.setIsbn("0123456789");
        book.setPrice(BigDecimal.valueOf(100));
        book.setDescription("Description");
        book.setCoverImage("Cover Image");
        book.setCategories(Set.of(category));

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto);

        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(List.of(book));

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);
        assertEquals(expected.get(0), actual.get(0));
    }
}
