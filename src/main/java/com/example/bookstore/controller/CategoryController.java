package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing book's categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category", description = "Create a new book's category")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(
            @RequestBody @Valid CreateCategoryRequestDto categoryRequestDto) {
        return categoryService.save(categoryRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all categories", description = "Get a list of all categories,"
            + "could be divided into pages")
    @GetMapping
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get category by id", description = "Get exact category by id")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a category", description = "Update information"
            + "about a category by id")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @PathVariable Long id, @RequestBody CreateCategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a category", description = "Delete a category by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get books by category id", description = "Get a list of books"
            + "of some category by category id")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }
}
