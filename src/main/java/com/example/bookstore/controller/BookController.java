package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Get a list of all books,"
            + "could be divided into pages")
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book by id", description = "Get exact book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Create a new book", description = "Add a new book to DB")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(summary = "Delete a book", description = "Delete a book by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Update a book", description = "update information about a book by id")
    @PutMapping("/{id}")
    public BookDto updateById(@PathVariable Long id,
                              @Valid @RequestBody CreateBookRequestDto requestDto) {
        return bookService.updateById(id, requestDto);
    }

    @Operation(summary = "Search a book", description = "Search a book by author or title")
    @GetMapping("/search")
    public List<BookDto> searchBooks(BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }
}
