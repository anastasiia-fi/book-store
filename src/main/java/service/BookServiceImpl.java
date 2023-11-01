package service;

import dao.BookRepository;
import dto.BookDto;
import dto.CreateBookRequestDto;
import exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.BookMapper;
import model.Book;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toBook(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find book with id " + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
